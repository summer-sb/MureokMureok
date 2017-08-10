package com.example.totoroto.mureok.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Data.FirebaseDB;
import com.example.totoroto.mureok.Data.ListData;
import com.example.totoroto.mureok.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListFragment extends Fragment implements View.OnClickListener {
    private final int REQ_GALLERY_PICTURE = 1;
    private final String TAG = "LF";
    private String imgPath;

    private ListAdapter listAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<ListData> mListDatas;

    private ImageView ivImg;
    private Button btnReset;
    private Button btnAdd;
    private EditText etInput;
    private RecyclerView recyclerView;

    private FirebaseDB firebaseDB;


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        init(view);
        aboutRecycler();
        firebaseDB.readListData(mListDatas, listAdapter);

        return view;
    }

    private void aboutRecycler() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        listAdapter = new ListAdapter();
        listAdapter.setListDatas(mListDatas);
        recyclerView.setAdapter(listAdapter);
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.listRecycler);
        ivImg = (ImageView) view.findViewById(R.id.iv_listInput);
        etInput = (EditText) view.findViewById(R.id.et_listInput);
        btnReset = (Button) view.findViewById(R.id.btnReset_listInput);
        btnAdd = (Button) view.findViewById(R.id.btnAdd_listInput);

        ivImg.setOnClickListener(this); //이미지 버튼 클릭-> 사진 추가
        btnReset.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        mListDatas = new ArrayList<>();
        firebaseDB = new FirebaseDB();
    }

    private void addItemFunc() {
        if (!etInput.getText().toString().equals("")) {

            long ctm = System.currentTimeMillis();
            Date currentDate = new Date(ctm);
            SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.dateFormat));

            ListData tmpListData = new ListData(dateFormat.format(currentDate), imgPath, etInput.getText().toString(), false);

            mListDatas.add(tmpListData);
            firebaseDB.writeNewListData(tmpListData);
            listAdapter.notifyDataSetChanged();
            aboutBtnReset(); //입력 item clear
        }else{
            Toast.makeText(getActivity(), "내용을 입력해 주세요.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GALLERY_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                imgPath = data.getData().toString(); //이미지의 uri
                Glide.with(getContext())
                        .load(data.getData())
                        .override(1000, 1000)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(ivImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void LoadPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_GALLERY_PICTURE);
    }

    private void aboutBtnReset() {
        imgPath = null;
        ivImg.setImageResource(R.drawable.ic_img_placeholder);
        etInput.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReset_listInput:
                aboutBtnReset();
                break;
            case R.id.btnAdd_listInput:
                addItemFunc();
                break;
            case R.id.iv_listInput:
                LoadPicture();
                break;
            default:
                //
        }
    }


}
