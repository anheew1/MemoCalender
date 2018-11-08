package com.kmu.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class FragCalander extends Fragment {
    private int year ,month, date, position;
    private View rootView;
    private int COUNT = 200;
    static  FragCalander newInstance(int position){
        FragCalander fc = new FragCalander();
        Bundle args = new Bundle();
        args.putInt("position",position);
        fc.setArguments(args);
        return fc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt("position"): 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main_calander,container,false);

        GregorianCalendar cal = new GregorianCalendar(Locale.KOREA);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        date = cal.get(Calendar.DATE);

        if(position > 0 && position < 100){
            month += position;
            while(month > 11) {
                month-=12;
                year+=1;
            }
        }
        else if(position >100 ){
            month -= COUNT-position;
            while(month <0 ){
                month+=12;
                year-=1;
            }
        }



        cal.set(year,month-1,date);
        int lastMonthDay = cal.getActualMaximum(Calendar.DATE);
        cal.set(year,month,date);

        int startDay =cal.get(Calendar.DAY_OF_WEEK); // 월 시작 요일
        int lastDay = cal.getActualMaximum(Calendar.DATE); // 월 마지막 날짜

        GridView gridview = (GridView) rootView.findViewById(R.id.calendar);
        TextView ymdate = (TextView) getActivity().findViewById(R.id.ymdate);
        String symdate=year+ "년 " +(month+1)+"월";

        ymdate.setText(symdate);
        ArrayList<String> dayList = new ArrayList<>();

        for (int i = lastMonthDay; i > lastMonthDay-startDay+1; i--) {
            String day = String.valueOf(i);
            dayList.add(0,day);
        }
        for (int i = 1; i <= lastDay; i++) {
            String day = String.valueOf(i);
            dayList.add(day);
        }
        for(int i=1;i <((lastDay+startDay)/7) + 4;i++){
            String day = String.valueOf(i);
            dayList.add(day);
        }
        CalenderAdapter calenderAdapter = new CalenderAdapter(getActivity().getApplicationContext(),dayList);
        gridview.setAdapter(calenderAdapter);
        return rootView;
    }


}
