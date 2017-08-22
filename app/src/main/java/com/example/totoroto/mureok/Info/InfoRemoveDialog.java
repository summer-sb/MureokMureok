package com.example.totoroto.mureok.Info;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.LoginActivity;
import com.example.totoroto.mureok.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Totoroto on 2017-08-21.
 */

public class InfoRemoveDialog extends DialogFragment implements View.OnClickListener {
    private Button btnCancel;
    private Button btnOK;
    private EditText etPassword;

    public InfoRemoveDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_removeuser_info, container);

        btnCancel = (Button) view.findViewById(R.id.btnCancel_removeDialog);
        btnOK = (Button) view.findViewById(R.id.btnOK_removeDialog);
        etPassword = (EditText) view.findViewById(R.id.etPW_removeDialog);

        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel_removeDialog:
                dismiss();
                break;
            case R.id.btnOK_removeDialog:
                aboutRemove();
                break;
        }
    }

    private void aboutRemove() {
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        String userPassword = "";
        try {
            userPassword = etPassword.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO: 비밀번호를 입력하면 -> 삭제
        if (!userPassword.equals("")) {
            AuthCredential credential = EmailAuthProvider.getCredential(fUser.getEmail(), userPassword);

            fUser.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           //Log.d("SOLBIN", "User re-authenticated.");
                        }
                    });

            fUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //db에서도 삭제해주고
                        FirebaseDBHelper dbHelper = new FirebaseDBHelper();
                        dbHelper.removeUser(fUser.getUid());
                        //로그인 화면으로 돌아간다.
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity().getApplicationContext(), "회원 탈퇴 되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
