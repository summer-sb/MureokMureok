package com.example.totoroto.mureok.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.totoroto.mureok.R
import com.example.totoroto.mureok.R.id.*
import com.example.totoroto.mureok.community.CommunityFragment
import com.example.totoroto.mureok.data.FirebaseDBHelper
import com.example.totoroto.mureok.data.FirebaseStorageHelper
import com.example.totoroto.mureok.data.ListData
import com.example.totoroto.mureok.list.ListFragment.imgPath
import com.example.totoroto.mureok.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*


class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var firebaseDBHelper: FirebaseDBHelper = FirebaseDBHelper()
    private var firebaseStorageHelper: FirebaseStorageHelper = FirebaseStorageHelper()

    private var mListDatas: ArrayList<ListData>? = null
    private var isFilter = false

    private lateinit var context: Context
    private lateinit var imgUrl: String

    override fun getItemViewType(position: Int) = if(position == 0)  VIEWTYPE_CARD else VIEWTYPE_LIST

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = viewGroup.context
        val inflater = LayoutInflater.from(context)

        if (viewType == VIEWTYPE_CARD) {
            val itemView = inflater.inflate(R.layout.item_list_card, viewGroup, false)
            return ListCardViewHolder(itemView)
        } else {
            val itemView = inflater.inflate(R.layout.item_list, viewGroup, false)
            return ListViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == VIEWTYPE_CARD) {
            (holder as ListCardViewHolder).btnAdd.setOnClickListener { aboutBtnAdd(holder) }

            holder.btnReset.setOnClickListener { aboutBtnReset(holder) }

            holder.iv_listInput.setOnClickListener { aboutImgClick(holder) }

            holder.btnFilter.setOnClickListener { aboutFilterDialog(holder) }

            if (isFilter) {
                holder.btnFilter.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorAccent))
            } else {
                holder.btnFilter.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorPrimary))
            }

        } else if (holder.itemViewType == VIEWTYPE_LIST) {
            val listData = mListDatas?.get(position)

            (holder as ListViewHolder).tvDate.text = listData?.date
            holder.tvContents.text = listData?.contents

            if (listData?.getisShare() ?: return) {
                holder.btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorAccent))
            } else {
                holder.btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorPrimary))
            }

            if (listData.imgPath != null) {
                try {
                    holder.ivImage.visibility = View.VISIBLE

                    Glide.with(context)
                            .load(Uri.parse(listData.imgPath))
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .into(holder.ivImage)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            holder.btnShare.setOnClickListener {
                aboutShareDialog(holder, holder.getAdapterPosition())
                isFilter = false
            }

            holder.btnModify.setOnClickListener {
                aboutBtnModify(holder, holder.getAdapterPosition())
            }

            holder.btnDelete.setOnClickListener {
                val pos = holder.getAdapterPosition()

                if (pos != RecyclerView.NO_POSITION) {

                    if (mListDatas?.get(pos)?.getisShare() ?: false) {//갤러리에서 삭제하면 공유 중인 경우, 커뮤니티에서도 지워줘야 한다.
                        firebaseStorageHelper.imageDelete(mListDatas?.get(pos)?.getFirebaseKey())
                        firebaseStorageHelper.profileDelete(mListDatas?.get(pos)?.getFirebaseKey())
                        firebaseDBHelper.deleteCommunityData(mListDatas?.get(pos)?.getFirebaseKey())
                    }
                    firebaseDBHelper.deleteListData(mListDatas?.get(pos)?.getFirebaseKey())
                    mListDatas?.removeAt(pos)

                    notifyItemChanged(pos)
                    notifyItemRangeChanged(pos, mListDatas?.size ?: 0)
                }
            }
        }
    }

    private fun aboutFilterDialog(holder: ListCardViewHolder) {

        val mFilteredDatas = ArrayList<ListData>()
        mFilteredDatas.clear()

        val filterDialog = ListFilterDialog()
        val fm = (context as AppCompatActivity).supportFragmentManager
        filterDialog.show(fm, "listFilterDialog")

        filterDialog.setFilterResult { year, month, day ->
            var str = ""
            if (month < 10) { //다이얼로그에서 date정보를 가져온다.
                str += year.toString() + "년 0" + month + "월 " + day + "일"
            } else {
                str += year.toString() + "년 " + month + "월 " + day + "일"
            }

            for (pos in 1 until (mListDatas?.size ?: 1)) { //pos 0 : cardview
                val tempArr = mListDatas?.get(pos)?.getDate()?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()

                if (str == tempArr?.get(0) + " " + tempArr?.get(1) + " " + tempArr?.get(2)) {//날짜가 같으면
                    var posData = mListDatas?.get(pos)
                    if(posData != null) {
                        mFilteredDatas.add(posData)
                    }
                }
            }

            if (mFilteredDatas.size != 0) {
                isFilter = true

                notifyItemRangeRemoved(0, itemCount)
                mListDatas?.clear()

                //모두 삭제했기때문에 카드뷰 다시 만들어주기..
                mListDatas?.add(ListData())


                for (i in 0 until mFilteredDatas.size) {
                    mListDatas?.add(mFilteredDatas[i])
                }
                notifyItemRangeInserted(0, (mListDatas?.size ?: 0)) //카드뷰(pos 0)빼고 내용 리프레시

            } else {
                isFilter = false
                firebaseDBHelper.readListData(mListDatas, this@ListAdapter)
                notifyDataSetChanged()
            }
        }
    }

    private fun aboutBtnModify(holder: RecyclerView.ViewHolder, position: Int) {
        val modifyDialog = ListModifyDialog()

        val args = Bundle(2)    //pass data to dialog fragment
        args.putString("listImagePath", mListDatas?.get(position)?.getImgPath())
        args.putString("listContents", mListDatas?.get(position)?.getContents())
        modifyDialog.arguments = args

        val fm = (context as AppCompatActivity).supportFragmentManager
        modifyDialog.show(fm, "modifyDialog")
        //apply changed item

        modifyDialog.setDialogResult { imgPath, contents ->
            val ctm = System.currentTimeMillis()
            val currentDate = Date(ctm)
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 hh:mm")

            mListDatas?.get(position)?.setDate(dateFormat.format(currentDate))
            mListDatas?.get(position)?.setImgPath(imgPath)
            mListDatas?.get(position)?.setContents(contents)
            notifyDataSetChanged()

            firebaseDBHelper.updateListData(mListDatas?.get(position)?.getFirebaseKey(), mListDatas?.get(position))
        }
    }

    private fun aboutShareDialog(holder: RecyclerView.ViewHolder, position: Int) {

        val shareDialog = ListShareDialog()

        shareDialog.setSelectShareResult { selectId ->
            when (selectId) {
                radioFlower -> {
                    mListDatas?.get(position)?.isRadioFlower = true
                    mListDatas?.get(position)?.setisShare(true)
                    (holder as ListViewHolder).btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorAccent))
                }
                radioHerb -> {
                    mListDatas?.get(position)?.isRadioHerb = true
                    mListDatas?.get(position)?.setisShare(true)
                    (holder as ListViewHolder).btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorAccent))
                }
                radioCactus -> {
                    mListDatas?.get(position)?.isRadioCactus = true
                    mListDatas?.get(position)?.setisShare(true)
                    (holder as ListViewHolder).btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorAccent))
                }
                radioVegetable -> {
                    mListDatas?.get(position)?.isRadioVegetable = true
                    mListDatas?.get(position)?.setisShare(true)
                    (holder as ListViewHolder).btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorAccent))
                }
                radioTree -> {
                    mListDatas?.get(position)?.isRadioTree = true
                    mListDatas?.get(position)?.setisShare(true)
                    (holder as ListViewHolder).btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorAccent))
                }
                NUM_CANCEL //click btn cancel
                -> {
                    mListDatas?.get(position)?.setisShare(false)
                    (holder as ListViewHolder).btnShare.backgroundTintList = ColorStateList.valueOf(context.getColor(R.color.colorPrimary))

                    mListDatas?.get(position)?.isRadioFlower = false
                    mListDatas?.get(position)?.isRadioHerb = false
                    mListDatas?.get(position)?.isRadioCactus = false
                    mListDatas?.get(position)?.isRadioVegetable = false
                    mListDatas?.get(position)?.isRadioTree = false
                }
                else -> Toast.makeText(context, "카테고리를 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }
            notifyDataSetChanged()
            firebaseDBHelper.updateListShareData(mListDatas?.get(position)?.getFirebaseKey(), mListDatas?.get(position))

            val isShare = mListDatas?.get(position)?.getisShare()

            if(isShare != null) {
                val data = mListDatas?.get(position)
                if (isShare && data != null) {
                    sendCommunity(data)
                } else {//공유가 false이면 커뮤니티에서 지우고 저장소에도 지워준다.
                    val key =  mListDatas?.get(position)?.getFirebaseKey()
                    firebaseStorageHelper.imageDelete(key)
                    firebaseStorageHelper.profileDelete(key)
                    firebaseDBHelper.deleteCommunityData(key)
                }
            }
        }

        val fm = (context as AppCompatActivity).supportFragmentManager
        shareDialog.show(fm, "shareDialog")
    }

    private fun sendCommunity(listData: ListData) {
        val fUser = FirebaseAuth.getInstance().currentUser

        val communityFragment = CommunityFragment()
        val activity = context as MainActivity?
        val manager = activity?.supportFragmentManager
        val transaction = manager?.beginTransaction()

        firebaseStorageHelper.imageUpload(listData.getImgPath(), listData.getFirebaseKey())
        firebaseStorageHelper.setPassImageResult { uri ->
            imgUrl = uri.toString()

            val bundle = Bundle(4)
            try {
                bundle.putString("userProfilePhoto", fUser?.photoUrl.toString())
                bundle.putString("userNickName", fUser?.displayName)
                bundle.putString("imgUrl", imgUrl)
                bundle.putParcelable("sharedListData", listData)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }

            communityFragment.arguments = bundle
            transaction?.replace(R.id.mainFrameLayout, communityFragment)?.commit()
        }
    }

    private fun aboutImgClick(holder: RecyclerView.ViewHolder) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        (context as Activity).startActivityForResult(intent, REQ_GALLERY_LIST)
    }

    private fun aboutBtnReset(holder: RecyclerView.ViewHolder) {
        imgPath = null
        (holder as ListCardViewHolder).iv_listInput.setImageResource(R.drawable.ic_img_placeholder)
        holder.et_listInput.setText("")
    }

    private fun aboutBtnAdd(holder: RecyclerView.ViewHolder) {
        if ((holder as ListCardViewHolder).et_listInput.text.toString() != "" && imgPath != null) {

            val ctm = System.currentTimeMillis()
            val currentDate = Date(ctm)
            val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 hh:mm")

            val tmpListData = ListData(dateFormat.format(currentDate), imgPath, holder.et_listInput.text.toString(), false, false, false, false, false, false)
            mListDatas?.add(tmpListData)
            firebaseDBHelper.writeNewListData(tmpListData)
            notifyDataSetChanged()
            aboutBtnReset(holder) //입력 item clear

        } else {
            Toast.makeText(context, "사진과 내용을 입력해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = mListDatas?.size ?: 0

    fun setListDatas(listDatas: ArrayList<ListData>) {
        mListDatas = listDatas
    }

    companion object {
        const val TAG = "SOLBIN"
        const val VIEWTYPE_CARD = 0
        const val VIEWTYPE_LIST = 1
        const val REQ_GALLERY_LIST = 100
        const val NUM_CANCEL = -1 //공유 취소
    }

}
