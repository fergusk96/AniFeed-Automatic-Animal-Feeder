package edu.upf.database;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    public static String[][] pets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv;
        tv = (TextView) findViewById(R.id.sampleText);
        tv.setText("");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("AniFeed");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                startActivity(new Intent(MainActivity.this, MainActivity.class));

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                startActivity(new Intent(MainActivity.this, insertPet.class));

            }
        });

        FloatingActionButton fablive = (FloatingActionButton) findViewById(R.id.fablive);
        fablive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                startActivity(new Intent(MainActivity.this, LiveFeed.class));

            }
        });

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        mDbHelper.insert("1", "SNUFFLES", "FRIDAY", "08:00", "11:00", "12:00", "15:00", "18:00", "21:00", 100, 100, 100);
        mDbHelper.insert("10", "REX", "FRIDAY", "08:00", "11:00", "12:00", "15:00", "18:00", "21:00", 150, 300, 100);
        mDbHelper.insert("100", "MISTY", "FRIDAY", "08:00", "11:00", "12:00", "15:00", "18:00", "21:00", 100, 200, 300);
        mDbHelper.insert("101", "LUCKY", "FRIDAY", "08:00", "11:00", "12:00", "15:00", "18:00", "21:00", 50, 80, 60);
        mDbHelper.insert("102", "SAM", "FRIDAY", "08:00", "11:00", "12:00", "15:00", "18:00", "21:00", 50, 300, 60);

        pets = mDbHelper.readAll();
        if(pets.length <= 0) {
        tv.append("\n");
        tv.setText("Add your first pet!");
        }
        else {
            String[] petnames = new String[pets.length];
            for (int i = 0; i < pets.length; i++) {
                petnames[i] = pets[i][1];
            }
           // TextView tv;
        /*tv = (TextView) findViewById(R.id.sample_text);
        tv.append("\n");
        tv.append("" + readval[1]);
        tv.append("\n");
        tv.append("" + readval2[1]);*/


            ListView listview = (ListView) findViewById(R.id.listView1);
            listview.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_listitem, petnames));
            listview.setOnItemClickListener(this);
        }
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, ListItemDetail.class);
        intent.putExtra("position", position);
        intent.putExtra("RFID", pets[position][0]);

        // Or / And
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
