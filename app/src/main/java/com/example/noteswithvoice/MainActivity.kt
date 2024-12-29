package com.example.noteswithvoice

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val notes = mutableListOf<Note>()
    private lateinit var adapter: Adapter
    private val REQUEST_CODE_VOICE_INPUT = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val fabAddNote: FloatingActionButton = findViewById(R.id.fabAddNote)
        val fabVoiceInput: FloatingActionButton = findViewById(R.id.fabVoiceInput)

        adapter = Adapter(notes)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        fabAddNote.setOnClickListener {
            openAddNoteDialog()
        }

        fabVoiceInput.setOnClickListener {
            startVoiceInput()
        }
    }
    private fun openAddNoteDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_note, null)
        val editTextNote = dialogView.findViewById<EditText>(R.id.etNoteText)

        AlertDialog.Builder(this)
            .setTitle("Add Note")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val noteText = editTextNote.text.toString()
                if (noteText.isNotBlank()) {
                    val note = Note(noteText, getCurrentDate())
                    notes.add(note)
                    adapter.notifyItemInserted(notes.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your note")
        }
        try {
            startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "Your device doesn't support speech input", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VOICE_INPUT && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.get(0) ?: ""
            if (spokenText.isNotBlank()) {
                val note = Note(spokenText, getCurrentDate())
                notes.add(note)
                adapter.notifyItemInserted(notes.size - 1)
                Toast.makeText(this, "Note added Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }




}