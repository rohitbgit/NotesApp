package com.example.notesapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.notesapp.Database.DatabaseHelper
import com.example.notesapp.databinding.FragmentAddNoteScreenBinding
import com.example.notesapp.model.NoteModel

class AddNoteScreen : Fragment() {
    private lateinit var binding: FragmentAddNoteScreenBinding
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNoteScreenBinding.inflate(layoutInflater,container,false)
        databaseHelper = DatabaseHelper(requireContext())
        binding.saveBtn.setOnClickListener {
            val title = binding.titleEt.text.toString()
            val content = binding.descEt.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()){
                val notes = NoteModel(0,title,content)
                databaseHelper.insertNotes(notes)
                binding.titleEt.text?.clear()
                binding.descEt.text?.clear()
                Toast.makeText(requireContext(),"Note Saved",Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }else{
                Toast.makeText(requireContext(),"Please fill all the details",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}