package com.example.totoroto.mureok.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.item_list_card.view.*

class ListCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var iv_listInput: ImageView = itemView.iv_listInput
    var et_listInput: EditText = itemView.et_listInput
    var btnReset: Button = itemView.btnReset_listInput
    var btnAdd: Button = itemView.btnAdd_listInput
    var btnFilter: Button = itemView.btnFilter_listInput
}
