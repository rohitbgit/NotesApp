package com.example.notesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Database.DatabaseHelper
import com.example.notesapp.NotesScreenDirections
import com.example.notesapp.R
import com.example.notesapp.model.NoteModel

class NoteAdapter(private val context: Context, private var notes : List<NoteModel>, private val navController: NavController):RecyclerView.Adapter<NoteAdapter.TaskViewHolder>() {

    val db : DatabaseHelper = DatabaseHelper(context)
    inner class TaskViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTv)
        val content: TextView = itemView.findViewById(R.id.descTv)
        val updateBtn :ImageView= itemView.findViewById(R.id.updateNoteBtn)
        val deleteBtn :ImageView = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.note_item_layout,parent,false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content

        holder.updateBtn.setOnClickListener {
            val action = NotesScreenDirections.actionNotesScreenToNotesDetailActivity(note.id)
            navController.navigate(action)
        }

        holder.deleteBtn.setOnClickListener {
            db.deleteNote(note.id)
            refreshNotes(db.getAllNotes())
            Toast.makeText(context,"Note Deleted",Toast.LENGTH_SHORT).show()
        }
    }
    private fun refreshNotes(newNotes : List<NoteModel>){
        this.notes = newNotes
        notifyDataSetChanged()
    }
}