package com.example.notesapp.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notesapp.model.NoteModel

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {

    companion object {
        const val DB_NAME = "MyDatabase.db"
        const val TABLE_NAME = "NoteTable"
        const val ID = "id"
        const val TITLE_NAME = "titleName"
        const val TITLE_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery =
            "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, $TITLE_NAME TEXT, $TITLE_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNotes(notes: NoteModel) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(TITLE_NAME, notes.title)
            put(TITLE_CONTENT, notes.content)
        }
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun getAllNotes(): List<NoteModel> {
        val noteList = mutableListOf<NoteModel>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_NAME))
                    val content = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_CONTENT))

                    val note = NoteModel(id, title, content)
                    noteList.add(note)
                } while (cursor.moveToNext())

            }
        } finally {
            cursor.close()
            db.close()
        }
        return noteList
    }
    fun updateNotes(notes: NoteModel) : Int{
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put( TITLE_NAME,notes.title)
            put( TITLE_CONTENT,notes.content)
        }
        val result = db.update(TABLE_NAME,contentValues ,"$ID = ?" , arrayOf(notes.id.toString()))
        db.close()
        return result
    }
    fun getNotesById(id : Int) : NoteModel{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $ID = $id", null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_NAME))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(TITLE_CONTENT))

        cursor.close()
        db.close()
        return NoteModel(id,title,content)
    }
    fun deleteNote(id : Int) : Int{
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME , "$ID = ?" , arrayOf(id.toString()))
        db.close()
        return result
    }
}