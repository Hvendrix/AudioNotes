package com.example.audionotes.home.presentation.ui.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.databinding.ItemNoteBinding
import com.example.audionotes.home.presentation.ui.list.viewholder.OnItemClickListener
import com.example.audionotes.home.presentation.ui.list.viewholder.ViewHolderNote

class AdapterNotes(
    onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolderNote>() {

    var data = mutableListOf<AudioNote>()
    var dataPlayed = mutableListOf<Boolean>()
    var elapsedTime = mutableListOf<Long>()

    private var onItemClickListener: OnItemClickListener = onItemClickListener


    fun updateData(list: MutableList<AudioNote>, listPlayed: MutableList<Boolean>, listTime: MutableList<Long>) {
        if(!list.isNullOrEmpty()) {
            data = list
            dataPlayed = listPlayed
            elapsedTime = listTime
            notifyDataSetChanged()

        }
    }

    fun updatePlaying(audioNote: AudioNote?, playing: Boolean){
        if(audioNote!=null) {
            var index = data.indexOf(audioNote)
            dataPlayed[index] = playing
            elapsedTime[index] = 0L

            notifyItemChanged(index)
        }
    }


    fun updatePlaying(audioNote: AudioNote?, playing: Boolean, milliseconds: Long){
        if(audioNote!=null) {
            var index = data.indexOf(audioNote)
            dataPlayed[index] = playing
            elapsedTime[index] = milliseconds
            notifyItemChanged(index)
        }
    }
    fun addToData(audioNote : AudioNote){
            data.add(audioNote)
            notifyItemInserted(data.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNote {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolderNote(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderNote, position: Int) {
        holder.bind(data[position],dataPlayed[position], elapsedTime[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }



}
