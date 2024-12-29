package com.example.noteswithvoice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val notes: List<Note>): RecyclerView.Adapter<Adapter.NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter.NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return NotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: Adapter.NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.tvNoteText.text = note.text
        holder.tvNoteDate.text = note.date
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNoteText: TextView = itemView.findViewById(R.id.tvNoteText)
        val tvNoteDate: TextView = itemView.findViewById(R.id.tvNoteDate)
    }
}
