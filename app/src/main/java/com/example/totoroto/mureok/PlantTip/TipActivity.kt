package com.example.totoroto.mureok.PlantTip

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.totoroto.mureok.Data.DetailParams
import com.example.totoroto.mureok.Data.TipData
import com.example.totoroto.mureok.Data.TipDetailData
import com.example.totoroto.mureok.R
import com.example.totoroto.mureok.RecyclerItemClickListener
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStreamReader
import java.net.URL

class TipActivity : AppCompatActivity(), View.OnClickListener {

    private var tempLists: ArrayList<TipData>? = null
    private var filteredLists: ArrayList<TipData> = ArrayList()
    private var tipDatas: ArrayList<TipData> = ArrayList()

    private lateinit var tipAdapter: TipAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var btnBack: Button
    private lateinit var etTipSearch: EditText
    private lateinit var recyclerTip: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tip)

        init()
        aboutRecycler()
        aboutTipDetail()

        val loadTask = LoadTask()
        loadTask.execute()

        aboutEditTextSearch()
    }

    private inner class LoadTask : AsyncTask<Void, Void, Void?>() {
        override fun doInBackground(vararg voids: Void): Void? {
            getPlantListData()
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            aboutMapListData()
        }
    }


    private fun aboutEditTextSearch() {
        etTipSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var s = s
                filteredLists.clear()
                s = s.toString().toLowerCase()

                for (i in 0..tipDatas.size) {
                    val str = tipDatas.get(i).realName //식물 이름
                    if (str?.contains(s) != null) {
                        filteredLists.add(tipDatas.get(i))
                    }
                }
                tipAdapter.setTipDatas(filteredLists)
                tipAdapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun aboutTipDetail() {

        recyclerTip.addOnItemTouchListener(RecyclerItemClickListener(applicationContext,
                recyclerTip, RecyclerItemClickListener.OnItemClickListener { view, position ->
            val plantCode: Int?

            if (filteredLists.size != 0) {
                plantCode = filteredLists.get(position).code
                Log.d(TAG, "pCode$plantCode|position$position")
            } else {
                plantCode = tipDatas.get(position).code
                Log.d(TAG, "pCode$plantCode|position$position")
            }

            val detailParams = DetailParams(plantCode, position);
            val detailTask = DetailTask()
            detailTask.execute(detailParams)
        }))
    }

    private inner class DetailTask : AsyncTask<DetailParams, Void, Void?>{
        constructor() : super()

        override fun doInBackground(vararg params: DetailParams): Void? {
            getPlantDetailData(params[0].plantCode, params[0].position)
            return null
        }
    }

    private fun getPlantDetailData(pCode: Int?, position: Int) {
        val queryUrl = ("http://api.nongsaro.go.kr/service/garden/gardenDtl"
                + "?apiKey=" + API_KEY + "&cntntsNo=" + pCode)

        try {
            val url = URL(queryUrl) //문자열로 된 요청 url을 URL 객체로 생성.
            val `is` = url.openStream()  //url위치로 입력스트림 연결

            val factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()

            xpp.setInput(InputStreamReader(`is`, "UTF-8"))  //inputstream 으로부터 xml 입력받기

            var tag: String

            xpp.next()
            var eventType = xpp.eventType
            val tipDetailData = TipDetailData()

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        tag = xpp.name    //태그 이름 얻어오기

                        if (tag == "item")
                        else if (tag == TAG_FRT) {
                            xpp.next()
                            tipDetailData.frt = xpp.text
                        } else if (tag == TAG_TEMPERATURE) {
                            xpp.next()
                            tipDetailData.temperature = xpp.text
                        } else if (tag == TAG_HYDRO) {
                            xpp.next()
                            tipDetailData.hydro = xpp.text
                        } else if (tag == TAG_PRPG) {
                            xpp.next()
                            tipDetailData.prpg = xpp.text
                        } else if (tag == TAG_SOIL) {
                            xpp.next()
                            tipDetailData.soil = xpp.text
                        } else if (tag == TAG_WATER_SPRING) {
                            xpp.next()
                            tipDetailData.waterSpring = xpp.text
                        } else if (tag == TAG_WATER_SUMMER) {
                            xpp.next()
                            tipDetailData.waterSummer = xpp.text
                        } else if (tag == TAG_WATER_AUTUMN) {
                            xpp.next()
                            tipDetailData.waterAutumn = xpp.text
                        } else if (tag == TAG_WATER_WINTER) {
                            xpp.next()
                            tipDetailData.waterWinter = xpp.text
                        }
                    }
                }
                eventType = xpp.next()

                if (tipDetailData.waterWinter != null) {
                    eventType = XmlPullParser.END_DOCUMENT //while 종료


                    if (filteredLists.size == 0) {
                        //detail 액티비티로 객체 보내기
                        val intent = Intent(applicationContext, TipDetailActivity::class.java)
                        intent.putExtra(INTENT_STR, tipDetailData)
                        intent.putExtra("plantImage", tipDatas.get(position).image)
                        intent.putExtra("plantName", tipDatas.get(position).realName)
                        startActivity(intent)
                    } else {
                        val intent = Intent(applicationContext, TipDetailActivity::class.java)
                        intent.putExtra(INTENT_STR, tipDetailData)
                        intent.putExtra("plantImage", filteredLists.get(position).image)
                        intent.putExtra("plantName", filteredLists.get(position).realName)
                        startActivity(intent)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun init() {
        recyclerTip = findViewById<View>(R.id.recyclerTip) as RecyclerView
        btnBack = findViewById<View>(R.id.btnBack_tip) as Button
        etTipSearch = findViewById<View>(R.id.etTipSearch) as EditText

        btnBack.setOnClickListener(this)
    }

    private fun aboutRecycler() {
        layoutManager = LinearLayoutManager(applicationContext)
        recyclerTip.setHasFixedSize(true)
        recyclerTip.layoutManager = layoutManager
        val mDividerItemDeco = DividerItemDecoration(recyclerTip.context, layoutManager.orientation)
        recyclerTip.addItemDecoration(mDividerItemDeco)

        tipAdapter = TipAdapter()
        tipAdapter.setTipDatas(tipDatas)
        recyclerTip.adapter = tipAdapter
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnBack_tip -> finish()
        }
    }

    fun getPlantListData() {
        tempLists = ArrayList()
        var pNo = 0
        var pName = ""
        var pImage = ""

        val queryUrl = ("http://api.nongsaro.go.kr/service/garden/gardenList"
                + "?apiKey=" + API_KEY + "&numOfRows=" + NUM_OF_ROWS)

        try {
            val url = URL(queryUrl) //문자열로 된 요청 url을 URL 객체로 생성.
            val `is` = url.openStream()  //url위치로 입력스트림 연결

            val factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()

            xpp.setInput(InputStreamReader(`is`, "UTF-8"))  //inputstream 으로부터 xml 입력받기

            var tag: String
            xpp.next()
            var eventType = xpp.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        tag = xpp.name    //태그 이름 얻어오기

                        if (tag == "item")
                        else if (tag == "cntntsNo") { //식물 넘버
                            xpp.next()
                            pNo = Integer.valueOf(xpp.text)
                        } else if (tag == "cntntsSj") { //식물명
                            xpp.next()
                            pName = xpp.text
                        } else if (tag == "rtnStreFileNm") { //이미지 파일 경로
                            xpp.next()
                            pImage = xpp.text
                            val data = TipData(pNo, pName, pImage)
                            tempLists?.add(data)
                        }
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun aboutMapListData() {
        val listSize = tempLists?.size ?: return

        for (i in 0 until listSize) {
            val tData = TipData(tempLists?.get(i)?.code, tempLists?.get(i)?.realName, tempLists?.get(i)?.image)
            tipDatas.add(tData)
        }

        tipAdapter.notifyDataSetChanged()
    }

    companion object {
        val INTENT_STR = "TipDetailData"
        private val TAG = "TipPlant"

        private val API_KEY = "20170726ADJPJHDALH0FXS5HK84T7G"
        private val TAG_FRT = "frtlzrInfo"
        private val TAG_TEMPERATURE = "grwhTpCode"
        private val TAG_HYDRO = "hdCode"
        private val TAG_PRPG = "prpgtEraInfo"
        private val TAG_SOIL = "soilInfo"
        private val TAG_WATER_SPRING = "watercycleSprngCode"
        private val TAG_WATER_SUMMER = "watercycleSummerCode"
        private val TAG_WATER_AUTUMN = "watercycleAutumnCode"
        private val TAG_WATER_WINTER = "watercycleWinterCode"

        private val NUM_OF_ROWS = 216
    }
}


