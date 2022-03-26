package com.example.audionotes.home.presentation.ui.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.audionotes.core.data.model.NoteEntity
import com.example.audionotes.databinding.ItemNoteBinding

class ViewHolderNote(
    private val binding: ItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(note: NoteEntity, onItemClickListener: OnItemClickListener){
        binding.textViewNameNote.text = note.name
        binding.textViewDateNote.text = note.date.toString()


    }
}

interface OnItemClickListener {
    fun onItemClick(item: String)
}