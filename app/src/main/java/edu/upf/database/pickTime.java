package edu.upf.database;
import java.util.Calendar;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;



public class pickTime extends Activity {
    private TimePicker timePicker1;
    private TextView time;
    private Calendar calendar;
    private String format = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttime);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker);
        final Intent intent_in = getIntent();
        final Button button = (Button) findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(View v) {
                Intent intent = new Intent();
                int min = timePicker1.getMinute();
                String smin = "";
                if(min < 10){
                    smin = "0" + min;
                } else {
                    smin = String.valueOf(min);
                }
                intent.putExtra("Time", timePicker1.getHour() + ":" + smin);
                intent.putExtra("button_id", intent_in.getIntExtra("button_id", 0));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setTime(View view) {
        int hour = timePicker1.getHour();
        int min = timePicker1.getMinute();

        showTime(hour, min);
    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

        time.setText(new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format));
    }


}