package com.example.audionotes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.audionotes.core.presentation.ui.viewmodel.MainViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /**
     * решил все-таки объявить viewModel в core/domain/interactors/
     */
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            777,
        )


        val authLauncher = VK.login(this) { result : VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> {
                    Toast.makeText(this, "Авторизация успешна", Toast.LENGTH_SHORT).show()
                }
                is VKAuthenticationResult.Failed -> {
                    Toast.makeText(this, "Авторизация не удалась", Toast.LENGTH_SHORT).show()
                }
            }
        }
        authLauncher.launch(arrayListOf(VKScope.WALL, VKScope.DOCS, VKScope.AUDIO))

    }


//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.deleteNotes -> {
//                mainViewModel.deleteNotes()
//                return true
//            }
//
//        }
//        return super.onOptionsItemSelected(item)
//    }
}