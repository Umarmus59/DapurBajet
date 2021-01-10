package com.allcodingtutorials.autheticate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class profile extends AppCompatActivity {
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    TextView textView;
    CollectionReference col=db.collection("student");
    DocumentReference doc=db.document("Student/cjj");
    Button button, btnLogout;
    FirebaseUser fbuser;
    FirebaseAuth fbauth;
    GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        button = findViewById(R.id.dialog_btn);
        btnLogout=findViewById(R.id.btnLogout);
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
                                    sb.append(document.getId()+" :) :\n\n"+"Ingredients:"+document.getString("Ingredients")+"\n\nSteps:\n"+document.getString("Steps")+"\n\n\n\n");
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                                builder.setCancelable(true);
                                builder.setTitle("Recipe");
                                builder.setMessage(sb.toString());
                                builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User clicked OK button
                                        Toast.makeText(profile.this, "Goodluck Chef!", Toast.LENGTH_SHORT).show();

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
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            fbauth.signOut();
                            Toast.makeText(profile.this, "Logged Out", Toast.LENGTH_SHORT);
                            finish();
                        }
                    }
                });
            }
        });

    }





    private void saveData() {
        Map<String,Object> student;
        student=new HashMap<>();
        student.put("Recipe Name","French Toast");
        student.put("Ingredients"," telur, esen vanila, susu segar, roti, mentega");
        student.put("Steps","1. Gaulkan 1 biji telur, 1 cawan susu, 1 susu gula, secubit garam, sedikit esen vanila, 1 cawan tepung gandum, dan 1 sudu teh baking powder sehingga sebati. Ratakan adunan atas periuk sehingga masak.\n" +
                "2. Taburkan gula pasir, kacang hancur, sedikit jagung manis tin dan marjerin. Tunggu sehingga gula mencair, kemudian apam balik sedia diangkat dan dihidang.");
        db.collection("student").document("French Toast").set(student);
    }
}
