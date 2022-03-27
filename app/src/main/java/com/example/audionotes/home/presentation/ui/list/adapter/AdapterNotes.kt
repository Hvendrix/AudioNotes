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

    var data = listOf<AudioNote>()
    var dataPlayed = mutableListOf<Boolean>()

    private var onItemClickListener: OnItemClickListener = onItemClickListener


    fun updateData(list: List<AudioNote>, listPlayed: MutableList<Boolean>) {
        if(!list.isNullOrEmpty()) {
            data = list
            dataPlayed = listPlayed
            notifyDataSetChanged()

        }
    }

    fun updatePlaying(audioNote: AudioNote, playing: Boolean){
        var index = data.indexOf(audioNote)
        dataPlayed[index] = playing
        notifyItemChanged(index)
    }
    fun addToData(audioNote : AudioNote){

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
        holder.bind(data[position],dataPlayed[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return data.size
    }



}
