package cv.edu.unicv.facefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.FaceDetector;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cv.edu.unicv.facefinder.Helpers.ImageHelperActivity;
import cv.edu.unicv.facefinder.funcs.FaceDetectionActivity;
import cv.edu.unicv.facefinder.funcs.ImageClassifierActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onGoToImageClassification(View view){
        Intent intent = new Intent(this, ImageClassifierActivity.class);
        startActivity(intent);
    }

    public void onGoToFaceDetector(View view){
        Intent intent = new Intent(this, FaceDetectionActivity.class);
        startActivity(intent);
    }

}