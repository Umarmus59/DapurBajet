package com.allcodingtutorials.autheticate;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class map extends AppCompatActivity {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    TextView textView;
    CollectionReference col=db.collection("student");
    DocumentReference doc=db.document("Student/cjj");
    ImageView selectedImage;
    Button cameraBtn,Share;
    EditText text;
    int trigger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        trigger = 0;
        text= findViewById(R.id.EditText);
        selectedImage = findViewById(R.id.displayImageView);
        cameraBtn = findViewById(R.id.cameraBtn);
        Share = findViewById(R.id.Share);
        AlertDialog.Builder builder = new AlertDialog.Builder(map.this);
        builder.setCancelable(true);
        builder.setTitle("Instruction");
        builder.setMessage("Snap your food and share it!");
        builder.setPositiveButton(R.string.got, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Toast.makeText(map.this, "Goodluck Chef!", Toast.LENGTH_SHORT).show();

            }
        });
        builder.show();
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                askCameraPermissions();
         }
        });

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trigger < 1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(map.this);
                    builder.setCancelable(true);
                    builder.setTitle("Sorry :(");
                    builder.setMessage("You did not snap any pic");
                    builder.setPositiveButton(R.string.snap, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askCameraPermissions();
                        }
                    });
                    builder.show();
                }else {
                    image();                }

            }
        });


    }
    private void image(){
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        BitmapDrawable drawable=(BitmapDrawable)selectedImage.getDrawable();
        Bitmap bitmap=drawable.getBitmap();
        File f = new File(getExternalCacheDir()+"/"+getResources().getString(R.string.app_name)+".png");
        Intent shareint;
        try {
            FileOutputStream outputStream=new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);

            outputStream.flush();
            outputStream.close();
            shareint=new Intent(Intent.ACTION_SEND);
            shareint.setType("Image/*");
            shareint.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            shareint.putExtra(Intent.EXTRA_TEXT, text.getText().toString());
            shareint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        }catch (Exception e){
            throw  new RuntimeException();
        }
        startActivity(Intent.createChooser(shareint,"share image"));
    }


    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }else {
            openCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(map.this);
                builder.setCancelable(true);
                builder.setTitle("Sorry :(");
                builder.setMessage("We need permission to use the Camera");
                builder.show();            }
        }
    }

    private void openCamera(){
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(image);
            trigger=+1;
        }




    }



}
