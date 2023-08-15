package com.iti.itiproject.ui.setup.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.iti.itiproject.data.datastore.DataStoreImplementation
import com.iti.itiproject.databinding.FragmentLoginBinding
import com.iti.itiproject.model.auth.AuthModel
import com.iti.itiproject.ui.home.HomeActivity
import com.iti.itiproject.utils.hideKeypad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginFragment : Fragment() {

    @Inject
    lateinit var dataStore: DataStoreImplementation

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private var isValidLoginData: Boolean = false
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var saveUser: AuthModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        dataStore = DataStoreImplementation(appContext = context)

        CoroutineScope(Dispatchers.IO).launch {
            if (dataStore.isLoggedIn()) {
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
                requireActivity().finish()
            }
        }
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                requireActivity().hideKeypad()
                login()
            }
            tvSignUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }
            tvForgotPassword.setOnClickListener {
                if (etEmail.text.toString().isNotEmpty()) {
                    auth.sendPasswordResetEmail(etEmail.text.toString())
                    Toast.makeText(requireContext() , "Check your email", Toast.LENGTH_SHORT).show()
                }else
                    Toast.makeText(requireContext() , "Enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUserData() {
        email = binding.etEmail.text.toString()
        password = binding.etPassword.text.toString()
    }

    private fun login() {
        setUserData()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            isValidLoginData = viewModel.isValidLogin(email = email, password = password)
            if (isValidLoginData) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        auth.signInWithEmailAndPassword(email, password).await()

                        val userId = auth.currentUser?.uid ?: ""
                        val database = FirebaseDatabase.getInstance()
                        val userReference = database.getReference("users").child(userId)

                        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val userData = snapshot.getValue(AuthModel::class.java)
                                if (userData != null) {
                                    saveDataStore(userData)
                                    Log.d("dataFetch", userData.toString())
                                    startActivity(
                                        Intent(
                                            requireActivity(),
                                            HomeActivity::class.java
                                        )
                                    )
                                    requireActivity().finish()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("dataFetch", error.toString())
                                Toast.makeText(
                                    requireContext(),
                                    error.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else if (viewModel.email.value.toString().isNotEmpty()) {
                Toast.makeText(context, "Invalid email", Toast.LENGTH_LONG).show()
            } else if (viewModel.password.value.toString().isNotEmpty()) {
                Toast.makeText(context, "Invalid password", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Email or Password field is empty", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveDataStore(data: AuthModel) {
        CoroutineScope(Dispatchers.IO).launch {
            saveUser = data
            viewModel.saveUserDataAndLogFlag(saveUser, context = context)
        }
    }
}