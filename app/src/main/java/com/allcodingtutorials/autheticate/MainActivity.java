package com.allcodingtutorials.autheticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    TextView textView;
    CollectionReference col=db.collection("student"), col1=db.collection("student");
    DocumentReference doc=db.document("Student/cjj"), doc1=db.document("Student/cjj");
    Button button, btnLogout, btnTemp, btnmap;
    FirebaseUser fbuser;
    FirebaseAuth fbauth;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.dialog_btn);
        btnTemp = findViewById(R.id.tempbutton);
        btnmap = findViewById(R.id.mapbtn);
        saveData();
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.collection("student").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                StringBuilder sb= new StringBuilder();
                                for(QueryDocumentSnapshot document:queryDocumentSnapshots){
                                    sb.append(document.getId()+" :) :\n\n"+"Ingredients:\n"+document.getString("Ingredients")+"\n\nSteps:\n"+document.getString("Steps")+"\n\n\n\n");
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                String item = sb.toString();
                                builder.setCancelable(true);
                                builder.setTitle("Recipe");
                                builder.setMessage(sb.toString());
                                builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        Toast.makeText(MainActivity.this, "Goodluck Chef!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                builder.setNegativeButton("share", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("text/plain");
                                        intent.putExtra(Intent.EXTRA_TEXT, item);
                                        startActivity(intent);


                                    }
                                });
                                builder.show();

                            }
                        });


            }
        });
        fbauth=FirebaseAuth.getInstance();
        fbuser=fbauth.getCurrentUser();

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity2Intent = new Intent(getApplicationContext(), map.class);
                startActivity(activity2Intent);
            }
        });
    btnTemp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent activity2Intent = new Intent(getApplicationContext(), tempe.class);
            startActivity(activity2Intent);
        }
    });
    }





    private void saveData() {
        Map<String,Object> student;
        student=new HashMap<>();
        student.put("Recipe Name","Murtabak Maggi");
        student.put("Ingredients","sebiji telur, satu bungkus maggi kari");
        student.put("Steps","1. Rebus maggi kari sehingga lembut. Sementara itu, gaulkan sebiji telur bersama perencah maggi. Kemudian masukkan maggi yang sudah lembut dan gaulkan sekali\n" +
                "2. Panaskan kuali, kemudian goreng sehingga kedua permukaan menjadi keperangan.");
        db.collection("student").document("Murtabak Maggi").set(student);
    }
}
