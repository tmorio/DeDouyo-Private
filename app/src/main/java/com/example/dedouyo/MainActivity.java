package com.example.dedouyo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.VideoView;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private VideoView videoView;
    private ImageButton videoControlImageButton;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("DeDouyo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        // getLocalDownloadDirectory
        File pathExternalPublicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String dir = ((File) pathExternalPublicDir).getPath();

        // setVideoSource (from Local)
        final VideoView videoView = (VideoView) findViewById(R.id.introVideo);
        videoView.setVideoPath(dir + "/video.mp4");
        videoView.start();

         */

        // setVideoSource (from Internet)
        videoView = (VideoView) findViewById(R.id.introVideo);
        videoView.setVideoURI(Uri.parse("https://moritoworks.com/video.mp4"));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                showVideoThumbnail(mp);
                changePlayButton();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                showVideoThumbnail(mp);
                changePlayButton();
            }
        });

        videoControlImageButton = findViewById(R.id.button);
        videoControlImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    changePlayButton();
                } else {
                    videoView.start();
                    changePauseButton();
                }
            }
        });

    }

    private void showVideoThumbnail(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
    }


    private void changePlayButton() {
        videoControlImageButton.setImageResource(android.R.drawable.ic_media_play);
    }


    private void changePauseButton() {
        videoControlImageButton.setImageResource(android.R.drawable.ic_media_pause);
    }
}
