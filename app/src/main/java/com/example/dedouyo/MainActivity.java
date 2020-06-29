package com.example.dedouyo;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

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

        // setTitle
        TextView contentTitle = (TextView) findViewById(R.id.ch_contentTitle);
        contentTitle.setText("サッカー");

        // setSubTitle
        TextView contentSubTitle = (TextView) findViewById(R.id.ch_contentSubTitle);
        contentSubTitle.setText("球技");

        // setIntroduceText
        TextView aboutContent = (TextView) findViewById(R.id.ch_aboutContent);
        aboutContent.setText("丸い球体を用いて1チームが11人の計2チームの間で行われるスポーツ競技の一つである。");

        // setVideoSource (from Internet)
        videoView = (VideoView) findViewById(R.id.ch_introVideo);
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

        videoControlImageButton = findViewById(R.id.ch_playButton);
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
