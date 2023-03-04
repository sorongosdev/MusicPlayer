package com.sorongos.musicplayer

import android.app.*
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.IBinder

class MediaPlayerService : Service() {


    private var mediaPlayer: MediaPlayer? = null

    /**포그라운드 서비스이기 때문에 bind를 사용하지 않음*/
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val playIcon = Icon.createWithResource(baseContext, R.drawable.baseline_play_arrow_24)
        val pauseIcon = Icon.createWithResource(baseContext, R.drawable.baseline_pause_24)
        val stopIcon = Icon.createWithResource(baseContext, R.drawable.baseline_stop_24)

        /**notification을 눌렀을 때 mainActivity로 이동함*/
        val mainPendingIntent = PendingIntent.getActivity(
            baseContext,
            0,
            Intent(baseContext, MainActivity::class.java)
                .apply {
                    //기존 액티비티가 존재할 경우, 재사용함
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                },
            PendingIntent.FLAG_IMMUTABLE
        )

        val pausePendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_PAUSE
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        val playPendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_PLAY
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        val stopPendingIntent = PendingIntent.getService(
            baseContext,
            0,
            Intent(baseContext, MediaPlayerService::class.java).apply {
                action = MEDIA_PLAYER_STOP
            },
            PendingIntent.FLAG_IMMUTABLE
        )

        /**상단바를 내렸을 때 보이는 notification*/
        val notification = Notification.Builder(baseContext, CHANNEL_ID)
            .setStyle(
                Notification.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setVisibility(Notification.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.baseline_star_24)
            .addAction(
                Notification.Action.Builder(
                    pauseIcon,
                    "Pause",
                    //pendingIntent
                    pausePendingIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    playIcon,
                    "Play",
                    //pendingIntent
                    playPendingIntent
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    stopIcon,
                    "Stop",
                    //pendingIntent
                    stopPendingIntent
                ).build()
            )
            //안에 pendingIntent가 들어가야함
            .setContentIntent(mainPendingIntent)
            .setContentTitle("음악재생")
            .setContentText("음원이 재생 중입니다.")
            .build()

        startForeground(100, notification)
    }

    private fun createNotificationChannel() {
        //포그라운드 서비스 실행을 위해 채널 아이디, 채널 이름을 만듦
        val channel =
            NotificationChannel(CHANNEL_ID, "MEDIA_PLAYER", NotificationManager.IMPORTANCE_HIGH)

        val notificationManager = baseContext.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    /**onCreate 실행되고 실행 START될 때마다 실행*/
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //각 액션마다 동작
        when (intent?.action) {
            MEDIA_PLAYER_PLAY -> {
                if (mediaPlayer == null) {
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