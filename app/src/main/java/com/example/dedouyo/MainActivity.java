package com.example.dedouyo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("DeDouyo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File pathExternalPublicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String dir = ((File) pathExternalPublicDir).getPath();

        final VideoView videoView = (VideoView) findViewById(R.id.introVideo);
        videoView.setVideoPath(dir + "/video.mp4");
        videoView.start();
    }

}