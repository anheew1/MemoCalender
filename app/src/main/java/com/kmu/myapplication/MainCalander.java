package com.kmu.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainCalander extends AppCompatActivity {
    private int COUNT = 200;
    private TextView ymdate;
    private SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("yyyy 년 MM 월",Locale.KOREA);
    private CompactCalendarView compactCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calander);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_dehaze_black_18dp);
        getSupportActionBar().setTitle("");
        ymdate = findViewById(R.id.ymdate);

        compactCalendarView =  (CompactCalendarView) findViewById(R.id.customcalendar_view);
        compactCalendarView.setLocale(TimeZone.getDefault(),Locale.KOREA);
        compactCalendarView.setFirstDayOfWeek(1);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                ymdate.setText(simpleMonthFormat.format(dateClicked));
                Log.d("dateclick",dateClicked+"");
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                ymdate.setText(simpleMonthFormat.format(firstDayOfNewMonth));
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        ymdate.setText(simpleMonthFormat.format(compactCalendarView.getFirstDayOfCurrentMonth()));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
}
