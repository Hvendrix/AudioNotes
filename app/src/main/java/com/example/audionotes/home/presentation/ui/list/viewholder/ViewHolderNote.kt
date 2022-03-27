package com.example.audionotes.home.presentation.ui.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.utils.DateTimeUtils
import com.example.audionotes.databinding.ItemNoteBinding

class ViewHolderNote(
    private val binding: ItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(audioNote: AudioNote, onItemClickListener: OnItemClickListener){
        binding.textViewNameNote.text = audioNote.name
//        binding.textViewDateNote.text = audioNote.date.toString()
        binding.textViewDateNote.text = DateTimeUtils.getTimeStamp(audioNote.startDateTime)
        if(audioNote.endDateTime!=0) {
            val duration = audioNote.endDateTime - audioNote.startDateTime
            binding.textViewTimeInterval.text = DateTimeUtils.getDuration(duration)
        } else {
            binding.textViewTimeInterval.text = "0"
        }
        binding.buttonPlayNote.setOnClickListener {

            onItemClickListener.onItemClick(audioNote)
        }


    }
}

interface OnItemClickListener {
    fun onItemClick(item: AudioNote)
}