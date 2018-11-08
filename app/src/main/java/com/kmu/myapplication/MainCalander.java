package com.kmu.myapplication;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainCalander extends AppCompatActivity {
    private int COUNT = 200;
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

        ArrayList<String> weekdayList = new ArrayList<String>();
        weekdayList.add("일");
        weekdayList.add("월");
        weekdayList.add("화");
        weekdayList.add("수");
        weekdayList.add("목");
        weekdayList.add("금");
        weekdayList.add("토");

        // Calender is add by FragCalander

        GridView weekday = findViewById(R.id.weekday);
        WeekdayAdapter weekdayAdapter = new WeekdayAdapter(getApplicationContext(),weekdayList);
        weekday.setAdapter(weekdayAdapter);

        final ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(COUNT);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position < COUNT){
                    viewPager.setCurrentItem(position+COUNT,false);
                }
                else if (position >= COUNT*2){
                    viewPager.setCurrentItem(position-COUNT,false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
}
