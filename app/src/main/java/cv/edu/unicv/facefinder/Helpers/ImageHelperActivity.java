package cv.edu.unicv.facefinder.Helpers;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cv.edu.unicv.facefinder.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageHelperActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView inputImageView;
    private TextView tvResultado;
    public final static int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 1064;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_helper);

        inputImageView = findViewById(R.id.imageView2);
        tvResultado = findViewById(R.id.tvResultado);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
    }
    public void  onSelecionaImagem(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    public void onCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                inputImageView.setImageBitmap(imageBitmap);
                classificador(imageBitmap);
            }

        if(requestCode == PICK_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {;
                Bitmap bitmap = loadFromUri(data.getData());
                getInputImageView().setImageBitmap(bitmap);
                classificador(bitmap);
            }
            else{
                Toast.makeText(this,"Imagem nao Selecionada",Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap loadFromUri(Uri uri){
        Bitmap bitmap = null;
        try{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }else{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    protected Bitmap drawDetectionResult(List<Box> boxes, Bitmap bitmap){
        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(outputBitmap);
        Paint pen = new Paint();
        pen.setTextAlign(Paint.Align.LEFT);

        for (Box box : boxes) {
            pen.setColor(Color.RED);
            pen.setStrokeWidth(2F);
            pen.setStyle(Paint.Style.STROKE);
            canvas.drawRect(box.rect, pen);
        }
        return outputBitmap;
    }

    public ImageView getInputImageView() {
        return inputImageView;
    }
    public TextView getTvResultado(){return tvResultado;}

    protected void classificador(Bitmap bitmap){}
}