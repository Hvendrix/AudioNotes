package com.example.audionotes.home.presentation.ui.fragment

import android.annotation.SuppressLint
import android.content.ContentUris
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.audionotes.R
import com.example.audionotes.core.data.model.NoteEntity
import com.example.audionotes.databinding.FragmentHomeBinding
import com.example.audionotes.home.presentation.ui.Controller.RecordController
import com.example.audionotes.home.presentation.ui.list.adapter.AdapterNotes
import com.example.audionotes.home.presentation.ui.list.viewholder.OnItemClickListener
import com.example.audionotes.home.presentation.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.io.File
import kotlin.math.min


@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var recordController: RecordController
    private var countDownTimer: CountDownTimer? = null

    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater)
        val adapter = AdapterNotes(this)
        recordController = RecordController(this.requireContext())

        this.activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                777,
            )
        }
//        adapter.updateData(listOf(NoteEntity(1,"2", 3, 4)))

        binding.recyclerViewNotes.adapter = adapter

        binding.startButton.apply {
            setOnClickListener {
                Timber.v("clicked")
                onButtonClicked()
            }

        }

        binding.btn.setOnClickListener {
            Timber.v("test")
//            var mediaPlayer = MediaPlayer.create(context, R.raw.sound_file_1)
//            mediaPlayer.start()
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                val id: Long = 1
                val contentUri: Uri =
                    ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id )
//            val myUri: Uri = Uri("${context.cacheDir.absolutePath}${File.pathSeparator}.wav")
                setDataSource(this@HomeFragment.requireContext(), contentUri)
                prepare()
                start()
            }
            mediaPlayer.start()
        }



        lifecycleScope.launchWhenResumed {
            homeViewModel.notesList.collect {
                adapter.updateData(it)
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }

//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//            HomeFragment().apply {
//            }
//    }


    private fun onButtonClicked() {
        Timber.v("clicked")
        if (recordController.isAudioRecording()) {
            recordController.stop()
            countDownTimer?.cancel()
            countDownTimer = null
        } else {
            recordController.start()
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

    private companion object {
        private const val MAX_RECORD_AMPLITUDE = 32768.0
        private const val VOLUME_UPDATE_DURATION = 100L
        private val interpolator = OvershootInterpolator()
    }


    override fun onItemClick(item: String) {
        TODO("Not yet implemented")
    }
}