package com.example.totoroto.mureok.PlantTip

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.totoroto.mureok.Data.TipDetailData
import com.example.totoroto.mureok.R

class TipDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var ivImage: ImageView
    private lateinit var plantName: TextView
    private lateinit var frt: TextView
    private lateinit var temperature: TextView
    private lateinit var hydro: TextView
    private lateinit var prpg: TextView
    private lateinit var soil: TextView
    private lateinit var waterSpring: TextView
    private lateinit var waterSummer: TextView
    private lateinit var waterAutumn: TextView
    private lateinit var waterWinter: TextView
    private lateinit var close: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip_detail)

        init()
        setData()
    }

    private fun setData() {
        val bundle = intent.extras
        val detailData = bundle.getParcelable<TipDetailData>(INTENT_STR)
        val imagePath = bundle.getString("plantImage")
        val pRealName = bundle.getString("plantName")
        plantName.text = pRealName

        try {
            val path = imagePath.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val realPath = "http://www.nongsaro.go.kr/cms_contents/301/$path"

            Glide.with(applicationContext)
                    .load(Uri.parse(realPath))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(ivImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            frt.text = detailData!!.getFrt()

            when (detailData.getTemperature()) {
                "082001" -> temperature.setText(R.string.temperature1)
                "082002" -> temperature.setText(R.string.temperature2)
                "082003" -> temperature.setText(R.string.temperature3)
                "082004" -> temperature.setText(R.string.temperature4)
                else -> temperature.setText(R.string.notFound_info)
            }
            when (detailData.getHydro()) {
                "083001" -> hydro.setText(R.string.hydro1)
                "083002" -> hydro.setText(R.string.hydro2)
                "083003" -> hydro.setText(R.string.hydro3)
                else -> hydro.setText(R.string.notFound_info)
            }
            prpg.text = detailData.getPrpg()
            soil.text = detailData.getSoil()

            when (detailData.getWaterSpring()) {
                "053001" -> waterSpring.setText(R.string.water1)
                "053002" -> waterSpring.setText(R.string.water2)
                "053003" -> waterSpring.setText(R.string.water3)
                "053004" -> waterSpring.setText(R.string.water4)
                else -> waterSpring.setText(R.string.notFound_info)
            }

            when (detailData.getWaterSummer()) {
                "053001" -> waterSummer.setText(R.string.water1)
                "053002" -> waterSummer.setText(R.string.water2)
                "053003" -> waterSummer.setText(R.string.water3)
                "053004" -> waterSummer.setText(R.string.water4)
                else -> waterSummer.setText(R.string.notFound_info)
            }

            when (detailData.getWaterAutumn()) {
                "053001" -> waterAutumn.setText(R.string.water1)
                "053002" -> waterAutumn.setText(R.string.water2)
                "053003" -> waterAutumn.setText(R.string.water3)
                "053004" -> waterAutumn.setText(R.string.water4)
                else -> waterAutumn.setText(R.string.notFound_info)
            }

            when (detailData.getWaterWinter()) {
                "053001" -> waterWinter.setText(R.string.water1)
                "053002" -> waterWinter.setText(R.string.water2)
                "053003" -> waterWinter.setText(R.string.water3)
                "053004" -> waterWinter.setText(R.string.water4)
                else -> waterWinter.setText(R.string.notFound_info)
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    private fun init() {
        ivImage = findViewById<View>(R.id.ivTip_img) as ImageView
        plantName = findViewById<View>(R.id.tvPname_tip_detail) as TextView
        frt = findViewById<View>(R.id.tvTip_frt) as TextView
        temperature = findViewById<View>(R.id.tvTip_temperature) as TextView
        hydro = findViewById<View>(R.id.tvTip_hydro) as TextView
        prpg = findViewById<View>(R.id.tvTip_prpg) as TextView
        soil = findViewById<View>(R.id.tvTip_soil) as TextView
        waterSpring = findViewById<View>(R.id.tvTip_waterSpring) as TextView
        waterSummer = findViewById<View>(R.id.tvTip_waterSummer) as TextView
        waterAutumn = findViewById<View>(R.id.tvTip_waterAutumn) as TextView
        waterWinter = findViewById<View>(R.id.tvTip_waterWinter) as TextView
        close = findViewById<View>(R.id.btnTipClose) as Button

        close.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTipClose -> finish()
        }
    }

    companion object {
        val INTENT_STR = "TipDetailData"
    }
}
