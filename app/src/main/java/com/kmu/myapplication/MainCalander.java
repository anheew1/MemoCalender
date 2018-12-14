package com.kmu.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainCalander extends AppCompatActivity {
    static int ADD_EVENT = 0;
    private TextView ymdate;
    private Date selectedDate;
    private SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("yyyy 년 MM 월",Locale.KOREA);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
    private CompactCalendarView compactCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calander);

        // Constructing
        selectedDate = new Date();

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
        compactCalendarView.setCurrentDayIndicatorStyle(CompactCalendarView.FILL_LARGE_INDICATOR);
        compactCalendarView.setCurrentDayBackgroundColor(CompactCalendarView.AUTOFILL_TYPE_NONE);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                ymdate.setText(simpleMonthFormat.format(dateClicked));
                selectedDate = dateClicked;
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == ADD_EVENT){
            if(resultCode == RESULT_OK){
                createEvent(data.getExtras());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_addEvent){
            Intent intent = new Intent(this,AddEvent.class);

            String dateString = simpleDateFormat.format(selectedDate);
            intent.putExtra("selectedDate",dateString);
            startActivityForResult(intent,ADD_EVENT);
        }
        return super.onOptionsItemSelected(item);
    }

    private void createEvent(Bundle extras){
        String eventName = extras.getString("eventName");
        if(eventName == null) eventName = "";
        String eventDate = extras.getString("eventDate");

        String eventMemo = extras.getString("eventMemo");
        if(eventMemo == null) eventMemo = "";
        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            Event newEvent = new Event(Color.RED,newdate.getTime(),extras);
            compactCalendarView.addEvent(newEvent);
        }catch (ParseException e){
            e.printStackTrace();
        }

        Log.d("eventmsg",eventName+","+eventDate+","+eventMemo);

    }
}
