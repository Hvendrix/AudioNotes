package com.example.audionotes.home.presentation.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.core.utils.DateTimeUtils
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
    private var startTime: Long = 0
    private var currentId: Long = 0

    private val homeViewModel: HomeViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater)
        val adapter = AdapterNotes(this)
        recordController = RecordController()
        playController = PlayController()

        this.activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                777,
            )
        }

        binding.recyclerViewNotes.adapter = adapter


        binding.startButton.apply {
            setOnClickListener {
                Timber.v("clicked")
                onButtonClicked()
            }

        }


        binding.btn.setOnClickListener {

        }


        lifecycleScope.launch {
            async { homeViewModel.getAudioNotes() }
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
            lifecycleScope.launch{
                async {
                    Timber.v("t5 current id is $currentId")
                    homeViewModel.updateDuration(currentId, (System.currentTimeMillis() - startTime))
                    Timber.v("t5 ${DateTimeUtils.getDuration(System.currentTimeMillis() - startTime)}")

//                    Timber.v("t5 ${homeViewModel.getNote(currentId).endDateTime}" )
                }
            }
            Timber.v("t5 ${DateTimeUtils.getDuration(System.currentTimeMillis() - startTime)}")
        } else {
            startTime = System.currentTimeMillis()
            val fileName = "${startTime}.wav"
//            val fileName = "${File.pathSeparator}${startTime}.wav"
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


    override fun onItemClick(item: AudioNote) {
        playController.playNote(item.notePath)
    }


}