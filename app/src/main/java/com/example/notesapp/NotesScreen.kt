package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesapp.Database.DatabaseHelper
import com.example.notesapp.adapter.NoteAdapter
import com.example.notesapp.databinding.FragmentNotesScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class NotesScreen : Fragment() {
    private lateinit var binding : FragmentNotesScreenBinding
    private lateinit var db : DatabaseHelper
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        noteAdapter = NoteAdapter(requireContext(),db.getAllNotes(),navController)
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notesRecyclerView.adapter = noteAdapter
        binding.notesRecyclerView.setHasFixedSize(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesScreenBinding.inflate(layoutInflater,container,false)
        db = DatabaseHelper(requireContext())
        val toolbar = binding.toolBar
        toolbar.inflateMenu(R.menu.menu_item)
        toolbar.setOnMenuItemClickListener { menuItem->
            when(menuItem.itemId){
                R.id.signOutBtn -> {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.web_client_id))
                        .requestEmail().build()
                    GoogleSignIn.getClient(requireContext(),gso).signOut()
                    navController.navigate(R.id.action_notesScreen_to_loginScreen)
                    true
                }
                else -> false
            }
        }
        binding.addNoteBtn.setOnClickListener {
            navController.navigate(R.id.action_notesScreen_to_addNoteScreen)
        }

        return binding.root
    }

}