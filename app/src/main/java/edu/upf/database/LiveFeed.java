package edu.upf.database;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rober on 15/06/2017.
 */

public class LiveFeed extends Activity {
    private static String[][] pets;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intentRFID = new Intent(LiveFeed.this, MyService.class);
            stopService(intentRFID);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livefeed);

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        pets = mDbHelper.readAll();

        MyReceiver myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);
        Intent intentRFID = new Intent(LiveFeed.this, MyService.class);
        intentRFID.putExtra("URL", "http://localhost:3161");
        startService(intentRFID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("AniFeed - Live Feed");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intentRFID = new Intent(LiveFeed.this, MyService.class);
                stopService(intentRFID);
                finish();
                startActivity(new Intent(LiveFeed.this, MainActivity.class));

            }
        });
        TextView tv = (TextView) findViewById(R.id.sampleText);
        if (pets.length <= 0) {
            tv.append("\n");
            tv.setText("No pets found :(");
        } else {
            String[] petnames = new String[pets.length];
            for (int i = 0; i < pets.length; i++) {
                petnames[i] = pets[i][1] + " (" + pets[i][12] + "g eaten)";
            }


            ListView listview = (ListView) findViewById(R.id.listView1);
            listview.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_listitem, petnames));
        }

    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(LiveFeed.this);

            String[][] petsIn = mDbHelper.readAll();
            String[] datapassed = arg1.getStringArrayExtra("RFIDs");
            String TAG = MyService.class.getSimpleName();
            String feedNames = "";
            ArrayList<String[]> detectedPets = new ArrayList<String[]>();
            for(String e: datapassed) {
                for(int i=0;i<petsIn.length;i++) {
                    if(petsIn[i][0].equals(e) && (Integer.parseInt(petsIn[i][10]) >= Integer.parseInt(petsIn[i][12]))) {
                        feedNames = feedNames + petsIn[i][1] + ", ";
                        detectedPets.add(petsIn[i]);
                    }
                }
            }
            if(feedNames.length() > 2) {
                feedNames = feedNames.substring(0, feedNames.length() - 2);
            }

            Snackbar.make(findViewById(R.id.content_main),feedNames, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            Random rand = new Random();
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            for(String[] pet: detectedPets) {
                int fed = Integer.parseInt(pet[12]);
                int maxfeed = Integer.parseInt(pet[10]);
                int ate = 0;
                if(fed >= maxfeed) {
                    ate = fed;
                } else {
                    ate = fed + rand.nextInt(10) + 10;
                }

                String dbQuery = "UPDATE " + FeedReaderDbHelper.TABLE_NAME + " SET " + FeedReaderDbHelper.FED_COUNT +
                        " = " + ate + " WHERE " + FeedReaderDbHelper.RFID + " = \"" + pet[0] + "\";";
                db.execSQL(dbQuery);
                ate = 0;
            }
            Intent intent = new Intent(LiveFeed.this, LiveFeed.class);
            finish();
            startActivity(intent);
        }
    }
}
