package com.kmu.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddEvent extends Activity {
    String strDate ;

    int selectedYear;
    int selectedMonth;
    int selectedDay;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_addevent);

        Intent intent = getIntent();
        strDate = intent.getExtras().getString("selectedDate"); // yyyy-mm-dd
        String[] arrDate =  strDate.split("-");
        selectedYear = Integer.parseInt(arrDate[0]);
        selectedMonth = Integer.parseInt(arrDate[1]);
        selectedDay = Integer.parseInt(arrDate[2]);



        EditText eventDate = findViewById(R.id.edit_eventDate);
        eventDate.setText(strDate);
        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddEvent.this,dateSetListener,selectedYear,selectedMonth-1,selectedDay).show();
            }
        });

        Button confirmBtn = findViewById(R.id.btn_eventAddConfirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eventName = findViewById(R.id.edit_eventName);
                if(eventName.getText().toString().equals("")){
                    Toast noname = Toast.makeText(getApplicationContext(),"Write Event Name!",Toast.LENGTH_SHORT);
                    noname.show();
                    return;
                }
                Intent eventIntent = new Intent(AddEvent.this, MainCalander.class);
                // add eventName
                eventIntent.putExtra("eventName",eventName.getText().toString());
                Log.d("name",eventName.getText().toString());
                //add eventDate
                eventIntent.putExtra("eventDate",strDate);
                //add eventMemo
                EditText eventMemo = findViewById(R.id.edit_eventMemo);
                eventIntent.putExtra("eventMemo",eventMemo.getText().toString());

                setResult(RESULT_OK,eventIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            strDate = String.format("%d-%d-%d",year,month+1,dayOfMonth);
            EditText eventDate = (EditText)findViewById(R.id.edit_eventDate);
            eventDate.setText(strDate);
        }
    };
}
