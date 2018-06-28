package com.example.totoroto.mureok.manage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.R;

import java.util.Calendar;

public class AddPlantDialog extends DialogFragment implements View.OnClickListener{
    private final int REQ_GALLERY_MANAGE = 100;
    private String pName;
    private String pRealName;
    private String pDate;
    private String pPicture;

    private DialogResult mDialogResult;
    private Calendar calendar;

    private Button btnPositive;
    private Button btnNegative;
    private Button btnCalendar;
    private Button btnPhoto;

    private EditText et_pRealName;
    private EditText et_pName;
    private TextView tv_pDate;
    private ImageView iv_pPicture;

    public interface DialogResult{ //about waterset dialog
        void apply(String picture, String name, String realName, String enrollDate);
    }

    public void setDialogResult(DialogResult dialogResult){
        mDialogResult = dialogResult;
    }
    public AddPlantDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add, container);
        init(view);

        return view;
    }

    private void init(View view) {
        btnPositive = (Button)view.findViewById(R.id.dialog_btnPositive);
        btnNegative = (Button)view.findViewById(R.id.dialog_btnNegative);
        btnCalendar = (Button)view.findViewById(R.id.dialog_btnCalendar);
        btnPhoto = (Button)view.findViewById(R.id.dialog_btnPicture);
        et_pName = (EditText) view.findViewById(R.id.dialog_etName);
        et_pRealName = (EditText)view.findViewById(R.id.dialog_etRealname);
        tv_pDate = (TextView)view.findViewById(R.id.dialog_tvDate);
        iv_pPicture = (ImageView)view.findViewById(R.id.dialog_ivPicture);

        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);
        btnCalendar.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);

        calendar = Calendar.getInstance();
        tv_pDate.setText(calendar.get(Calendar.YEAR)+"."+String.valueOf(calendar.get(Calendar.MONTH)+1) +"."
                +calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btnPositive:
                aboutBtnPositive();
                break;
            case R.id.dialog_btnNegative:
                dismiss();
                break;
            case R.id.dialog_btnCalendar:
                aboutBtnCalendar();
                break;
            case R.id.dialog_btnPicture:
                aboutPicture();
                break;
            default:
                //
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { //about get Gallery image

        if (requestCode == REQ_GALLERY_MANAGE && resultCode == Activity.RESULT_OK) {
            try {
                pPicture = data.getData().toString(); //이미지의 uri

                 Glide.with(getContext())
                        .load(data.getData())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(iv_pPicture);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void aboutPicture() { //about get Gallery image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY_MANAGE);
    }

    private void aboutBtnCalendar() {
        DatePickerDialog pickerDialog = new DatePickerDialog(getContext(),  new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 데이터피커에서 선택한 날짜 처리 하는 부분
                tv_pDate.setText(String.valueOf(year+"."+ (monthOfYear+1) +"."+dayOfMonth));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), (calendar.get(Calendar.DAY_OF_MONTH)));

        pickerDialog.show();
    }

    private void aboutBtnPositive() {
        pName = et_pName.getText().toString();
        pRealName = et_pRealName.getText().toString();
        pDate = tv_pDate.getText().toString();

        if (pRealName != null && pPicture != null) { //적어도 입력해줘야 할 것
            mDialogResult.apply(pPicture, pName, pRealName, pDate);
        }
        dismiss();
    }
}
