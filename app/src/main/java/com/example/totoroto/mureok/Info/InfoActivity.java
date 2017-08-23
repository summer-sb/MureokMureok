package com.example.totoroto.mureok.Info;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.R;
import com.example.totoroto.mureok.RecyclerItemClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {
    private CircleImageView civProfile;
    private TextView tvNickName;
    private TextView tvEmail;
    private RecyclerView infoRecycler;
    private LinearLayoutManager layoutManager;
    private InfoAdapter infoAdapter;

    private ArrayList<String> mInfoLists;

    private FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
        setRecycler();
        addRecyclerData();
        updateInfo();
    }


    private void updateInfo() {
        Glide.with(getApplicationContext())
                .load(fUser.getPhotoUrl())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(civProfile);

        tvEmail.setText(fUser.getEmail());
        tvNickName.setText(fUser.getDisplayName());
    }


    private void addRecyclerData() {
        mInfoLists.add("프로필 변경");
        mInfoLists.add("비밀번호 변경");
        mInfoLists.add("회원 탈퇴");
        infoAdapter.notifyDataSetChanged();

        infoRecycler.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                infoRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0: //프로필 변경
                        loadInfoProfileDialog();
                        break;
                    case 1:
                        loadInfoPwChangeDialog();
                        break;
                    case 2: //회원 탈퇴
                        loadRemoveUserDialog();
                        break;
                }
            }
        }));

    }

    private void loadInfoPwChangeDialog() {
        InfoPwChangeDialog pwDialog = new InfoPwChangeDialog();

        FragmentManager fm = getSupportFragmentManager();
        pwDialog.show(fm, "pwDialog");
    }

    private void loadRemoveUserDialog() {
        InfoRemoveDialog removeDialog = new InfoRemoveDialog();

        FragmentManager fm = getSupportFragmentManager();
        removeDialog.show(fm, "InfoRemoveDialog");
    }

    private void loadInfoProfileDialog() {
        InfoProfileDialog profileDialog = new InfoProfileDialog();

        FragmentManager fm = getSupportFragmentManager();
        profileDialog.show(fm, "InfoProfileDialog");

        //apply changed info
        profileDialog.setDialogResult(new InfoProfileDialog.ProfileDialogResult() {
            @Override
            public void apply(String nickName, String imgPath) {
                Glide.with(getApplicationContext())
                        .load(Uri.parse(imgPath))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(civProfile);

                tvNickName.setText(nickName);
            }
        });
    }


    private void setRecycler() {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        infoRecycler.setHasFixedSize(true);
        infoRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDeco = new DividerItemDecoration(infoRecycler.getContext(), layoutManager.getOrientation());
        infoRecycler.addItemDecoration(mDividerItemDeco);

        infoAdapter = new InfoAdapter();
        infoAdapter.setInfoDatas(mInfoLists);
        infoRecycler.setAdapter(infoAdapter);
    }

    private void init() {
        civProfile = (CircleImageView)findViewById(R.id.civProfile_info);
        tvNickName = (TextView)findViewById(R.id.tvNickName_Info);
        tvEmail = (TextView)findViewById(R.id.tvEmail_info);
        infoRecycler = (RecyclerView)findViewById(R.id.infoRecycler);

        mInfoLists = new ArrayList<>();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
    }
}
