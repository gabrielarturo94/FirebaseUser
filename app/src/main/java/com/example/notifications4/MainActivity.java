package com.example.notifications4;

/* For the Notifications we need:

    1) Notification Channel
    2) Notification Builder
    3) Notification Manager

*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    /*
    //This is the channel
    private static final String Channel_ID = "Notif4";
    private static final String Channel_Name = "Notifications 4 App";
    private static final String Channel_Description = "Receive Test Notifications in Android App";
    //channel
    */

    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(Channel_ID,Channel_Name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(Channel_Description);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }*/

        /*findViewById(R.id.notificationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNotifications();
            }
        });*/


        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.INVISIBLE);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createUser();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {

           startProfileActivity();

        }
    }


    //  Method that Displays the Notification building it with a Title and a Message

    /*private void displayNotifications(){
        //this is the builder

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,Channel_ID)
                .setSmallIcon(R.drawable.ic_gabrielsandroid)
                .setContentTitle("Notifications 4")
                .setContentText("This is my new Notification")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
        mNotificationMgr.notify(1,mBuilder.build());

    }*/

    private void createUser(){

        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){

            editTextEmail.setError("Email Required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){

            editTextPassword.setError("Password Required");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6){

            editTextPassword.setError("Password should be at least 6 chars long");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        // mAuth is an object of the class FirebaseAuth which is a Template to deal with Authorization matters
        // Authorization has its  own section in Firebase where users will be stored

        mAuth.createUserWithEmailAndPassword(email,password)  //User is created in backend of Firebase Auth
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            startProfileActivity();
                        }else {

                            // If the user already exists (UserCollision)
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                //the sign up button will just log in
                                userLogin(email,password);

                            }else{

                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

    }

    private void userLogin(String email, String password){

        mAuth.signInWithEmailAndPassword(email,password)  //Sign in Method enabled from Firebase console (predetermined methods from FirebaseAuth Object)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            // Task Achieved: Create User in Firebase Auth
                            // Not yet in Firebase RealTime Database
                            // Profile Activity will create the User into the RealTime Database

                            startProfileActivity();

                        }else {

                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }

    private void startProfileActivity(){

        Intent intent = new Intent(this,ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
