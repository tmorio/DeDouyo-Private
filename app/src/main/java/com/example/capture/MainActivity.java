package com.example.capture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import java.nio.ByteBuffer;
import android.widget.Toast;

import com.isseiaoki.simplecropview.CropImageView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_MEDIA_PROJECTION = 1001;
    private MediaProjectionManager mpManager;
    private MediaProjection mProjection;

    private int displayWidth, displayHeight;
    private ImageReader imageReader;
    private VirtualDisplay virtualDisplay;
    private int screenDensity;
    private ImageView imageView;

    // トリミング用Viewの定義
    private CropImageView cropImageView;
    private ImageView croppedImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        // Service用Intentの発行
        Intent intent = new Intent(getApplication(), TestService.class);
        intent.putExtra("REQUEST_CODE", 1);

        // Serviceの開始
        //startService(intent);
        startForegroundService(intent);

        cropImageView = (CropImageView)findViewById(R.id.cropImageView);
        croppedImageView = (ImageView)findViewById(R.id.croppedImageView);

        // Service停止ボタン設定
        Button buttonStop = findViewById(R.id.button_stop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), TestService.class);
                stopService(intent);
            }
        });

        // スクリーンショット用ボタンの設定
        ImageButton button = findViewById(R.id.analyzeButton);
        // ボタンタップでスクリーンショットを発行
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context , "要素の抽出を開始します", Toast.LENGTH_LONG).show();
                getScreenshot();
            }
        });

        Button cropButton = (Button)findViewById(R.id.crop_button);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // フレームに合わせてトリミング
                croppedImageView.setImageBitmap(cropImageView.getCroppedBitmap());
            }
        });

        // 撮影したスクリーンを表示するImageView
        imageView = findViewById(R.id.image_view);

        // 画面の縦横サイズとdpを取得
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenDensity = displayMetrics.densityDpi;
        displayWidth = displayMetrics.widthPixels;
        displayHeight = displayMetrics.heightPixels;

        mpManager = (MediaProjectionManager)
                getSystemService(MEDIA_PROJECTION_SERVICE);

        // permissionを確認するintentを投げ、ユーザーの許可・不許可を受け取る
        if(mpManager != null){
            startActivityForResult(mpManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        }
    }

    // ユーザーの許可を受け取る
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_MEDIA_PROJECTION == requestCode) {
            if (resultCode != RESULT_OK) {
                // 拒否された
                Toast.makeText(this,
                        "User cancelled", Toast.LENGTH_LONG).show();
                return;
            }
            // 許可された結果を受け取る
            setUpMediaProjection(resultCode, data);
        }
    }

    private void setUpMediaProjection(int code, Intent intent) {
        mProjection = mpManager.getMediaProjection(code, intent);
        setUpVirtualDisplay();
    }

    private void setUpVirtualDisplay() {
        imageReader = ImageReader.newInstance(
                displayWidth, displayHeight, PixelFormat.RGBA_8888, 2);

        virtualDisplay = mProjection.createVirtualDisplay("ScreenCapture",
                displayWidth, displayHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                imageReader.getSurface(), null, null);
    }

    private void getScreenshot() {
        // ImageReaderから画面を取り出す
        Log.d("debug", "getScreenshot");

        Image image = imageReader.acquireLatestImage();
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();

        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * displayWidth;

        // バッファからBitmapを生成
        Bitmap bitmap = Bitmap.createBitmap(
                displayWidth + rowPadding / pixelStride, displayHeight,
                Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();

        imageView.setImageBitmap(bitmap);

        // トリミング画像セット
        cropImageView.setImageBitmap(bitmap);
    }

}