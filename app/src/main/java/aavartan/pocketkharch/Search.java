package aavartan.pocketkharch;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Search extends AppCompatActivity {

    public TextView dateView;
    private int year, month, day;
    private Calendar calendar;
    public DatabaseHelper myDb;
    int count,sid,uid;
    String string[];
    TableLayout tl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar4);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search Expense");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        tl = (TableLayout) findViewById(R.id.main_table);
        dateView = (TextView) findViewById(R.id.dateView);
        myDb = new DatabaseHelper(this);
        string = new String[20];

        viewDate();
    }

    //DATE PICKER
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month-1, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    //finish();
                    //startActivity(getIntent());
                    tl.removeAllViewsInLayout();
                    showDate(arg1, arg2+1, arg3);}
            };
    void viewDate(){
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }
    private void showDate(int y, int m, int d) {
        day = d;
        month = m;
        year = y;
        //Date =  toString(year + "/" + month + "/" + day);
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

        DataDate(d,m,y);


    }

    void DataDate(int d,int m,int y){

        final Context context = this;
        myDb = new DatabaseHelper(this);

        TableRow tr_head = new TableRow(this);
        tr_head.setId(10);
        tr_head.setBackgroundColor(Color.parseColor("#a8e4e5"));
        tr_head.setLayoutParams(new ActionBar.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        TextView label_date = new TextView(this);
        label_date.setId(20);
        label_date.setText("Category");
        label_date.setTextSize(18);
        label_date.setTextColor(Color.BLACK);
        label_date.setPadding(8, 8, 18, 18);
        tr_head.addView(label_date);// add the column to the table row here

        TextView label_date1 = new TextView(this);
        label_date1.setId(22);
        label_date1.setText("Date");
        label_date1.setTextSize(18);
        label_date1.setTextColor(Color.BLACK);
        // label_date1.setPadding(5, 5, 15, 15);
        tr_head.addView(label_date1);// add the column to the table row here

        TextView label_weight_kg = new TextView(this);
        label_weight_kg.setId(21);// define id that must be unique
        label_weight_kg.setText("Amount"); // set the text for the header
        label_weight_kg.setTextSize(18);
        label_weight_kg.setTextColor(Color.BLACK); // set the color
        label_weight_kg.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_weight_kg); // add the column to the table row here

        tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));


        count=0;
        Cursor res = myDb.GetDateData(Integer.toString(d),Integer.toString(m),Integer.toString(y));
        sid=0;

        if(res.getCount()==0)
            Toast.makeText(Search.this,"NO DATA FOUND",Toast.LENGTH_LONG).show();

        while(res.moveToNext()) {
            string[count] = res.getString(0);

            TableRow tr = new TableRow(this);
            if(count%2!=0) tr.setBackgroundColor(Color.parseColor("#72B6B2"));
            else tr.setBackgroundColor(Color.parseColor("#4E9692"));
            tr.setId(100+count);
            tr.setLayoutParams(new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            //Create two columns to add as table data
            // Create a TextView to add date
            final TextView labelDATE = new TextView(this);
            labelDATE.setId(200+count);
            labelDATE.setText(res.getString(4));
            labelDATE.setTextSize(14);
            labelDATE.setPadding(5, 5, 15, 15);
            labelDATE.setTextColor(Color.WHITE);
            labelDATE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                    //builder.setTitle("AlertDialog Example");
                    builder.setMessage("Are you Sure want to Delete Record");
                    uid=labelDATE.getId()-200;
                    //input.setHint("HELLO");
                    //Button One : Yes
                    builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDb.DeleteRow(Integer.parseInt(string[uid]));
                            Toast.makeText(Search.this, "Record Deleted", Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(getIntent());
                        }
                    });

                    //Button Three : Neutral
                    builder.setNeutralButton("CANCLE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(Search.this, "Cancle", Toast.LENGTH_LONG).show();
                            dialog.cancel();
                        }
                    });
                    AlertDialog diag = builder.create();
                    diag.show();
                }
            });
            tr.addView(labelDATE);

            final TextView labelWEIGHT = new TextView(this);
            labelWEIGHT.setId(300+count);
            labelWEIGHT.setText(res.getString(1)+"/"+res.getString(2)+"/"+res.getString(3));
            labelWEIGHT.setTextSize(14);
            labelWEIGHT.setTextColor(Color.WHITE);
            tr.addView(labelWEIGHT);

            final TextView labelWEIGHT1 = new TextView(this);
            labelWEIGHT1.setId(400+count);
            labelWEIGHT1.setText(res.getString(5));
            labelWEIGHT1.setTextSize(14);
            labelWEIGHT1.setTextColor(Color.WHITE);
            tr.addView(labelWEIGHT1);

// finally add this to the table row
            tl.addView(tr, new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            count++;
        }

    }

}
