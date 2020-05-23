package com.lenovo.example.appointo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerifyEmailActivity extends AppCompatActivity {

    EditText userEmailText,userPasswordText;
    Button signInButton;
    private FirebaseAuth mAuth;
    String userName,userEmail,userPassword;
    String userEmailPassed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        userName = intent.getStringExtra("user_name");
        userEmailPassed = intent.getStringExtra("user_email_passed");

        userEmailText = findViewById(R.id.user_email);
        userPasswordText = findViewById(R.id.user_password);
        signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = userEmailText.getText().toString().trim();
                userPassword = userPasswordText.getText().toString().trim();

                //updateUI(user);
                if(userEmail.equals(userEmailPassed)){
                    VerifyEmail();
                }
                else {
                    SignIn();
                }
            }
        });
    }

    void  VerifyEmail(){
        mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    if(user.isEmailVerified()){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        final String userType;
                        if(userEmail.equals("gagang033@gmail.com"))
                            userType = "Admin";
                        else
                            userType = "User";

                        String uid = user.getUid();

                        User user1 = new User(userName,userEmail,userPassword,userType);
                        databaseReference.child("users").child(uid).setValue(user1)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (userType.equals("Admin"))
                                            startActivity(new Intent(VerifyEmailActivity.this,AdminHome.class));
                                        else
                                            startActivity(new Intent(VerifyEmailActivity.this,UserHome.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(VerifyEmailActivity.this,"Failed to write user",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else{
                        Toast.makeText(VerifyEmailActivity.this,"Your email is not verified",Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        user.delete();
                        finish();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(VerifyEmailActivity.this, "Email not registered",
                            Toast.LENGTH_SHORT).show();
                }

                else if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(VerifyEmailActivity.this, "Wrong Password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void SignIn(){
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            //updateUI(user);
                            if(currentUser!=null && currentUser.isEmailVerified()){
                                String uid = currentUser.getUid();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        User user = dataSnapshot.getValue(User.class);
                                        String userType = user.getType();
                                        if (userType.equals("Admin"))
                                            startActivity(new Intent(VerifyEmailActivity.this,AdminHome.class));
                                        else
                                            startActivity(new Intent(VerifyEmailActivity.this,UserHome.class));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else {
//                                Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_SHORT).show();
//                                finish();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());

                            //updateUI(null);
                            // ...
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(VerifyEmailActivity.this, "Email not registered",
                            Toast.LENGTH_SHORT).show();
                }

                else if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(VerifyEmailActivity.this, "Wrong Password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
