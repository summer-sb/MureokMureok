package com.example.totoroto.mureok.list;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.totoroto.mureok.data.FirebaseDBHelper;
import com.example.totoroto.mureok.R;

public class ListShareDialog extends DialogFragment implements View.OnClickListener{
    private Button btnOk;
    private Button btnCancel;
    private RadioGroup radioGroup;
    private RadioButton radioFlower;
    private RadioButton radioHerb;
    private RadioButton radioCactus;
    private RadioButton radioVegetable;
    private RadioButton radioTree;

    private FirebaseDBHelper firebaseDBHelper;
    private int selectId;
    private SelectShareResult mShareResult;
    private final int NUM_CANCEL = -1;

    public interface SelectShareResult{
        void getResult(int selectId);
    }

    public void setSelectShareResult(SelectShareResult shareResult){
        mShareResult = shareResult;
    }

    public ListShareDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share, container);
        init(view);
        aboutRadioButton();

        return view;
    }

    private void aboutRadioButton() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
               selectId = checkedId;
            }
        });
    }

    private void init(View view) {
        firebaseDBHelper = new FirebaseDBHelper();

        btnCancel = (Button)view.findViewById(R.id.btnCancel_selectShare);
        btnOk = (Button)view.findViewById(R.id.btnOK_selectShare);
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        radioFlower = (RadioButton)view.findViewById(R.id.radioFlower);
        radioHerb = (RadioButton)view.findViewById(R.id.radioHerb);
        radioCactus = (RadioButton)view.findViewById(R.id.radioCactus);
        radioVegetable = (RadioButton)view.findViewById(R.id.radioVegetable);
        radioTree = (RadioButton)view.findViewById(R.id.radioTree);

        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOK_selectShare:
                mShareResult.getResult(selectId);
                dismiss();
                break;
            case R.id.btnCancel_selectShare:
                mShareResult.getResult(NUM_CANCEL);
                dismiss();
                break;
        }
    }

}
