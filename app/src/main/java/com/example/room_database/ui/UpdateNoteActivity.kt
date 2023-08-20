package com.example.room_database.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.room_database.databinding.ActivityUpdateNoteBinding
import com.example.room_database.db.NoteDatabase
import com.example.room_database.db.NoteEntity
import com.example.room_database.utils.Constants.BUNDLE_NOTE_ID
import com.example.room_database.utils.Constants.NOTE_DATABASE
import com.google.android.material.snackbar.Snackbar

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private val noteDB: NoteDatabase by lazy {
        Room.databaseBuilder(this, NoteDatabase::class.java, NOTE_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private lateinit var noteEntity: NoteEntity
    private var noteId = 0
    private var defaultTitle = ""
    private var defaultDesc = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            noteId = it.getInt(BUNDLE_NOTE_ID)
        }

        binding.apply {
            defaultTitle=noteDB.doa().getNote(noteId).noteTitle
            defaultDesc=noteDB.doa().getNote(noteId).noteDesc

            edtTitle.setText(defaultTitle)
            edtDesc.setText(defaultDesc)

            btnDelete.setOnClickListener {
                noteEntity= NoteEntity(noteId,defaultTitle,defaultDesc)
                noteDB.doa().deleteNote(noteEntity)
                finish()
            }

            btnSave.setOnClickListener {
                val title = edtTitle.text.toString()
                val desc=edtDesc.text.toString()
                if (title.isNotEmpty() || desc.isNotEmpty()){
                    noteEntity= NoteEntity(noteId,title,desc)
                    noteDB.doa().updateNote(noteEntity)
                    finish()
                }
                else{
                    Snackbar.make(it,"Title and Description cannot be Empty", Snackbar.LENGTH_LONG).show()
                }
            }
        }

    }
}