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
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainCalander extends AppCompatActivity {
    static int ADD_EVENT = 0;
    static int SHOW_EVENT_INFO = 1;
    static int RESULT_REMOVE_EVENT = 101;
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
        compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);
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
                showRecyclerEvents(firstDayOfNewMonth);
            }
        });



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



            try{
                Date addDate = simpleDateFormat.parse(date);

                Bundle bundle = new Bundle();
                bundle.putString("eventName",name);
                bundle.putString("eventDate",date);
                bundle.putString("eventMemo",memo);
                bundle.putInt("eventId",id);
                Event event = new Event(Color.RED,addDate.getTime(),bundle);
                compactCalendarView.addEvent(event);

            }catch (ParseException e){
                e.printStackTrace();
            }

            dblength++;
        }
        Log.d("dblength",String.valueOf(dblength));

        showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());

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
                updateEvent(data.getExtras());
            }
            else if(resultCode == RESULT_REMOVE_EVENT){
                deleteEvent(data.getExtras());
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
        int eventId= -1;

        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            // insert to DB
            db.execSQL("INSERT INTO events VALUES (null, '"+ eventName+"','"+eventDate+"','"+eventMemo+"');");
            cursor = db.rawQuery("SELECT LAST_INSERT_ROWID();",null);
            if(cursor.moveToFirst())
            eventId = cursor.getInt(0);

            extras.putInt("eventId",eventId);
            Log.d("eventID is",String.valueOf(eventId));

            Event newEvent = new Event(Color.RED,newdate.getTime(),extras);
            compactCalendarView.addEvent(newEvent);

            showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());
        }catch (ParseException e){
            e.printStackTrace();
        }

        Log.d("eventmsg",eventName+","+eventDate+","+eventMemo);

    }
    public void updateEvent(Bundle extras){
        int eventId = extras.getInt("eventId");
        String eventName = extras.getString("eventName");
        String eventDate = extras.getString("eventDate");
        String eventMemo = extras.getString("eventMemo");
        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            db.execSQL("UPDATE events SET name = '"+eventName+"', date = '"+eventDate+
                    "', memo ='"+eventMemo+"' WHERE id ='"+eventId+"';");
            for ( Event event : compactCalendarView.getEvents(newdate)){
                Bundle eventData = (Bundle) event.getData();
                if(eventData.getInt("eventId") == eventId){
                    compactCalendarView.removeEvent(event);
                    extras.putString("eventName",eventName);
                    extras.putString("eventDate",eventDate);
                    extras.putString("eventMemo",eventMemo);
                    Event newEvent = new Event(Color.RED,newdate.getTime(),extras);
                    compactCalendarView.addEvent(newEvent);
                    showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());

                    Log.d("updateEvent","is Success, id: "+eventId);
                    break;
                }
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }
    public void deleteEvent(Bundle extras){
        int eventId = extras.getInt("eventId");
        String eventDate = extras.getString("eventDate");
        try {
            Date newdate = simpleDateFormat.parse(eventDate);
            db.execSQL("DELETE FROM events WHERE id=" + eventId + " ;");
            for (Event event : compactCalendarView.getEvents(newdate)) {
                Bundle eventData = (Bundle) event.getData();
                if(eventData.getInt("eventId") == eventId){
                    compactCalendarView.removeEvent(event);
                    showRecyclerEvents(compactCalendarView.getFirstDayOfCurrentMonth());
                    Log.d("delEvent","is Success, id: "+eventId);
                    break;
                }
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }
    public void showRecyclerEvents(Date date){
        List<Event> events =compactCalendarView.getEventsForMonth(date);
        ArrayList<EventData> dataArrayList = new ArrayList<>();
        for(Event event: events){
            Bundle extras = (Bundle) event.getData();
            String sname = extras.getString("eventName");
            String sdate = extras.getString("eventDate");
            String smemo = extras.getString("eventMemo");
            int sid = extras.getInt("eventId");

            EventData eventData = new EventData(sname,sdate,smemo,sid);
            dataArrayList.add(eventData);
        }
        horizontalLayout = (LinearLayout) findViewById(R.id.horizontal_event_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(horizontalLayout.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        horizontalAdapter = new HorizontalAdapter(dataArrayList,this);

        horizontalView = (RecyclerView) findViewById(R.id.horizontal_event_view);
        horizontalView.setLayoutManager(linearLayoutManager);
        horizontalView.setAdapter(horizontalAdapter);

    }

}
