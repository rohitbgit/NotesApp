package com.example.notesapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.notesapp.databinding.FragmentLoginScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginScreen : Fragment() {
    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(layoutInflater,container,false)
        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.web_client_id))
            .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)

        binding.googleCardBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
        return binding.root
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {result->
        if(result.resultCode==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
            if (task.isSuccessful){
                val account: GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(requireContext(),"Login Successfully",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginScreen_to_notesScreen)
                    }else{
                        Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }else{
            Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
        }
    }
    private fun handleSignInResult(completedTask : Task<GoogleSignInAccount>){
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG,"firebaseAuthWithGoogle"+ account.id)

            if (isAdded()){
                val sharePref = activity?.getSharedPreferences("NotesApp",Context.MODE_PRIVATE) ?: return
                with(sharePref.edit()){
                    putBoolean("isLoggedIn",true)
                    apply()
                }
            }
        }catch (e: ApiException){
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

}
