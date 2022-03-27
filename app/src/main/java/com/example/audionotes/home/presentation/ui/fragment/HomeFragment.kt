package com.example.audionotes.home.presentation.ui.fragment

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.audionotes.core.data.model.AudioNote
import com.example.audionotes.databinding.FragmentHomeBinding
import com.example.audionotes.home.presentation.ui.Controller.PlayController
import com.example.audionotes.home.presentation.ui.Controller.RecordController
import com.example.audionotes.home.presentation.ui.list.adapter.AdapterNotes
import com.example.audionotes.home.presentation.ui.list.viewholder.OnItemClickListener
import com.example.audionotes.home.presentation.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.min
import java.io.*


@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var recordController: RecordController
    lateinit var playController: PlayController
    private var countDownTimer: CountDownTimer? = null

    private val homeViewModel: HomeViewModel by viewModels()



    private val filepath = "MyFileStorage"
    private var myExternalFile: File?=null
    private val isExternalStorageReadOnly: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }
    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(extStorageState)
    }


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
//        adapter.updateData(listOf(NoteEntity(1,"2", 3, 4)))

        binding.recyclerViewNotes.adapter = adapter

        var fileData = "date"

        binding.startButton.apply {
            setOnClickListener {
                Timber.v("clicked")
                onButtonClicked()
//                myExternalFile = File(this@HomeFragment.requireContext().getExternalFilesDir(filepath), "name123")
//                try {
//                    val fileOutPutStream = FileOutputStream(myExternalFile)
//                    fileOutPutStream.write(fileData.toString().toByteArray())
//                    fileOutPutStream.close()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//                Toast.makeText(this@HomeFragment.requireContext(),"data save",Toast.LENGTH_SHORT).show()
//                Timber.v("test")

//                lifecycleScope.launch {
//                    val notes = async { homeViewModel.getAudioNotes() }
////                    Timber.v("t5 " + notes.await().size)
////                    homeViewModel.consumeData(notes.await())
//                }



        }

        }


        binding.btn.setOnClickListener {


            lifecycleScope.launch{

                val notes = async { homeViewModel.getAudioNotes() }
//                 val post = async { homeViewModel.saveAudioNote(AudioNote("5", 44, 4, "4")) }
            }
//            val filename = "name123"
//            val mediaPlayer = MediaPlayer().apply {
//                setAudioAttributes(
//                    AudioAttributes.Builder()
//                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                        .setUsage(AudioAttributes.USAGE_MEDIA)
//                        .build()
//                )
//                myExternalFile = File(this@HomeFragment.requireContext().getExternalFilesDir(filepath),filename)
//                setDataSource(myExternalFile!!.absolutePath)
//                prepare()
//                start()
//            }
//            mediaPlayer.start()
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

            val startTime = System.currentTimeMillis()
            val fileName = "${startTime}.wav"
//            val fileName = "${File.pathSeparator}${startTime}.wav"
            recordController.start(fileName)
            lifecycleScope.launch {
                async { homeViewModel.saveAudioNote(AudioNote("Новая заметка", startTime, 0, fileName)) }
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