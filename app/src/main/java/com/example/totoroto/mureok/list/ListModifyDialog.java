package com.example.totoroto.mureok.list;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.R;

public class ListModifyDialog extends DialogFragment implements View.OnClickListener{
    private final int REQ_GALLERY_MODIFY_IMG = 333;
    private String changedImgPath = "";
    private ModifyDialogResult mDialogResult;

    private ImageView imageView;
    private EditText editText;
    private Button btnCancel;
    private Button btnOK;

    private String originImgPath;
    private String originContents;


    public interface ModifyDialogResult{
        void apply(String imgPath, String contents);
    }

    public void setDialogResult(ModifyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public ListModifyDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_modify_list, container);
        init(view);
        setOriginalListItem();
        loadGallery();

        return view;
    }

    private void loadGallery() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_GALLERY_MODIFY_IMG);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GALLERY_MODIFY_IMG && resultCode == Activity.RESULT_OK) {
            try {
                changedImgPath = data.getData().toString();
                Glide.with(getContext())
                        .load(data.getData())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setOriginalListItem() {
        originImgPath = getArguments().getString("listImagePath");
        originContents = getArguments().getString("listContents");

        Glide.with(getContext())
                .load(Uri.parse(originImgPath))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

        editText.setText(originContents);
    }

    private void init(View view) {
        imageView = (ImageView)view.findViewById(R.id.iv_modifyDialog);
        editText = (EditText)view.findViewById(R.id.et_modifyDialog);
        btnCancel = (Button)view.findViewById(R.id.btnCancel_modifyDialog);
        btnOK = (Button)view.findViewById(R.id.btnOK_modifyDialog);

        btnCancel.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel_modifyDialog:
                dismiss();
                break;
            case R.id.btnOK_modifyDialog:
                aboutBtnOK();
                break;

        }
    }

    private void aboutBtnOK() {
        String changedContents = editText.getText().toString();

        if(changedContents.equals("")){
            Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
        }else if(changedImgPath.equals("")){
            changedImgPath = originImgPath;
        }else{
            mDialogResult.apply(changedImgPath, changedContents);
            dismiss();
        }
        /*
        if(!changedImgPath.equals("") && !changedContents.equals("")) {
            mDialogResult.apply(changedImgPath, changedContents);
            dismiss();
        }*/
    }
}
