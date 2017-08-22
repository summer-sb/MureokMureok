package com.example.totoroto.mureok.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.Community.CommunityFragment;
import com.example.totoroto.mureok.Data.FirebaseDBHelper;
import com.example.totoroto.mureok.Data.FirebaseStorageHelper;
import com.example.totoroto.mureok.Data.ListData;
import com.example.totoroto.mureok.Main.MainActivity;
import com.example.totoroto.mureok.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.totoroto.mureok.List.ListFragment.imgPath;


public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = "SOLBIN";
    private final int NUM_CANCEL = -1; //공유 취소
    public static final int VIEWTYPE_CARD = 0;
    public static final int VIEWTYPE_LIST = 1;
    public static final int REQ_GALLERY_LIST = 100;

    private Context context;
    private ArrayList<ListData> mListDatas;
    private FirebaseDBHelper firebaseDBHelper;
    private FirebaseStorageHelper firebaseStorageHelper;
    private boolean isFilter = false;

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_CARD;
        }
        return VIEWTYPE_LIST;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        firebaseDBHelper = new FirebaseDBHelper();
        firebaseStorageHelper = new FirebaseStorageHelper();

        if (viewType == VIEWTYPE_CARD) {
            View itemView = inflater.inflate(R.layout.item_list_card,
                    viewGroup, false);
            return new ListCardViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.item_list,
                    viewGroup, false);
            return new ListViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemCount() == 0) {
            ListData tmpListData = new ListData();

            mListDatas.add(tmpListData);
            notifyDataSetChanged();
        }

        if (holder.getItemViewType() == VIEWTYPE_CARD) {
            ((ListCardViewHolder) holder).btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aboutBtnAdd(holder);
                }
            });

            ((ListCardViewHolder) holder).btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aboutBtnReset(holder);
                }
            });

            ((ListCardViewHolder) holder).iv_listInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aboutImgClick(holder);
                }
            });

            ((ListCardViewHolder) holder).btnFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aboutFilterDialog((ListCardViewHolder) holder);
                }
            });
            if(isFilter){
                ((ListCardViewHolder) holder).btnFilter.setBackgroundResource(R.color.colorPrimary);
            }else{
                ((ListCardViewHolder) holder).btnFilter.setBackgroundResource(android.R.drawable.btn_default);
            }

        } else if (holder.getItemViewType() == VIEWTYPE_LIST) {
            ListData listData = mListDatas.get(position);

            ((ListViewHolder) holder).tvDate.setText(listData.date);
            ((ListViewHolder) holder).tvContents.setText(listData.contents);

            if (listData.getisShare()) {
                ((ListViewHolder) holder).btnShare.setBackgroundResource(R.color.colorPrimary);
            } else {
                ((ListViewHolder) holder).btnShare.setBackgroundResource(android.R.drawable.btn_default);
            }

            if (listData.imgPath != null) {
                try {
                    ((ListViewHolder) holder).ivImage.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .load(Uri.parse(listData.imgPath))
                            .override(3500, 1500)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(((ListViewHolder) holder).ivImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ((ListViewHolder) holder).btnShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (holder).getAdapterPosition();
                    aboutShareDialog(holder, pos);
                    isFilter = false;
                }
            });

            ((ListViewHolder) holder).btnModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (holder).getAdapterPosition();
                    aboutBtnModify(holder, pos);
                }
            });

            ((ListViewHolder) holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        if (mListDatas.get(pos).getisShare()) {//갤러리에서 삭제하면 공유 중인 경우, 커뮤니티에서도 지워줘야 한다.
                            firebaseStorageHelper.imageDelete(mListDatas.get(pos).getFirebaseKey());
                            firebaseStorageHelper.profileDelete(mListDatas.get(pos).getFirebaseKey());
                            firebaseDBHelper.deleteCommunityData(mListDatas.get(pos).getFirebaseKey());
                        }
                        firebaseDBHelper.deleteListData(mListDatas.get(pos).getFirebaseKey());
                        mListDatas.remove(pos);

                        notifyItemChanged(pos);
                        notifyItemRangeChanged(pos, mListDatas.size());
                    }
                }
            });
        }
    }

    private void aboutFilterDialog(final ListCardViewHolder holder) {

        final ArrayList<ListData> mFilteredDatas = new ArrayList<>();
        mFilteredDatas.clear();

        ListFilterDialog filterDialog = new ListFilterDialog();
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        filterDialog.show(fm, "listFilterDialog");

        filterDialog.setFilterResult(new ListFilterDialog.FilterDialogResult() {
            @Override
            public void apply(int year, int month, int day) {
                String str = "";
                if (month < 10) { //다이얼로그에서 date정보를 가져온다.
                    str += year + "년 0" + month + "월 " + day + "일";
                } else {
                    str += year + "년 " + month + "월 " + day + "일";
                }

                for (int pos = 1; pos < mListDatas.size(); pos++) { //pos 0 : cardview
                    String[] tempArr = mListDatas.get(pos).getDate().split(" ");

                    if (str.equals(tempArr[0]+" "+tempArr[1]+" "+tempArr[2])) {//날짜가 같으면
                        mFilteredDatas.add(mListDatas.get(pos));
                    }
                }

                if (mFilteredDatas.size() != 0) {
                    isFilter = true;

                    notifyItemRangeRemoved(0, getItemCount());
                    mListDatas.clear();

                    ListData tmpListData = new ListData();  //모두 삭제했기때문에 카드뷰 다시 만들어주기..
                    mListDatas.add(tmpListData);

                    for (int i = 0; i < mFilteredDatas.size(); i++) {
                        mListDatas.add(mFilteredDatas.get(i));
                    }
                    notifyItemRangeInserted(0, mListDatas.size()); //카드뷰(pos 0)빼고 내용 리프레시

                }else {
                    isFilter = false;
                   // Toast.makeText(context, "해당 날짜에 기록이 없습니다", Toast.LENGTH_SHORT).show();
                    firebaseDBHelper.readListData(mListDatas, ListAdapter.this);
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void aboutBtnModify(RecyclerView.ViewHolder holder, final int position) {
        final ListModifyDialog modifyDialog = new ListModifyDialog();

        Bundle args = new Bundle(2);    //pass data to dialog fragment
        args.putString("listImagePath", mListDatas.get(position).getImgPath());
        args.putString("listContents", mListDatas.get(position).getContents());
        modifyDialog.setArguments(args);

        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        modifyDialog.show(fm, "modifyDialog");
        //apply changed item

        modifyDialog.setDialogResult(new ListModifyDialog.ModifyDialogResult() {
            @Override
            public void apply(String imgPath, String contents) {
                long ctm = System.currentTimeMillis();
                Date currentDate = new Date(ctm);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm");

                mListDatas.get(position).setDate(dateFormat.format(currentDate));
                mListDatas.get(position).setImgPath(imgPath);
                mListDatas.get(position).setContents(contents);
                notifyDataSetChanged();

                firebaseDBHelper.updateListData(mListDatas.get(position).getFirebaseKey(), mListDatas.get(position));
            }
        });
    }

    private void aboutShareDialog(final RecyclerView.ViewHolder holder, final int position) {

        ListShareDialog shareDialog = new ListShareDialog();

        shareDialog.setSelectShareResult(new ListShareDialog.SelectShareResult() {
            @Override
            public void getResult(int selectId) {
                switch (selectId) {
                    case R.id.radioFlower:
                        mListDatas.get(position).setRadioFlower(true);
                        mListDatas.get(position).setisShare(true);
                        ((ListViewHolder) holder).btnShare.setBackgroundResource(R.color.colorPrimary);
                        break;
                    case R.id.radioHerb:
                        mListDatas.get(position).setRadioHerb(true);
                        mListDatas.get(position).setisShare(true);
                        ((ListViewHolder) holder).btnShare.setBackgroundResource(R.color.colorPrimary);
                        break;
                    case R.id.radioCactus:
                        mListDatas.get(position).setRadioCactus(true);
                        mListDatas.get(position).setisShare(true);
                        ((ListViewHolder) holder).btnShare.setBackgroundResource(R.color.colorPrimary);
                        break;
                    case R.id.radioVegetable:
                        mListDatas.get(position).setRadioVegetable(true);
                        mListDatas.get(position).setisShare(true);
                        ((ListViewHolder) holder).btnShare.setBackgroundResource(R.color.colorPrimary);
                        break;
                    case R.id.radioTree:
                        mListDatas.get(position).setRadioTree(true);
                        mListDatas.get(position).setisShare(true);
                        ((ListViewHolder) holder).btnShare.setBackgroundResource(R.color.colorPrimary);
                        break;
                    case NUM_CANCEL: //click btn cancel
                        mListDatas.get(position).setisShare(false);
                        ((ListViewHolder) holder).btnShare.setBackgroundResource(android.R.drawable.btn_default);

                        mListDatas.get(position).setRadioFlower(false);
                        mListDatas.get(position).setRadioHerb(false);
                        mListDatas.get(position).setRadioCactus(false);
                        mListDatas.get(position).setRadioVegetable(false);
                        mListDatas.get(position).setRadioTree(false);

                        break;
                    default:
                        Toast.makeText(context, "카테고리를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
                firebaseDBHelper.updateListShareData(mListDatas.get(position).getFirebaseKey(), mListDatas.get(position));

                if (mListDatas.get(position).getisShare()) {
                    sendCommunity(mListDatas.get(position));
                } else {//공유가 false이면 커뮤니티에서 지우고 저장소에도 지워준다.
                    firebaseStorageHelper.imageDelete(mListDatas.get(position).getFirebaseKey());
                    firebaseStorageHelper.profileDelete(mListDatas.get(position).getFirebaseKey());
                    firebaseDBHelper.deleteCommunityData(mListDatas.get(position).getFirebaseKey());
                }
            }
        });

        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        shareDialog.show(fm, "shareDialog");
    }

    private void sendCommunity(ListData listData) {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        CommunityFragment communityFragment = new CommunityFragment();
        MainActivity activity = (MainActivity) context;
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Bundle bundle = new Bundle(3);
        try {
            bundle.putString("userProfilePhoto", fUser.getPhotoUrl().toString());
            bundle.putString("userNickName", fUser.getDisplayName());
            bundle.putParcelable("sharedListData", listData);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        communityFragment.setArguments(bundle);
        transaction.replace(R.id.mainFrameLayout, communityFragment).commit();
    }

    private void aboutImgClick(RecyclerView.ViewHolder holder) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        ((Activity) context).startActivityForResult(intent, REQ_GALLERY_LIST);
    }

    private void aboutBtnReset(RecyclerView.ViewHolder holder) {
        imgPath = null;
        ((ListCardViewHolder) holder).iv_listInput.setImageResource(R.drawable.ic_img_placeholder);
        ((ListCardViewHolder) holder).et_listInput.setText("");
    }

    private void aboutBtnAdd(RecyclerView.ViewHolder holder) {
        if (!((ListCardViewHolder) holder).et_listInput.getText().toString().equals("") &&
                imgPath != null) {

            long ctm = System.currentTimeMillis();
            Date currentDate = new Date(ctm);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm");

            ListData tmpListData = new ListData(dateFormat.format(currentDate), imgPath, ((ListCardViewHolder) holder).et_listInput.getText().toString(), false
                    , false, false, false, false, false);
            mListDatas.add(tmpListData);
            firebaseDBHelper.writeNewListData(tmpListData);
            notifyDataSetChanged();
            aboutBtnReset(holder); //입력 item clear

        } else {
            Toast.makeText(context, "사진과 내용을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return (mListDatas != null) ? mListDatas.size() : 0;
    }

    public void setListDatas(ArrayList<ListData> listDatas) {
        mListDatas = listDatas;
    }

}
