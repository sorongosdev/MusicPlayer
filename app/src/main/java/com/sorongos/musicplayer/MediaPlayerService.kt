package com.sorongos.musicplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MediaPlayerService : Service() {


    private var  mediaPlayer : MediaPlayer? = null
    /**포그라운드 서비스이기 때문에 bind를 사용하지 않음*/
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /**onCreate 실행되고 실행 START될 때마다 실행*/
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //각 액션마다 동작
        when (intent?.action) {
            MEDIA_PLAYER_PLAY -> {
                if(mediaPlayer == null){
                    mediaPlayer = MediaPlayer.create(baseContext, R.raw.walkaway)
                }
                mediaPlayer?.start()
            }
            MEDIA_PLAYER_PAUSE -> {
                mediaPlayer?.pause()
            }
            MEDIA_PLAYER_STOP -> {
                mediaPlayer?.stop()
                //명시적 종료
                mediaPlayer?.release()
                mediaPlayer = null
                stopSelf()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
}