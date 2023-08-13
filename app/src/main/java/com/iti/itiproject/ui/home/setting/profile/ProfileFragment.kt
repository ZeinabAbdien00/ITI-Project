package com.iti.itiproject.ui.home.setting.profile

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.iti.itiproject.R
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.databinding.FragmentProfileBinding
import com.iti.itiproject.model.auth.AuthModel
import com.iti.itiproject.utils.convertStringToBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException

class ProfileFragment : Fragment() {

    private lateinit var dataStore: DataStoreImplementation
    private lateinit var viewModel: ProfileViewModel

    private var uri: Uri? = null

    private var sImg: String? = null

    private lateinit var user: FirebaseUser

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance().currentUser!!
        dataStore = DataStoreImplementation(appContext = context)
        viewModel = ProfileViewModel(dataStore)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observation()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.apply {
            ivEditImage.setOnClickListener {
                showBottomSheet()
            }
            btnProfileBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSave.setOnClickListener {
                saveData()
            }
        }
    }

    private fun saveData() {
        CoroutineScope(Dispatchers.IO).launch {

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("users").child(dataStore.getUserId())

            changePassword()

            binding.apply {
                val userName = etUserName.text.toString()
                val phone = etUserPhone.text.toString()
                val email = etUserEmail.text.toString()
                val password = etUserPassword.text.toString()

                viewModel.saveUserDataAndLogFlag(
                    AuthModel(
                        userId = dataStore.getUserId(),
                        fullName = userName,
                        phone = phone,
                        email = email,
                        password = password,
                        imgUri = sImg
                    )
                )


                val updates = hashMapOf<String, Any>(
                    "userId" to dataStore.getUserId(),
                    "fullName" to userName,
                    "phone" to phone,
                    "email" to email,
                    "password" to password,
                    "imgUri" to sImg!!
                )

                reference.updateChildren(updates)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Updated Saved", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error with Saved", Toast.LENGTH_SHORT)
                            .show()
                    }

            }
        }
    }

    private fun imageRefactored(uri: Uri) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val myBitmap = BitmapFactory.decodeStream(inputStream)

            val stream = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

            val byte = stream.toByteArray()
            sImg = Base64.encodeToString(byte, Base64.DEFAULT)
            binding.ivUser.setImageBitmap(myBitmap)
            inputStream!!.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun changePassword() {

        CoroutineScope(Dispatchers.IO).launch {

            if (user != null) {
                val newPassword =
                    binding.etUserPassword.text.toString()
                val email = user.email

                val credential = EmailAuthProvider.getCredential(
                    email!!,
                    dataStore.getUser().password.toString()
                )

                user.reauthenticate(credential)
                    .addOnCompleteListener { authTask ->
                        authTask.toString()
                        if (authTask.isSuccessful) {
                            user.updatePassword(newPassword)
                                .addOnCompleteListener { updatePasswordTask ->
                                    if (updatePasswordTask.isSuccessful) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Updating ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Error with Updating ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            val reauthError = authTask.exception
                            Log.d("reauthError", reauthError.toString())
                        }
                    }
            }
        }
    }


    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.choose_image, null)
        val btnCamera: LinearLayout = view.findViewById(R.id.camera_choice)
        btnCamera.setOnClickListener {
            startCameraIntent()
            dialog.dismiss()
        }
        val btnGallery: LinearLayout = view.findViewById(R.id.gallery_choice)
        btnGallery.setOnClickListener {
            startGalleryIntent()
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }


    private fun startCameraIntent() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        uri = requireContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        cameraResultLauncher.launch(cameraIntent)
    }

    private fun startGalleryIntent() {
        val i = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        galleryResultLauncher.launch(i)
    }

    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val data = result.data
                uri = data!!.data
                imageRefactored(uri!!)
            }
        }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (uri != null) {
                    imageRefactored(uri!!)
                }
            }
        }


    private fun observation() {
        viewModel.getUserData()
        viewModel.userData.observe(viewLifecycleOwner, Observer { response ->
            if (response != null)
                setData(response)
        })
    }

    private fun setData(data: AuthModel) {

        binding.apply {
            etUserName.setText(data.fullName.toString())
            etUserEmail.setText(data.email.toString())
            etUserPhone.setText(data.phone.toString())
            etUserPassword.setText(data.password.toString())
            tvUserName.text = data.fullName.toString()
            if (data.imgUri == "") {
                ivUser.setImageResource(R.drawable.ic_person)
            } else {
                sImg = data.imgUri!!
                ivUser.setImageBitmap(convertStringToBitmap(sImg!!))
            }
        }
    }

}