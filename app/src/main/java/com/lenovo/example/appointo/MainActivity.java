package com.lenovo.example.appointo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText userNameText,userEmailText,userPasswordText,confirmPasswordText;
    TextView signInText;
    Button registerButton;
    private FirebaseAuth mAuth;
    String userName,userEmail,userPassword,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        userNameText = findViewById(R.id.user_name);
        userEmailText = findViewById(R.id.user_email);
        userPasswordText = findViewById(R.id.user_password);
        confirmPasswordText = findViewById(R.id.confirm_password);
        registerButton = findViewById(R.id.register_button);
        signInText = findViewById(R.id.sign_in_text);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = userNameText.getText().toString().trim();
                userEmail = userEmailText.getText().toString().trim();
                userPassword = userPasswordText.getText().toString().trim();
                confirmPassword = confirmPasswordText.getText().toString().trim();
                if(!userPassword.equals(confirmPassword)){
                    confirmPasswordText.setError("Passwords do not match");
                    return;
                }

                SignUp(userName,userEmail,userPassword);
            }
        });

        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            String uid = currentUser.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    String userType = user.getType();
                    if (userType.equals("Admin"))
                        startActivity(new Intent(MainActivity.this,AdminHome.class));
                    else
                        startActivity(new Intent(MainActivity.this,UserHome.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
//            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//            startActivity(intent);
        }
    }

    void SignUp(final String userName, final String userEmail, final String userPassword){

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //og.d(TAG, "createUserWithEmail:success");
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
                                                    startActivity(new Intent(MainActivity.this,AdminHome.class));
                                                else
                                                    startActivity(new Intent(MainActivity.this,UserHome.class));
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MainActivity.this,"Failed to write user",Toast.LENGTH_SHORT).show();
                                            }
                                        });
//                                Intent intent = new Intent(MainActivity.this,HomeActivity.class);
//                                startActivity(intent);
                            }
                            else{
                                sendVerificationEmail(user);

                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(MainActivity.this, "Email already registered",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void sendVerificationEmail(FirebaseUser user){
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button
                        ///findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    "Verification email sent",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,VerifyEmailActivity.class);
                            intent.putExtra("user_name",userName);
                            intent.putExtra("user_email_passed",userEmail);
                            startActivity(intent);
                        } else {
                            //Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(MainActivity.this,
                                    "Failed to send verification email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
