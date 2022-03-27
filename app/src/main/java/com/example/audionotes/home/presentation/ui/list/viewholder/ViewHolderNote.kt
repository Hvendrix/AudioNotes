package com.example.audionotes.home.presentation.ui.list.viewholder

import android.opengl.Visibility
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.audionotes.R
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.utils.DateTimeUtils
import com.example.audionotes.databinding.ItemNoteBinding
import java.util.*

class ViewHolderNote(
    private val binding: ItemNoteBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        audioNote: AudioNote,
        playing: Boolean,
        elapsedTime: Long,
        onItemClickListener: OnItemClickListener
    ) {
        binding.textViewNameNote.text = audioNote.name
        binding.textViewDateNote.text = DateTimeUtils.getTimeStamp(audioNote.startDateTime)
        if (audioNote.endDateTime != 0L) {
            if (elapsedTime != 0L) {
                binding.textViewTimeInterval.text = DateTimeUtils.getElapsedTime(elapsedTime) +
                        DateTimeUtils.getDuration(
                            Date(audioNote.endDateTime),
                            Date(audioNote.startDateTime)
                        )
            } else
                binding.textViewTimeInterval.text = DateTimeUtils.getDuration(
                    Date(audioNote.endDateTime),
                    Date(audioNote.startDateTime)
                )

            binding.buttonPlayNote.visibility = View.VISIBLE
            if (playing) {
                binding.buttonPlayNote.setImageResource(R.drawable.ic_stop)
            } else {
                binding.buttonPlayNote.setImageResource(R.drawable.ic_play)
            }
        } else {
            binding.textViewTimeInterval.text = "Идет запись"
            binding.buttonPlayNote.visibility = View.GONE
        }

        binding.buttonPlayNote.setOnClickListener {
            onItemClickListener.onItemClick(audioNote)
        }


    }
}

interface OnItemClickListener {
    fun onItemClick(item: AudioNote)
}