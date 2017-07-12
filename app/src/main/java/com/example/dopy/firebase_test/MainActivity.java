package com.example.dopy.firebase_test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.editPassword)
    EditText editPassword;

    Toast toast;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String TAG = "FireBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthstateChanged:sighned_id:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthstateChanged:sighed_out");
                }
            }
        };
        send();
    }

    public void send() {
        user users = new user("dlwlrjs1@gmail.com", "이지건");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("user").push().setValue(users);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //Sign up new USER
    @OnClick(R.id.btnJoin)
    public void onClickJoin() {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "create UserWithEmail");
                if (!task.isSuccessful()) {
                    toast = Toast.makeText(MainActivity.this, "회원가입 실패", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
                    toast.show();
                } else {
                    toast = Toast.makeText(MainActivity.this, "회원가입 성공", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });
    }

    //Sign in User
    @OnClick(R.id.btnLogin)
    public void onClickSignIn() {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signinWithEmail:OnCompleteLogin");

                if (!task.isSuccessful()) {
                    Log.d(TAG, "signinWithEmail:failed");
                    toast = Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
                    toast.show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String email = user.getEmail();
                        String uid = user.getUid();
                        toast = Toast.makeText(MainActivity.this, "Email:" + email + " uid:" + uid, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
                        toast.show();

                    }
                }
            }
        });
    }
}


