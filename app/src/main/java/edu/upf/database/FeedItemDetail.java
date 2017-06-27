package edu.upf.database;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.upf.database.R;

public class FeedItemDetail extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeditem);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                startActivity(new Intent(FeedItemDetail.this, MainActivity.class));

            }
        });

        Intent intent = getIntent();
        final String RFID = intent.getStringExtra("RFID");
        final String day = intent.getStringExtra("DAY");
        final int numFeeds = intent.getIntExtra("numFeeds", 0);

        // Here we turn your string.xml in an array

        TextView mornTextView = (TextView) findViewById(R.id.mornRange);
        TextView lunTextView = (TextView) findViewById(R.id.lunRange);
        TextView dinTextView = (TextView) findViewById(R.id.dinRange);

        final FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this);
        String[] feed = mDbHelper.read(RFID,day);
        toolbar.setTitle(feed[1] + " - " + day);

        String mornRange = "BREAKFAST: " + feed[3] + " to " + feed[4] + " (max " + feed[9] + "g food)";
        String lunRange = "LUNCH: " + feed[5] + " to " + feed[6] + " (max " + feed[10] + "g food)";
        String dinRange = "DINNER: " + feed[7] + " to " + feed[8] + " (max " + feed[11] + "g food)";

        mornTextView.setText(mornRange);
        lunTextView.setText(lunRange);
        dinTextView.setText(dinRange);

        final Button buttonRemove = (Button) findViewById(R.id.Delete);
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(numFeeds > 1) {

                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    String query = "DELETE FROM " + FeedReaderDbHelper.TABLE_NAME +
                            " WHERE " + FeedReaderDbHelper.RFID + " = \"" + RFID + "\"" +
                            " AND " + FeedReaderDbHelper.DAY + " = \"" + day + "\"";
                    db.execSQL(query);
                    Intent intent = new Intent(FeedItemDetail.this, ListItemDetail.class);
                    intent.putExtra("RFID", RFID);
                    startActivity(intent);
                }
                else {
                    Snackbar.make(v, "ERROR: Cannot Delete Feed; each pet must have one feed", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }

            }
        });

        final Button buttonReturn = (Button) findViewById(R.id.Back);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FeedItemDetail.this, ListItemDetail.class);
                intent.putExtra("RFID", RFID);
                startActivity(intent);
            }
        });

    }

}