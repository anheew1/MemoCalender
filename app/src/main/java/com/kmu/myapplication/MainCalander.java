package com.kmu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
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
    static int SHOW_EVENT_INFO = 1;

    static int RESULT_REMOVE_EVENT = 101;
    static int RESULT_MODIFY_EVENT = 102;
    private TextView ymdate;
    private Date selectedDate;
    private SimpleDateFormat simpleMonthFormat = new SimpleDateFormat("yyyy 년 MM 월",Locale.KOREA);
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
    private CompactCalendarView compactCalendarView;
    private LinearLayout horizontalLayout;
    private RecyclerView horizontalView;
    private HorizontalAdapter horizontalAdapter;
    private DBHelper helper;
    private Cursor cursor;
    SQLiteDatabase db;
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

        ArrayList<EventData> dataArrayList = new ArrayList<>();

        //DATABASE data.add
        helper = new DBHelper(this);
        db = helper.getWritableDatabase();

        cursor = db.rawQuery("SELECT *FROM events",null);
        int dblength=0;
        while ( cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String date = cursor.getString(2);
            String memo = cursor.getString(3);


            EventData eventData = new EventData(name,date,memo,id);
            dataArrayList.add(eventData);
            dblength++;
        }
        Log.d("dblength",String.valueOf(dblength));


        horizontalLayout = (LinearLayout) findViewById(R.id.horizontal_event_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(horizontalLayout.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        horizontalAdapter = new HorizontalAdapter(dataArrayList,getApplicationContext());

        horizontalView = (RecyclerView) findViewById(R.id.horizontal_event_view);
        horizontalView.setLayoutManager(linearLayoutManager);
        horizontalView.setAdapter(horizontalAdapter);

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
        if(requestCode == SHOW_EVENT_INFO){
            if(resultCode == RESULT_OK){

            }
            else if(resultCode == RESULT_REMOVE_EVENT){

            }
            else if(resultCode == RESULT_MODIFY_EVENT){

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

    public void createEvent(Bundle extras){
        String eventName = extras.getString("eventName");
        String eventDate = extras.getString("eventDate");
        String eventMemo = extras.getString("eventMemo");

        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            Event newEvent = new Event(Color.RED,newdate.getTime(),extras);
            compactCalendarView.addEvent(newEvent);

            // insert to DB
            db.execSQL("INSERT INTO events VALUES (null, '"+ eventName+"','"+eventDate+"','"+eventMemo+"');");
            Log.d("eventtodb",eventName+","+eventDate+","+eventMemo);
        }catch (ParseException e){
            e.printStackTrace();
        }

        Log.d("eventmsg",eventName+","+eventDate+","+eventMemo);

    }
    public void showEventInfo(int position){

    }
}
