package com.kmu.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class WeekdayAdapter extends CalenderAdapter{
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
