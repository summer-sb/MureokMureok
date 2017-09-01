package com.example.totoroto.mureok.PlantTip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.totoroto.mureok.Data.TipData;
import com.example.totoroto.mureok.Data.TipDetailData;
import com.example.totoroto.mureok.R;
import com.example.totoroto.mureok.RecyclerItemClickListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TipActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "TipPlant";
    public static final String INTENT_STR = "TipDetailData";

    private final String API_KEY = "20170726ADJPJHDALH0FXS5HK84T7G";
    private final String TAG_FRT = "frtlzrInfo";
    private final String TAG_TEMPERATURE = "grwhTpCode";
    private final String TAG_HYDRO = "hdCode";
    private final String TAG_PRPG = "prpgtEraInfo";
    private final String TAG_SOIL = "soilInfo";
    private final String TAG_WATER_SPRING = "watercycleSprngCode";
    private final String TAG_WATER_SUMMER = "watercycleSummerCode";
    private final String TAG_WATER_AUTUMN = "watercycleAutumnCode";
    private final String TAG_WATER_WINTER = "watercycleWinterCode";

    private final int numOfRows = 216;
    private ArrayList<TipData> tempLists;
    private ArrayList<TipData> mTipDatas;
    private ArrayList<TipData> filteredLists;
    private RecyclerView recyclerTip;
    private TipAdapter tipAdapter;
    private LinearLayoutManager layoutManager;
    private Button btnBack;
    private EditText etTipSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        init();
        aboutRecycler();
        aboutTipDetail();
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPlantListData();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        aboutMapListData();
                    }
                });
            }
        }).start();

        aboutEditTextSearch();
    }

    private void aboutEditTextSearch() {
        etTipSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filteredLists.clear();
                s = s.toString().toLowerCase();

                for(int i=0; i<mTipDatas.size(); i++){
                    String str = mTipDatas.get(i).getpRealName(); //식물 이름
                    if(str.contains(s)){
                        filteredLists.add(mTipDatas.get(i));
                    }
                }
                tipAdapter.setTipDatas(filteredLists);
                tipAdapter.notifyDataSetChanged();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void aboutTipDetail() {

        recyclerTip.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                recyclerTip, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                final int plantCode;
                if(filteredLists.size() != 0) {
                    plantCode = filteredLists.get(position).getpCode();
                    Log.d(TAG, "pCode" + plantCode + "|position" + position);
                }else{
                    plantCode = mTipDatas.get(position).getpCode();
                    Log.d(TAG, "pCode" + plantCode + "|position" + position);
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getPlantDetailData(plantCode, position);
                    }
                }).start();

            }
        }));
    }

    private void getPlantDetailData(int pCode, int position) {
        //1.
        String queryUrl = "http://api.nongsaro.go.kr/service/garden/gardenDtl"
                + "?apiKey=" + API_KEY + "&cntntsNo=" + pCode;

        try {
            URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();
            TipDetailData tipDetailData = new TipDetailData();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //태그 이름 얻어오기

                        if (tag.equals("item")) ;
                        else if (tag.equals(TAG_FRT)) {
                            xpp.next();
                            tipDetailData.frt = xpp.getText();
                            Log.d(TAG, tipDetailData.frt);
                        }else if (tag.equals(TAG_TEMPERATURE)) {
                            xpp.next();
                            tipDetailData.temperature = xpp.getText();
                        }else if (tag.equals(TAG_HYDRO)) {
                            xpp.next();
                            tipDetailData.hydro = xpp.getText();
                        }else if (tag.equals(TAG_PRPG)) {
                            xpp.next();
                            tipDetailData.prpg = xpp.getText();
                        }else if (tag.equals(TAG_SOIL)) {
                            xpp.next();
                            tipDetailData.soil = xpp.getText();
                        }else if (tag.equals(TAG_WATER_SPRING)) {
                            xpp.next();
                            tipDetailData.waterSpring = xpp.getText();
                        }else if (tag.equals(TAG_WATER_SUMMER)) {
                            xpp.next();
                            tipDetailData.waterSummer = xpp.getText();
                        }else if (tag.equals(TAG_WATER_AUTUMN)) {
                            xpp.next();
                            tipDetailData.waterAutumn = xpp.getText();
                        }else if (tag.equals(TAG_WATER_WINTER)) {
                            xpp.next();
                            tipDetailData.waterWinter = xpp.getText();
                        }
                        break;
                }
                eventType = xpp.next();

                if(tipDetailData.waterWinter != null) {
                    eventType = XmlPullParser.END_DOCUMENT; //while 종료


                    if(filteredLists.size() == 0) {
                        //detail 액티비티로 객체 보내기
                        Intent intent = new Intent(getApplicationContext(), TipDetailActivity.class);
                        intent.putExtra(INTENT_STR, tipDetailData);
                        intent.putExtra("plantImage", mTipDatas.get(position).getpImage());
                        intent.putExtra("plantName", mTipDatas.get(position).getpRealName());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), TipDetailActivity.class);
                        intent.putExtra(INTENT_STR, tipDetailData);
                        intent.putExtra("plantImage", filteredLists.get(position).getpImage());
                        intent.putExtra("plantName", filteredLists.get(position).getpRealName());
                        startActivity(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        recyclerTip = (RecyclerView)findViewById(R.id.recyclerTip);
        btnBack = (Button)findViewById(R.id.btnBack_tip);
        etTipSearch = (EditText)findViewById(R.id.etTipSearch);

        btnBack.setOnClickListener(this);
        mTipDatas = new ArrayList<>();
        filteredLists = new ArrayList<>();
    }

    private void aboutRecycler() {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTip.setHasFixedSize(true);
        recyclerTip.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDeco = new DividerItemDecoration(
                recyclerTip.getContext(), layoutManager.getOrientation());
        recyclerTip.addItemDecoration(mDividerItemDeco);

        tipAdapter = new TipAdapter();
        tipAdapter.setTipDatas(mTipDatas);
        recyclerTip.setAdapter(tipAdapter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack_tip:
                finish();
                break;
        }
    }

    public void getPlantListData() {
        tempLists = new ArrayList<>();
        int pNo = 0;
        String pName = "";
        String pImage = "";

        String queryUrl = "http://api.nongsaro.go.kr/service/garden/gardenList"
                + "?apiKey=" + API_KEY + "&numOfRows=" + numOfRows;

        try {
            URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;
            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();    //태그 이름 얻어오기

                        if (tag.equals("item")) ;
                        else if (tag.equals("cntntsNo")) { //식물 넘버
                            xpp.next();
                            pNo = Integer.valueOf(xpp.getText());
                        } else if (tag.equals("cntntsSj")) { //식물명
                            xpp.next();
                            pName = xpp.getText();
                        }else if(tag.equals("rtnStreFileNm")){ //이미지 파일 경로
                            xpp.next();
                            pImage = xpp.getText();
                            TipData data = new TipData(pNo, pName, pImage);
                            tempLists.add(data);
                        }

                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//
    }

    private void aboutMapListData() {
        for(int i=0; i<tempLists.size(); i++){
            TipData tData = new TipData(tempLists.get(i).getpCode(), tempLists.get(i).getpRealName(), tempLists.get(i).getpImage());
            mTipDatas.add(tData);
        }

        tipAdapter.notifyDataSetChanged();
    }
}

