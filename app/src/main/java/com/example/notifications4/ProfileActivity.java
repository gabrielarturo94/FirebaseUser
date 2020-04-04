package com.example.notifications4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

import static java.lang.Float.parseFloat;

public class ProfileActivity extends AppCompatActivity {

    public static final String NODE_USERS = "users";

    private FirebaseAuth mAuth;                      //Object that Handles the Authentication Methods predetermined by Firebase

    private EditText editTextTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance(); // Object of FirebaseAuth is created to use info about the
                                            // current user in memory, to be then written into DB

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (task.isSuccessful()){

                            String newToken = Objects.requireNonNull(task.getResult()).getToken();  // The Current User's Token is stored in a String
                            // Notice that Token was previously generated with getInstanceID

                            saveToken(newToken);    //THIS METHOD WILL WRITE INTO FIREBASE the user
                                                    //and its unique token associated

                        }/*else{

                            //Toast.makeText(ProfileActivity.this,"I never thought reaching here",Toast.LENGTH_LONG).show();
                        }*/

                    }
                });

        TextView textViewUser = findViewById(R.id.textViewUser);  // The TextView for Displaying the User is related with the java class

        //These Methods are just to Display on the screen a key that Identifies that the user is logged in in his profile
        // In this case I chose to display the email


        //textViewUser.setText(mAuth.getCurrentUser().getDisplayName()); // returns blank because we have not set anything as a "name"
        //textViewUser.setText(mAuth.getCurrentUser().getProviderId()); // returns "firebase"
        //textViewUser.setText(mAuth.getCurrentUser().toString()); // returns....
        //textViewUser.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()); // returns the Unique ID with which the User is stored in Firebase
        textViewUser.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()); // returns the email with which you signed in

        editTextTemperature = findViewById(R.id.editTextTemperature); // The Edit Text for the Temperature is related with the java class

    }


    @Override
    protected void onStart() {
        super.onStart();

        // if there is no current user logged in, go back to Main Activity to sign in
        if (mAuth.getCurrentUser() == null) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);       // I don't remember what this do
            startActivity(intent);

        }

        findViewById(R.id.buttonLogOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logOut();
            }
        });


        //This is the button where the user prompts the desired Temperature
        /*
        findViewById(R.id.setTemperatureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desiredTemp = editTextTemperature.getText().toString();
                if (desiredTemp.isEmpty()){

                    editTextTemperature.setError("Temperature Empty");
                    editTextTemperature.requestFocus();
                    return;

                }else {

                    float desiredTemperature = parseFloat(desiredTemp.trim());
                    addTemperatureToDB(desiredTemperature);
                }
            }
        });

        */


    }



    private void saveToken(String token){


        String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        //the mAuth gets the current user --in memory-- according to its determined sign in Method
        // In this case is by email

        User user = new User(email,token);  // The object User is initialized

        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);    //Will write into the specified node
                                                                                                // if the node did not exist, this method
                                                                                                // creates it

        dbUsers.child(mAuth.getCurrentUser().getUid())  //by this time we are inside the node "users", now we write into it a
                                                        // new object of type User that contains several properties
                                                        // the most important being the token
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(ProfileActivity.this, "Token Saved", Toast.LENGTH_LONG).show();
                }

            }
        });

    }// End of SaveToken method

    private void logOut(){

        mAuth.signOut();
        Intent signOutIntent = new Intent(this, MainActivity.class);
        startActivity(signOutIntent);
    }



    // This is the Method that Adds Temperature, IT IS NOT WORKING PROPERLY
    /*

    private void addTemperatureToDB(float Desired_Temperature){

        //User user = new User();
        //user.setDesiredTemp(Desired_Temperature);
        DatabaseReference dbUsers = FirebaseDatabase.getInstance().getReference(NODE_USERS);    //Will write into the specified node
        // if the node did not exist, this method
        // creates it


        dbUsers.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("desiredTemp").;

        /*

        dbUsers.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())  //by this time we are inside the node "users", now we write into it a
                // new object of type User that contains several properties
                // the most important being the token
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Toast.makeText(ProfileActivity.this, "Desired Temperature Saved", Toast.LENGTH_LONG).show();
                }

            }
        });


    }*/ //End of the Method AddTemperature


//End of ProfileActivity

}
