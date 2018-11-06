package com.kmu.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.BaseAdapter;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainCalander extends AppCompatActivity {
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

        GridView mainCalender = findViewById(R.id.calendar);
        ArrayList<String> weekdayList = new ArrayList<String>();
        weekdayList.add("일");
        weekdayList.add("월");
        weekdayList.add("화");
        weekdayList.add("수");
        weekdayList.add("목");
        weekdayList.add("금");
        weekdayList.add("토");

        ArrayList<String> dayList = new ArrayList<String>();

        Calendar cal = new GregorianCalendar(Locale.KOREA);
        int startDay = cal.get(Calendar.DAY_OF_WEEK); // 월 시작 요
        int lastDay = cal.getActualMaximum(Calendar.DATE); // 월 마지막 날짜

        TextView ymdate = (TextView) findViewById(R.id.ymdate);
        String symdate=cal.get(Calendar.YEAR)+ "년 " +(cal.get(Calendar.MONTH)+1)+"월";
        ymdate.setText(symdate);

        for (int i = 0; i <= startDay; i++) {
            dayList.add(" ");
        }
        for (int i = 1; i <= lastDay; i++) {
            String day = String.valueOf(i);
            dayList.add(day);
        }
        CalenderAdapter calenderAdapter = new CalenderAdapter(getApplicationContext(),dayList);
        mainCalender.setAdapter(calenderAdapter);
        GridView weekday = findViewById(R.id.weekday);
        WeekdayAdapter weekdayAdapter = new WeekdayAdapter(getApplicationContext(),weekdayList);
        weekday.setAdapter(weekdayAdapter);


    }
    public class CalenderAdapter extends BaseAdapter{
        private List list;
        private Context context;
        private LayoutInflater inflater;
        public CalenderAdapter(Context context, List<String> list) {
            this.list =list;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null){
                convertView = inflater.inflate(R.layout.day,parent,false);
                holder = new ViewHolder();
                holder.text = (TextView)convertView.findViewById(R.id.daylayout);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(String.valueOf(getItem(position)));

            if(position%7==0){
                holder.text.setTextColor(Color.RED);
            }
            else if(position%7 == 6){
                holder.text.setTextColor(Color.BLUE);
            }
            else {
                holder.text.setTextColor(Color.GRAY);
            }
            Calendar calendar = new GregorianCalendar(Locale.KOREA);
            Integer today = calendar.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.text.setTextColor(Color.BLACK);
            }
            return convertView;
}
    }
    protected class WeekdayAdapter extends CalenderAdapter{
        private List list;
        private Context context;
        private LayoutInflater inflater;
        public WeekdayAdapter(Context context, List<String> list) {
            super(context,list);
            this.list =list;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null){
                convertView = inflater.inflate(R.layout.weekday,parent,false);
                holder = new ViewHolder();
                holder.text = (TextView)convertView.findViewById(R.id.weekdaylayout);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text.setText(String.valueOf(getItem(position)));

            return convertView;
        }
    }
private class ViewHolder{
    TextView text;
}







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
}
