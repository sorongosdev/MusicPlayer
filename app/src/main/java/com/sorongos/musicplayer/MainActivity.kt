package com.sorongos.musicplayer

import android.Manifest
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.sorongos.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playButton.setOnClickListener { mediaPlayerPlay() }
        binding.pauseButton.setOnClickListener { mediaPlayerPause() }
        binding.stopButton.setOnClickListener { mediaPlayerStop() }
        requestPostNotification()
    }

    private fun mediaPlayerPlay() {
        val intent = Intent(this, MediaPlayerService::class.java)
            //string으로 action을 넣음
            .apply { action = MEDIA_PLAYER_PLAY }
        startService(intent)
    }

    private fun mediaPlayerStop() {
        val intent = Intent(this, MediaPlayerService::class.java)
            //string으로 action을 넣음
            .apply { action = MEDIA_PLAYER_STOP }
        startService(intent)
    }

    private fun mediaPlayerPause() {
        val intent = Intent(this, MediaPlayerService::class.java)
            //string으로 action을 넣음
            .apply { action = MEDIA_PLAYER_PAUSE }
        startService(intent)
    }
    private fun requestPostNotification() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            100
        )
    }

}