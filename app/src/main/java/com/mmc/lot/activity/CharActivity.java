package com.mmc.lot.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mmc.lot.IotApplication;
import com.mmc.lot.R;
import com.mmc.lot.bean.BodyData;
import com.mmc.lot.bean.TempBean;
import com.mmc.lot.net.Repository;
import com.mmc.lot.util.ChartView;
import com.mmc.lot.util.DataConvertUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static java.util.Arrays.asList;

/**
 * @author  by zhangzd on 2018/4/1
 */

public class CharActivity extends AppCompatActivity {

    private ChartView chartView;
    private List<String> data = new ArrayList<>();

    //x轴坐标对应的数据
    List<String> xValue1 = new ArrayList<>();
    List<String> xString1 = new ArrayList<>();
    //y轴坐标对应的数据
    List<Integer> yValue1 = new ArrayList<>();
    //折线对应的数据
    Map<String, Integer> value1 = new HashMap<>();
    Map<String, String> yString1 = new HashMap<>();

    List<String> yDegreeString = new ArrayList<String>(asList("严重", "重度", "中度", "轻度", "良", "优"));
    List<Integer> yDegreeValue = new ArrayList<>(asList(100, 80, 70, 50, 20, 0));
    List<Integer> pointColor = new ArrayList<>(asList(Color.WHITE, Color.WHITE, Color.WHITE));

    /**
     * 模拟的假数据
     */
    private List<Integer> testData, rankData, allData;

    private List<BodyData> bodyDatas = new ArrayList<>();
    private BodyData bodyData;
    private List<String> weight = new ArrayList<>();
    private List<Long> current_time = new ArrayList<>();
    private int width;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_chart);

        ImageView ivBack = (ImageView) findViewById(R.id.iv_title_bar_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title_bar_title);
        title.setText("温度曲线");

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();

        chartView = (ChartView) findViewById(R.id.chartview);
        addTestData();

        setChartView(bodyData);

        TempBean tempBean = (TempBean) getIntent().getSerializableExtra("tempBean");
        Log.d("zzDebug", tempBean.toString()+"");
        addRealData(tempBean);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setChartView(bodyData);
            }
        }, 100);


    }

    private void addTestData() {
        bodyData = new BodyData();
        bodyData.setName("zzd");
        bodyData.setID(1);

        weight.add("25");
        weight.add("35");
        weight.add("45");
        weight.add("55");
        weight.add("65");
        weight.add("30");
        weight.add("40");
        weight.add("50");
        bodyData.setWeight(weight);
        long time = System.currentTimeMillis();

        current_time.add(time - 75000);
        current_time.add(time - 65000);
        current_time.add(time - 55000);
        current_time.add(time - 45000);
        current_time.add(time - 35000);
        current_time.add(time - 25000);
        current_time.add(time - 15000);
        current_time.add(time - 5000);

        bodyData.setCurrent_time(current_time);
    }

    private void addRealData(TempBean bean) {
        bodyData.getWeight().clear();
        bodyData.getCurrent_time().clear();

        List<TempBean.OBean> beans = bean.getO();
        TempBean.OBean oBean = beans.get(0);
        long upTime = oBean.getUPTIME();
        String temp = oBean.getTEMP().substring(1, oBean.getTEMP().length() - 1);
        Log.d("zzDebug", "temp:" + temp);
        String[] array = temp.split(",");

        int length = array.length;
        for (String tempData : array) {
            float data = Float.valueOf(tempData);
            BigDecimal b1 = new BigDecimal(data);
            BigDecimal b2 = new BigDecimal(50.00);
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            weight.add(String.valueOf(decimalFormat.format(b1.add(b2).doubleValue())));
        }
        bodyData.setWeight(weight);

        for (int i = 0; i < length; i++) {
            long space = i * 60 * 1000;
            current_time.add(upTime / 1000);
        }
        bodyData.setCurrent_time(current_time);
    }

    private String timeStamp2Date(String currentMission) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(System.currentTimeMillis());
    }

    private void setChartView(BodyData bodyDatas) {
        clearData();
        initChartview_c(weight, chartView, bodyDatas, yValue1,
                xString1, xValue1, value1, yString1);
    }

    private void clearData() {
        xString1.clear();
        xValue1.clear();
        yValue1.clear();
        value1.clear();
    }


    private void initChartview_c(List<?> list, ChartView chartView, BodyData bodyData, List<Integer> yValue,
                                 List<String> xString, List<String> xValue, Map<String, Integer> value, Map<String, String> yString) {

        for (int j = 0; j < 10; j++) {
            //纵坐标轴显示文字
            yValue.add(j * 20);
        }
        int maxY = 0, currentY;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("")) {
                currentY = 0;
            } else {
                currentY = (int) Float.parseFloat((String) list.get(i));
            }
            if (currentY > maxY) {
                maxY = currentY;
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String currentTime = DataConvertUtil.getFormatFromDateFormat(
                    DataConvertUtil.LongToCalendar(bodyData.getCurrent_time().get(i)));
            xString.add(currentTime);
            xValue.add((i) + "");
            //横纵坐标 点
            int y;
            String yS;
            if (list.get(i).equals("")) {
                y = 0;
                yS = "0";
            } else {
                if (maxY == 0) {
                    maxY = 1;
                }
                y = (int) Float.parseFloat((String) list.get(i)) * 100 / maxY;
                yS = (String) list.get(i);
            }
            Log.e("-----77777", "y:" + y);
            yString.put((i) + "", yS);
            value.put((i) + "", y);//60--240
        }

        chartView.setValue(value, xValue, yValue, xString, yDegreeValue, yDegreeString, pointColor, yString);

        chartView.setInterval(width / 9);
        chartView.initLayout();
        chartView.setCurrentPosition(list.size() - 9);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
