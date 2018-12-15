package com.kmu.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder>{
    private ArrayList<EventData> eventDataArrayList;
    public HorizontalAdapter(ArrayList<EventData> list){
        eventDataArrayList = list;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_horizontal_event,viewGroup,false);

        HorizontalViewHolder viewHolder = new HorizontalViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        EventData data = eventDataArrayList.get(position);

        holder.date.setText(data.getDate());
        holder.name.setText(data.getName());
        holder.memo.setText(data.getMemo());
    }


    @Override
    public int getItemCount() {
        return eventDataArrayList.size();
    }
}
class HorizontalViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView date;
    public TextView memo;
    HorizontalViewHolder(View view){
        super(view);

    }
}
