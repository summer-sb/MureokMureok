package com.example.totoroto.mureok;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.totoroto.mureok.Main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etEmail;
    private EditText etPW;
    private Button btnLogin;
    private Button btnJoin;
    private Button btnNonMember;
    private Button btnGoogleJoin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private final static String TAG = "LoginActivity";
    private String userEmail;
    private String userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        currentUserState();
    }

    private void currentUserState() {//로그인 상태 변화에 응답->현재어떤사용자가로그인중인지
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void aboutLogin() {
        userEmail = etEmail.getText().toString();
        userPassword = etPW.getText().toString();
        //사용자가 앱에 로그인하면 이메일 주소와 비번을 아래 리스너에 전달한다.
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //비밀번호는 6자 이상 입력.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(getApplicationContext(), R.string.loginFailed,
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            //로그인 성공하면 메인 액티비티로 이동
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                aboutLogin();
                break;
            case R.id.btnJoin:
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
                break;
            //case R.id.btnGoogleJoin:
            //    break;
            case R.id.btnNonMember:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void init() {
        etEmail = (EditText)findViewById(R.id.etLoginEmail);
        etPW = (EditText)findViewById(R.id.etLoginPW);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnJoin = (Button)findViewById(R.id.btnJoin);
        btnNonMember = (Button)findViewById(R.id.btnNonMember);
        btnGoogleJoin = (Button)findViewById(R.id.btnGoogleLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
        btnNonMember.setOnClickListener(this);
//        btnGoogleJoin.setOnClickListener(this);
    }
}
