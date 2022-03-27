package com.example.audionotes.home.presentation.ui.list.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.audionotes.R
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.utils.DateTimeUtils
import com.example.audionotes.databinding.ItemNoteBinding
import java.util.*

class ViewHolderNote(
    private val binding: ItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(audioNote: AudioNote, playing: Boolean, onItemClickListener: OnItemClickListener){
        binding.textViewNameNote.text = audioNote.name
//        binding.textViewDateNote.text = audioNote.date.toString()
        binding.textViewDateNote.text = DateTimeUtils.getTimeStamp(audioNote.startDateTime)
        if(audioNote.endDateTime!=0L) {
            binding.textViewTimeInterval.text = DateTimeUtils.getDuration(Date(audioNote.endDateTime), Date(audioNote.startDateTime))
        } else {
            binding.textViewTimeInterval.text = "0"
        }
        if(playing){
            binding.buttonPlayNote.setImageResource(R.drawable.ic_stop)
        }else{
            binding.buttonPlayNote.setImageResource(R.drawable.ic_play)
        }
        binding.buttonPlayNote.setOnClickListener {
            onItemClickListener.onItemClick(audioNote)
        }


    }
}

interface OnItemClickListener {
    fun onItemClick(item: AudioNote)
}