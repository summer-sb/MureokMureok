package com.example.totoroto.mureok.login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.totoroto.mureok.data.FirebaseDBHelper;
import com.example.totoroto.mureok.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etJoinEmail;
    private EditText etJoinPassword;
    private EditText etJoinCheckPassword;
    private EditText etJoinNickName;
    private Button btnJoinComplete;
    private Button btnJoinCancel;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDBHelper firebaseDBHelper;

    private String userEmail;
    private String userPassword;
    private String userNickName;

    private final String TAG = "JOINSOLBIN";
    private final String defaultImagePath = "android.resource://com.example.totoroto.mureok/drawable/ic_profile_default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();
        currentUserState();
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

    private void currentUserState() {//로그인 상태 변화에 응답->현재어떤사용자가로그인중인지
  //
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // 유저 프로필 설정.
                    UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                            .setDisplayName(etJoinNickName.getText().toString())
                            .setPhotoUri(Uri.parse(defaultImagePath))
                            .build();
                    user.updateProfile(profileChange);

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    private void init() {
        firebaseDBHelper = new FirebaseDBHelper();
        mAuth = FirebaseAuth.getInstance();
        etJoinEmail = (EditText)findViewById(R.id.etJoinEmail);
        etJoinPassword = (EditText)findViewById(R.id.etJoinPassword);
        etJoinCheckPassword = (EditText)findViewById(R.id.etJoinCheckPassword);
        etJoinNickName = (EditText)findViewById(R.id.etJoinNickName);
        btnJoinComplete = (Button)findViewById(R.id.btnJoinComplete);
        btnJoinCancel = (Button)findViewById(R.id.btnJoinCancel);

        btnJoinComplete.setOnClickListener(this);
        btnJoinCancel.setOnClickListener(this);
    }

    private void createUser() {
        userEmail = etJoinEmail.getText().toString();
        userPassword = etJoinPassword.getText().toString();
        userNickName = etJoinNickName.getText().toString();
        Log.d(TAG, userEmail+ "|"+ userPassword);

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) { //가입을 실패한 경우
                            Toast.makeText(getApplicationContext(), R.string.joinFailed,
                                    Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "createUserWithEmail:not Complete:" + !task.isSuccessful());
                        }else {
                            //가입을 성공한 경우 db를 생성하고
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            firebaseDBHelper.writeNewUser(userEmail, userNickName, defaultImagePath);
                            //로그인 액티비티로 이동한다.
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.putExtra("nickName", userNickName);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnJoinComplete:
                createUser();
                break;
            case R.id.btnJoinCancel:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
