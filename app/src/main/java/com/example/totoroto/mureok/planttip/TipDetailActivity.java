package com.example.totoroto.mureok.planttip;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.totoroto.mureok.data.TipDetailData;
import com.example.totoroto.mureok.R;

public class TipDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String INTENT_STR = "TipDetailData";

    private ImageView ivTip_Img;
    private TextView tvPname;
    private TextView tvTip_frt;
    private TextView tvTip_temperature;
    private TextView tvTip_hydro;
    private TextView tvTip_prpg;
    private TextView tvTip_soil;
    private TextView tvTip_waterSpring;
    private TextView tvTip_waterSummer;
    private TextView tvTip_waterAutumn;
    private TextView tvTip_waterWinter;
    private Button btnTipClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_detail);

        init();
        setData();
    }

    private void setData() {
        Bundle bundle = getIntent().getExtras();
        TipDetailData detailData = bundle.getParcelable(INTENT_STR);
        String imagePath = bundle.getString("plantImage");
        String pRealName = bundle.getString("plantName");
        tvPname.setText(pRealName);

       try {
           String path = imagePath.split("\\|")[0];
           String realPath = "http://www.nongsaro.go.kr/cms_contents/301/"+path;

           Glide.with(getApplicationContext())
                   .load(Uri.parse(realPath))
                   .centerCrop()
                   .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                   .into(ivTip_Img);
       }catch (Exception e){
           e.printStackTrace();
       }

        try {
            tvTip_frt.setText(detailData.getFrt());

            switch (detailData.getTemperature()) {
                case "082001":
                    tvTip_temperature.setText(R.string.temperature1);
                    break;
                case "082002":
                    tvTip_temperature.setText(R.string.temperature2);
                    break;
                case "082003":
                    tvTip_temperature.setText(R.string.temperature3);
                    break;
                case "082004":
                    tvTip_temperature.setText(R.string.temperature4);
                    break;
                default:
                    tvTip_temperature.setText(R.string.notFound_info);
            }
            switch (detailData.getHydro()) {
                case "083001":
                    tvTip_hydro.setText(R.string.hydro1);
                    break;
                case "083002":
                    tvTip_hydro.setText(R.string.hydro2);
                    break;
                case "083003":
                    tvTip_hydro.setText(R.string.hydro3);
                    break;
                default:
                    tvTip_hydro.setText(R.string.notFound_info);
            }
            tvTip_prpg.setText(detailData.getPrpg());
            tvTip_soil.setText(detailData.getSoil());

            switch (detailData.getWaterSpring()) {
                case "053001":
                    tvTip_waterSpring.setText(R.string.water1);
                    break;
                case "053002":
                    tvTip_waterSpring.setText(R.string.water2);
                    break;
                case "053003":
                    tvTip_waterSpring.setText(R.string.water3);
                    break;
                case "053004":
                    tvTip_waterSpring.setText(R.string.water4);
                    break;
                default:
                    tvTip_waterSpring.setText(R.string.notFound_info);
            }

            switch (detailData.getWaterSummer()) {
                case "053001":
                    tvTip_waterSummer.setText(R.string.water1);
                    break;
                case "053002":
                    tvTip_waterSummer.setText(R.string.water2);
                    break;
                case "053003":
                    tvTip_waterSummer.setText(R.string.water3);
                    break;
                case "053004":
                    tvTip_waterSummer.setText(R.string.water4);
                    break;
                default:
                    tvTip_waterSummer.setText(R.string.notFound_info);
            }

            switch (detailData.getWaterAutumn()) {
                case "053001":
                    tvTip_waterAutumn.setText(R.string.water1);
                    break;
                case "053002":
                    tvTip_waterAutumn.setText(R.string.water2);
                    break;
                case "053003":
                    tvTip_waterAutumn.setText(R.string.water3);
                    break;
                case "053004":
                    tvTip_waterAutumn.setText(R.string.water4);
                    break;
                default:
                    tvTip_waterAutumn.setText(R.string.notFound_info);
            }

            switch (detailData.getWaterWinter()) {
                case "053001":
                    tvTip_waterWinter.setText(R.string.water1);
                    break;
                case "053002":
                    tvTip_waterWinter.setText(R.string.water2);
                    break;
                case "053003":
                    tvTip_waterWinter.setText(R.string.water3);
                    break;
                case "053004":
                    tvTip_waterWinter.setText(R.string.water4);
                    break;
                default:
                    tvTip_waterWinter.setText(R.string.notFound_info);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void init() {
        ivTip_Img = (ImageView)findViewById(R.id.ivTip_img);
        tvPname = (TextView)findViewById(R.id.tvPname_tip_detail);
        tvTip_frt = (TextView) findViewById(R.id.tvTip_frt);
        tvTip_temperature = (TextView)findViewById(R.id.tvTip_temperature);
        tvTip_hydro = (TextView)findViewById(R.id.tvTip_hydro);
        tvTip_prpg = (TextView)findViewById(R.id.tvTip_prpg);
        tvTip_soil = (TextView)findViewById(R.id.tvTip_soil);
        tvTip_waterSpring = (TextView)findViewById(R.id.tvTip_waterSpring);
        tvTip_waterSummer = (TextView)findViewById(R.id.tvTip_waterSummer);
        tvTip_waterAutumn = (TextView)findViewById(R.id.tvTip_waterAutumn);
        tvTip_waterWinter = (TextView)findViewById(R.id.tvTip_waterWinter);
        btnTipClose = (Button)findViewById(R.id.btnTipClose);

        btnTipClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTipClose:
                finish();
                break;
        }
    }
}
