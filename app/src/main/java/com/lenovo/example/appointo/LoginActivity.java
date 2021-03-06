package com.lenovo.example.appointo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class LoginActivity extends AppCompatActivity {

    EditText userEmailText,userPasswordText;
    TextView forgotPasswordText;
    Button signInButton;
    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pd = new ProgressDialog(this);
        pd.setTitle("Logging in...");
        pd.setCancelable(false);
        pd.show();

        mAuth = FirebaseAuth.getInstance();

        userEmailText = findViewById(R.id.user_email);
        userPasswordText = findViewById(R.id.user_password);
        signInButton = findViewById(R.id.sign_in_button);
        forgotPasswordText = findViewById(R.id.forgot_password_text);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = userEmailText.getText().toString().trim();
                String userPassword = userPasswordText.getText().toString().trim();
                SignIn(userEmail,userPassword);
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = userEmailText.getText().toString().trim();
                if(userEmail.isEmpty()){
                    userEmailText.setError("Email cannot be empty");
                    return;
                }
                mAuth.sendPasswordResetEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginActivity.this,"Password Reset email sent",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(LoginActivity.this,"Email not found",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    void SignIn(String userEmail,String userPassword){
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
                                            startActivity(new Intent(LoginActivity.this,AdminHome.class));
                                        else
                                            startActivity(new Intent(LoginActivity.this,UserHome.class));
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

                        pd.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(LoginActivity.this, "Email not registered",
                            Toast.LENGTH_SHORT).show();
                }

                else if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(LoginActivity.this, "Wrong Password",
                            Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            }
        });
    }
}
