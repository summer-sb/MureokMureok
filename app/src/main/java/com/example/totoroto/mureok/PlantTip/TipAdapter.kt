package com.example.totoroto.mureok.PlantTip

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.totoroto.mureok.Data.TipData
import com.example.totoroto.mureok.R
import java.util.*

class TipAdapter : RecyclerView.Adapter<TipViewHolder>() {
    private var tipDatas: ArrayList<TipData> ?= null

    fun setTipDatas(pDatas: ArrayList<TipData>) {
        tipDatas = pDatas
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_tip, parent, false)

        return TipViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipViewHolder, position: Int) {
        val tipData = tipDatas?.get(position) ?: return

        holder.tvPlantItem.text = tipData.realName
    }

    override fun getItemCount(): Int {
        return tipDatas?.size ?: 0
    }
}
