package edu.upf.database;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import edu.upf.database.R;

public class ListItemDetail extends Activity implements AdapterView.OnItemClickListener {

    public static String[][] feeds;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listitem);

        Intent intent = getIntent();
        final String RFID = intent.getStringExtra("RFID");

        // Here we turn your string.xml in an array

        final FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        feeds = mDbHelper.readAllforPET(RFID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile: " + feeds[0][1]);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                startActivity(new Intent(ListItemDetail.this, MainActivity.class));

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addfeed);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent_addfeed = new Intent(ListItemDetail.this, AddFeed.class);
                intent_addfeed.putExtra("RFID", RFID);
                startActivity(intent_addfeed);

            }
        });

        final EditText rfidEdit = (EditText)findViewById(R.id.editRFID);
        rfidEdit.setText(RFID);

        String[] feeddays = new String[feeds.length];
        for(int i=0; i<feeds.length; i++) {
            feeddays[i] = feeds[i][2];
        }

        ListView listview = (ListView) findViewById(R.id.listViewFeeds);
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_listitem, feeddays));
        listview.setOnItemClickListener(this);

        final Button Save = (Button) findViewById(R.id.savebutton);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                String newRFID = rfidEdit.getText().toString();
                String dbQuery = "UPDATE " + FeedReaderDbHelper.TABLE_NAME + " SET " + FeedReaderDbHelper.RFID +
                        " = \"" + newRFID + "\" WHERE " + FeedReaderDbHelper.RFID + " = \"" + RFID + "\";";
                db.execSQL(dbQuery);
                Snackbar.make(v, "Changes have been saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(ListItemDetail.this, ListItemDetail.class);
                intent.putExtra("RFID", newRFID);
                startActivity(intent);

            }
        });

        final Button buttonRemove = (Button) findViewById(R.id.Delete);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    String query = "DELETE FROM " + FeedReaderDbHelper.TABLE_NAME +
                            " WHERE " + FeedReaderDbHelper.RFID + " = \"" + RFID + "\"";
                    db.execSQL(query);
                    Intent intent = new Intent(ListItemDetail.this, MainActivity.class);
                    startActivity(intent);
            }
        });


    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i("FeedListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent();
        intent.setClass(this, FeedItemDetail.class);
        intent.putExtra("position", position);
        intent.putExtra("RFID", feeds[position][0]);
        intent.putExtra("DAY", feeds[position][2]);
        intent.putExtra("numFeeds", feeds.length);

        // Or / And
        intent.putExtra("id", id);
        startActivity(intent);
    }

}