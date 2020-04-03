package aavartan.pocketkharch;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Add_Exp extends AppCompatActivity {

    DatabaseHelper myDb;
    public int i;
    public String Cat,Date;
    public TextView textView_list;
    public Button btn,btnViewAll;
    public StringBuffer buffer;
    public TextView t_view;
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    public EditText editText_amt,editText_des;
    public String[] commandArray = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar3);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Expense");
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


        myDb = new DatabaseHelper(this);
        btn = (Button) findViewById(R.id.button_sbt);
        dateView = (TextView) findViewById(R.id.dateView);
        btnViewAll = (Button) findViewById(R.id.button_view);
        editText_des = (EditText) findViewById(R.id.editText_des);
        editText_amt = (EditText) findViewById(R.id.editText_amt);
        Cat = "FOOD";

        viewDate();
        viewCat();
        //btn_click();
        list_view();
        viewAll();
        AddData();
    }



    //DIALOG CATEGORY VIEW
    public void list_view(){
        textView_list = (TextView) findViewById(R.id.textView_list);
        textView_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDialog();
            }
        });
    }
    public void showDialog()
    {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CATEGORY");
        builder.setItems(commandArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Cat = commandArray[which];
                textView_list.setText(Cat);
            }
        });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void viewCat(){
        i=-1;
        Cursor res = myDb.getCat();
        while(res.moveToNext()){
            commandArray[++i]=res.getString(0);
            //i++;
        }
        int j;
        for(j=i+1;j<20;j++)
            commandArray[j]="";
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
                                      int arg1, int arg2, int arg3) {showDate(arg1, arg2+1, arg3);}
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
    }

    public void viewAll(){
        btnViewAll.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View v){
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0){
                            //show message
                            showMessage("Error","Nothing Found");
                            return;
                        }

                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("DATE: "+res.getString(1)+"/"+res.getString(2)+"/"+res.getString(3)+"\n");
                            buffer.append("CATEGORY: "+ res.getString(4)+"\n");
                            buffer.append("AMOUNT: "+ res.getString(5)+"\n");
                            buffer.append("DESCRIPTION: "+ res.getString(6)+"\n\n");
                        }
                        //show message
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }
    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void AddData(){
        btn.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        //t_view.setText(Cat.toString());
                        Calendar c = Calendar.getInstance();
                        int CurDay=c.get(Calendar.DATE);
                        int CurMonth=c.get(Calendar.MONTH)+1;
                        int CurYear=c.get(Calendar.YEAR);
                        if(CurYear==year)
                        {
                            if(CurMonth==month)
                            {
                                if(CurDay>=day)
                                {
                                    //valid
                                }
                                else
                                {
                                    Toast.makeText(Add_Exp.this,"Date is Greater than Current Date",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            else
                            {
                                if(CurMonth>month)
                                {
                                    //valid
                                }
                                else
                                {
                                    Toast.makeText(Add_Exp.this,"Date is Greater than Current Date",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }
                        else
                        {
                            if(CurYear>year)
                            {
                                //Valid
                            }
                            else
                            {
                                Toast.makeText(Add_Exp.this,"Date is Greater than Current Date",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        if(editText_amt.getText().toString().length() == 0){
                            Toast.makeText(Add_Exp.this,"Enter Amount",Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(editText_des.getText().toString().length() == 0){
                            Toast.makeText(Add_Exp.this,"Write Some Description",Toast.LENGTH_LONG).show();
                            return;
                        }
                        boolean isInserted = myDb.insertData(String.valueOf(day),
                                String.valueOf(month),
                                String.valueOf(year),
                                Cat.toString(),
                                editText_amt.getText().toString(),
                                editText_des.getText().toString() );
                        if(isInserted == true)
                            Toast.makeText(Add_Exp.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(Add_Exp.this,"Data Not Inserted",Toast.LENGTH_LONG).show();

                        editText_amt.setText("");
                        editText_des.setText("");

                        //Show Current Inserted Data

                    }

                }
        );
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this,MainActivity.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
