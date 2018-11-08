package com.kmu.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CalenderAdapter extends BaseAdapter {
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

class ViewHolder{
    TextView text;
}
