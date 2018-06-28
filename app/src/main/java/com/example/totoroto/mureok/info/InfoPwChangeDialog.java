package com.example.totoroto.mureok.info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.totoroto.mureok.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Totoroto on 2017-08-22.
 */

public class InfoPwChangeDialog extends DialogFragment implements View.OnClickListener{
    private EditText etCurrentPW;
    private EditText etChangePW;
    private EditText etConfirmPW;

    private Button btnCancel;
    private Button btnOK;
    private Context context;

    public InfoPwChangeDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info_password, container);
        context = getActivity();
        init(view);

        return view;
    }

    private void aboutChangePW() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentPassword = "";
        //비밀번호 바꾸기 전에 재인증해주기
        try {
            currentPassword = etCurrentPW.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "현재 비밀번호가 올바르지 않습니다",Toast.LENGTH_SHORT).show();
        }
        AuthCredential credential = EmailAuthProvider
                .getCredential(fUser.getEmail(), currentPassword);

        fUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Log.d(TAG, "User re-authenticated.");
                    }
                });
        //비밀번호 변경
       String changedPassword = "";
        try {
            changedPassword = etChangePW.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!currentPassword.equals(changedPassword)) {
            fUser.updatePassword(changedPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(context, "현재 비밀번호와 다르게 입력해주세요",Toast.LENGTH_SHORT).show();
        }
    }

    private void init(View view) {
        etCurrentPW = (EditText)view.findViewById(R.id.etCurrentPW_dialogPW);
        etChangePW = (EditText)view.findViewById(R.id.etChangePW_dialogPW);
        etConfirmPW = (EditText)view.findViewById(R.id.etConfirmPW_dialogPW);
        btnCancel = (Button)view.findViewById(R.id.btnCancel_dialogPW);
        btnOK = (Button)view.findViewById(R.id.btnOK_dialogPW);

        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel_dialogPW:
                dismiss();
                break;
            case R.id.btnOK_dialogPW:
                aboutChangePW();
                dismiss();
                break;
        }
    }
}
