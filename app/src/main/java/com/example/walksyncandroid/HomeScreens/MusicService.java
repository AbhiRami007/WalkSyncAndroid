package com.example.walksyncandroid.HomeScreens;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.walksyncandroid.R;

public class MusicService extends Service {

    private static final String CHANNEL_ID = "MUSIC_CHANNEL";
    private ExoPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MusicService", "Service created");
        createNotificationChannel();

        // Initialize ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        MediaItem mediaItem = MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"); // Replace with your music URL
        player.setMediaItem(mediaItem);

        // Add Listener to log errors and track playback state changes
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Log.e("MusicService", "ExoPlayer Error: " + error.getMessage());
            }

            @Override
            public void onPlaybackStateChanged(int playbackState) {
                if (playbackState == Player.STATE_READY && player.isPlaying()) {
                    generateNotification("Playing Music", "Your background music is playing.");
                    Log.i("MusicService", "Playback started");
                } else if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED) {
                    generateNotification("Music Paused", "Your background music is paused.");
                    Log.i("MusicService", "Playback paused");
                }
            }
        });

        // Prepare the Player
        player.prepare();
        Log.i("MusicService", "Player prepared");

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence app_name = getString(R.string.app_name);
            int notification_importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), app_name, notification_importance);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MusicService Start", "Service started");

        // Start playback if player is available
        if (player != null) {
            player.play();
            // Show notification and start the service as a foreground service
            generateNotification("Playing Music", "Your background music is playing.");
            startForeground(1, generateNotification("Playing Music", "Your background music is playing."));
        } else {
            Log.e("MusicService", "Player is null, cannot start playback");
        }

        return START_STICKY;
    }

    private Notification generateNotification(String title, String message) {
        // Create an Intent to redirect to the main activity on notification click
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // Automatically remove the notification when clicked

        return builder.build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MusicService", "Service destroyed");

        // Stop and release the player when service is destroyed
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }

        // Cancel the notification when the service is destroyed
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
