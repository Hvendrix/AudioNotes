package com.example.audionotes.home.presentation.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.audionotes.R
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.databinding.FragmentHomeBinding
import com.example.audionotes.home.presentation.Controllers.PlayController
import com.example.audionotes.home.presentation.Controllers.RecordController
import com.example.audionotes.home.presentation.ui.list.adapter.AdapterNotes
import com.example.audionotes.home.presentation.ui.list.viewholder.OnItemClickListener
import com.example.audionotes.home.presentation.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.min


@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var recordController: RecordController
    lateinit var playController: PlayController
    private var countDownTimer: CountDownTimer? = null
    private var currentId: Long = 0

    private val homeViewModel: HomeViewModel by viewModels()

    private val adapter = AdapterNotes(this)

    var previous: AudioNote? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater)
        recordController = RecordController()
        playController = PlayController()


        binding.recyclerViewNotes.adapter = adapter


        binding.startButton.apply {
            setOnClickListener {
                onButtonClicked()
            }

        }



//        lifecycleScope.launch {
//            async { homeViewModel.getAudioNotes() }
//        }

        lifecycleScope.launchWhenResumed {
            async { homeViewModel.getAudioNotes() }
        }

        lifecycleScope.launchWhenResumed {
            homeViewModel.notesList.collect {
                var playedList = mutableListOf<Boolean>()
                var elapsedTime = mutableListOf<Long>()
                for(i in 0..it.size){
                    playedList.add(false)
                    elapsedTime.add(0L)
                }
                adapter.updateData(it, playedList, elapsedTime)
            }
        }



        return binding.root
    }

    override fun onStop() {
        super.onStop()
        if (recordController.isAudioRecording()) {
            stopRecord()
            binding.startButton.setImageResource(R.drawable.ic_mic)
        }

        if(playController.stillPlaying()) {
            playController.stopPlayNote()
            adapter.updatePlaying(previous, false)
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()

        countDownTimer = null
    }

    private fun onButtonClicked() {
        if (recordController.isAudioRecording()) {
            stopRecord()
            binding.startButton.setImageResource(R.drawable.ic_mic)

        } else {
            binding.startButton.setImageResource(R.drawable.oval)
            playController.stopPlayNote()
            previous=null
            val startTime = System.currentTimeMillis()
            val fileName = "${startTime}.wav"
            recordController.start(fileName)
            lifecycleScope.launch {
                var id = async {
                    homeViewModel.saveAudioNote(
                        AudioNote(
                            "Новая заметка",
                            startTime,
                            0,
                            fileName
                        )
                    )
                }
                currentId = id.await()

            }
            countDownTimer = object : CountDownTimer(60_000, VOLUME_UPDATE_DURATION) {
                override fun onTick(p0: Long) {
                    val volume = recordController.getVolume()
                    Timber.v("Volume = $volume")
                    handleVolume(volume)
                }

                override fun onFinish() {
                }
            }.apply {
                start()
            }
        }
    }

    private fun stopRecord() {
        recordController.stop()
        countDownTimer?.cancel()
        countDownTimer = null
        lifecycleScope.launch {
            async {
                homeViewModel.updateDuration(currentId, (System.currentTimeMillis()))
            }
        }
    }

    private fun handleVolume(volume: Int) {
        val scale = min(8.0, volume / MAX_RECORD_AMPLITUDE + 1.0).toFloat()
        Timber.v("Scale = $scale")

        binding.startButton
            .animate()
            .scaleX(scale)
            .scaleY(scale)
            .setInterpolator(interpolator)
            .duration = VOLUME_UPDATE_DURATION
    }


    override fun onItemClick(item: AudioNote) {
        if (recordController.isAudioRecording()) {
            stopRecord()
            binding.startButton.setImageResource(R.drawable.ic_mic)

        }

        if(playController.stillPlaying()){
            if(previous!=null && previous!=item){
                playController.stopPlayNote()
                adapter.updatePlaying(previous, false)
                playController.playNote(item, adapter)
                adapter.updatePlaying(item, true)
                previous = item
            } else {
                playController.stopPlayNote()
                adapter.updatePlaying(item, false)
                previous=null
            }
        }else{
            adapter.updatePlaying(item, true)
            playController.playNote(item, adapter)
            previous = item
        }
    }

    override fun onItemNameClick(item: AudioNote) {
        if (recordController.isAudioRecording()) {
            stopRecord()
            binding.startButton.setImageResource(R.drawable.ic_mic)
        }

        if(playController.stillPlaying()) {
            playController.stopPlayNote()
            adapter.updatePlaying(previous, false)
        }
        val taskEditText = EditText(this.requireContext())
        taskEditText.setText(item.name)
        val dialog: AlertDialog = AlertDialog.Builder(this.requireContext())
            .setTitle("Название заметки")
            .setMessage("Сменить название заметки?")
            .setView(taskEditText)
            .setPositiveButton("Сменить", DialogInterface.OnClickListener { dialog, which ->
                val taskText = taskEditText.text.toString()
                homeViewModel.updateName(item.id, taskText)
            })
            .setNegativeButton("Отмена", null)
            .create()
        dialog.show()
    }


    private companion object {
        private const val MAX_RECORD_AMPLITUDE = 32768.0
        private const val VOLUME_UPDATE_DURATION = 100L
        private val interpolator = OvershootInterpolator()
    }


}