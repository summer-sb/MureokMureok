package com.example.totoroto.mureok.Info;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoProfileDialog extends DialogFragment implements View.OnClickListener{
    private final int REQ_GALLERY_INFO_DIALOG = 600;
    private String changedProfileImgPath;

    private CircleImageView civProfile;
    private EditText etNickName;
    private Button btnCancel;
    private Button btnOK;

    private FirebaseUser fUser;
    private FirebaseDBHelper firebaseDBHelper;

    private ProfileDialogResult mProfileResult;

    public interface ProfileDialogResult{
        void apply(String nickName,String imgPath);
    }
    public void setDialogResult(ProfileDialogResult profileResult){
        mProfileResult = profileResult;
    }

    public InfoProfileDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_info_profile, container);

        init(view);
        setOriginProfile();

        return view;
    }

    private void setOriginProfile() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        Glide.with(getActivity().getApplicationContext())
                .load(fUser.getPhotoUrl())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(civProfile);
        etNickName.setText(fUser.getDisplayName());
    }

    private void loadGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY_INFO_DIALOG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GALLERY_INFO_DIALOG && resultCode == Activity.RESULT_OK) {
            try {
                changedProfileImgPath = data.getData().toString();
                Glide.with(getContext())
                        .load(data.getData())
                        .override(100,100)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(civProfile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void applyChangeProfile() {
        UserProfileChangeRequest profileUpdates;
        try {
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(etNickName.getText().toString())
                    .setPhotoUri(Uri.parse(changedProfileImgPath))
                    .build();
            Log.d("SOLBIN", "changedPath:"+ changedProfileImgPath);
        }catch (NullPointerException e){
            Log.d("SOLBIN", "applyChangeProfile nullpointerException");
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(fUser.getDisplayName())
                    .setPhotoUri(fUser.getPhotoUrl())
                    .build();
        }

        fUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            try {
                                mProfileResult.apply(etNickName.getText().toString(), changedProfileImgPath);
                                firebaseDBHelper.updateUserProfile(etNickName.getText().toString(), changedProfileImgPath);
                                Toast.makeText(getActivity().getApplicationContext(), "프로필이 업데이트 되었습니다",Toast.LENGTH_SHORT).show();
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                            dismiss();
                        }
                    }
                });
    }
    private void init(View view) {
        firebaseDBHelper = new FirebaseDBHelper();

        civProfile = (CircleImageView)view.findViewById(R.id.civPofile_infoDialog);
        etNickName = (EditText)view.findViewById(R.id.et_infoDialog);
        btnCancel = (Button)view.findViewById(R.id.btnCancel_infoDialog);
        btnOK = (Button)view.findViewById(R.id.btnOK_infoDialog);

        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        civProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCancel_infoDialog:
                dismiss();
                break;
            case R.id.btnOK_infoDialog:
                applyChangeProfile();
                break;
            case R.id.civPofile_infoDialog:
                loadGallery();
                break;

        }
    }


}
