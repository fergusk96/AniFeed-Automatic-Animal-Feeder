package edu.upf.database;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Piotrek on 10.06.2017.
 */

public class insertPet extends Activity {
    String[] times = new String[6];
    String[] entries = new String[13];
    public void onCreate(Bundle savedInstanceState) {
        for(int i = 0; i < 13; i++){

            entries[i] = "null";
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertpet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add New Pet");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                startActivity(new Intent(insertPet.this, MainActivity.class));

            }
        });

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday", "Sunday"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        final Button buttonMorn = (Button) findViewById(R.id.mornStart);
        buttonMorn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(insertPet.this, pickTime.class);
                intent.putExtra("button_id", R.id.mornStart);
                startActivityForResult(intent,0);

            }
        });
        final Button buttonMornEnd = (Button) findViewById(R.id.mornEnd);
        buttonMornEnd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(insertPet.this, pickTime.class);
                intent.putExtra("button_id", R.id.mornEnd);
                startActivityForResult(intent,0);
            }
        });
        final Button buttonLunch = (Button) findViewById(R.id.lunchStart);
        buttonLunch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(insertPet.this, pickTime.class);
                intent.putExtra("button_id", R.id.lunchStart);
                startActivityForResult(intent,0);
            }

        });
        final Button buttonLunchEnd = (Button) findViewById(R.id.lunchEnd);
        buttonLunchEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(insertPet.this, pickTime.class);
                intent.putExtra("button_id", R.id.lunchEnd);
                startActivityForResult(intent,0);
            }

        });
        final Button buttonDinner = (Button) findViewById(R.id.dinnerStart);
        buttonDinner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(insertPet.this, pickTime.class);
                intent.putExtra("button_id", R.id.dinnerStart);
                startActivityForResult(intent,0);
            }
        });

        final Button buttonDinnerEnd = (Button) findViewById(R.id.dinnerEnd);
        buttonDinnerEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(insertPet.this, pickTime.class);
                intent.putExtra("button_id", R.id.dinnerEnd);
                startActivityForResult(intent,0);
            }
        });
        final Button Save = (Button) findViewById(R.id.Send);
        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText petNameEdit = (EditText)findViewById(R.id.editText1);
                EditText rfidEdit = (EditText)findViewById(R.id.editText2);
                EditText maxMorningEdit = (EditText)findViewById(R.id.editText4);
                EditText maxLunchEdit = (EditText)findViewById(R.id.editText5);

                EditText maxDinnerEdit = (EditText)findViewById(R.id.editText6);

                Spinner daySpinner = (Spinner) findViewById(R.id.spinner1)
                        ;
                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(insertPet.this);
                String dayName = daySpinner.getSelectedItem().toString();
                String morningStart = buttonMorn.getText().toString();
                String morningEnd = buttonMornEnd.getText().toString();
                String lunchStart = buttonLunch.getText().toString();
                String lunchEnd = buttonLunchEnd.getText().toString();
                String dinnerStart = buttonDinner.getText().toString();
                String dinnerEnd = buttonDinnerEnd.getText().toString();
                String maxDinnerName = maxDinnerEdit.getText().toString().trim();
                String maxLunchName = maxLunchEdit.getText().toString().trim();
                String maxMorningName = maxMorningEdit.getText().toString().trim();

                String rfidName = rfidEdit.getText().toString();
                String petName = petNameEdit.getText().toString();
                mDbHelper.insert(rfidName,petName,dayName,morningStart,morningEnd,lunchStart,lunchEnd,dinnerStart
                        ,dinnerEnd,Integer.parseInt(maxMorningName),Integer.parseInt(maxLunchName),Integer.parseInt(maxDinnerName));
                Intent intent = new Intent(insertPet.this, MainActivity.class);
                startActivity(intent);



            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent intent){

        super.onActivityResult(requestCode,resultCode,intent);


                if(resultCode == Activity.RESULT_OK){
                times[requestCode] = intent.getStringExtra("Time");
                    Button button = (Button) findViewById(intent.getIntExtra("button_id",0));
                    button.setText(times[requestCode]);




        }

    }

    }
