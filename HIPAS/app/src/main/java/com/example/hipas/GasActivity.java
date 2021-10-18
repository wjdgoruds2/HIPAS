package com.example.hipas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import java.util.ArrayList;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


public class GasActivity extends AppCompatActivity {
    LineChart chart;
    int X_RANGE = 50;
    int DATA_RANGE = 30;
    ArrayList<Entry> xVal;
    LineDataSet setXcomp;
    ArrayList<String> xVals;
    ArrayList<ILineDataSet> lineDataSets;
    LineData lineData;

    private void init(){
        chart = (LineChart)findViewById(R.id.chart);
        chartInit();
    }

    private void chartInit(){
        chart.setAutoScaleMinMaxEnabled(true);
        xVal = new ArrayList<Entry>();
        setXcomp = new LineDataSet(xVal, "X");
        setXcomp.setColor(Color.RED);
        setXcomp.setDrawValues(false);
        setXcomp.setDrawCircles(false);
        setXcomp.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSets = new ArrayList<ILineDataSet>();
        lineDataSets.add(setXcomp);

        xVals = new ArrayList<String>();
        for(int i=0;i<X_RANGE;i++){
            xVals.add("");
        }
        lineData = new LineData(xVals,lineDataSets);
        chart.setData(lineData);
        chart.invalidate();
    }

    public void chartUpdate(int x){
        if(xVal.size()>DATA_RANGE){
            xVal.remove(0);
            for(int i=0;i<DATA_RANGE;i++){
                xVal.get(i).setXIndex(i);
            }
        }
        xVal.add(new Entry(x,xVal.size()));
        setXcomp.notifyDataSetChanged();
        chart.notifyDataSetChanged();
        chart.invalidate();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what ==0){
                int a =0;
                //a : 새로운 데이터
                a = (int)(Math.random()*100);
                chartUpdate(a);
            }
        }
    };

    class MyThread extends Thread{
        @Override
        public  void run(){
            while(true){
                handler.sendEmptyMessage(0);
                try{
                    //업데이트
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    private void threadStart(){
        MyThread thread = new MyThread();
        thread.setDaemon(true);
        thread.start();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        threadStart();
    }

}
