package com.example.notesapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.notesapp.Database.DatabaseHelper
import com.example.notesapp.databinding.FragmentNotesDetailActivityBinding
import com.example.notesapp.model.NoteModel

class NotesDetailActivity : Fragment() {
    private lateinit var binding: FragmentNotesDetailActivityBinding
    private lateinit var db : DatabaseHelper
    private var noteId =  -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesDetailActivityBinding.inflate(layoutInflater,container,false)
        db = DatabaseHelper(requireContext())
        noteId = arguments?.getInt("noteId")!!
        if (noteId!=-1){
            val note = db.getNotesById(noteId)
            binding.updateTitleEt.setText(note.title)
            binding.updateDescEt.setText(note.content)

            binding.updateSaveBtn.setOnClickListener {
                val newTitle = binding.updateTitleEt.text.toString()
                val newContent= binding.updateDescEt.text.toString()

                if (newTitle.isNotEmpty() && newContent.isNotEmpty()) {
                    val updatedNote = NoteModel(noteId, newTitle, newContent)
                    db.updateNotes(updatedNote)
                    findNavController().navigateUp()
                    Toast.makeText(requireContext(),"Note Update Successfully",Toast.LENGTH_SHORT).show()
                }
            }
        }else{
           findNavController().navigateUp()
        }


        return binding.root
    }
}