package aavartan.pocketkharch;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AddCategory extends AppCompatActivity {
    TextView textView_list;
    EditText editText_cat;
    Button AddCat_btn;
    DatabaseHelper myDb;
    public String[] string = new String[30];
    Integer count,sid,uid;
    Context context = this;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

       Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Category");
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
        //textView_list = (TextView) findViewById(R.id.textView_list);
        editText_cat = (EditText) findViewById(R.id.editText_cat);
        AddCat_btn = (Button) findViewById(R.id.AddCat_btn);
        AddCat();
        table_view();
    }

    public void table_view(){
        final Context context = this;
        myDb = new DatabaseHelper(this);
        TableLayout tl = (TableLayout) findViewById(R.id.main_table);
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
        label_date1.setText("Update");
        label_date1.setTextSize(18);
        label_date1.setTextColor(Color.BLACK);
        // label_date1.setPadding(5, 5, 15, 15);
        tr_head.addView(label_date1);// add the column to the table row here

        TextView label_weight_kg = new TextView(this);
        label_weight_kg.setId(21);// define id that must be unique
        label_weight_kg.setText("Delete"); // set the text for the header
        label_weight_kg.setTextSize(18);
        label_weight_kg.setTextColor(Color.BLACK); // set the color
        label_weight_kg.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_weight_kg); // add the column to the table row here

        tl.addView(tr_head, new TableLayout.LayoutParams(
                ActionBar.LayoutParams.FILL_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT));

        count=0;
        Cursor res = myDb.getCat();
        sid=0;
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
            TextView labelDATE = new TextView(this);
            labelDATE.setId(200+count);
            labelDATE.setText(string[count].toString());
            labelDATE.setTextSize(14);
            labelDATE.setPadding(5, 5, 15, 15);
            labelDATE.setTextColor(Color.WHITE);
            tr.addView(labelDATE);

            final TextView labelWEIGHT = new TextView(this);
            labelWEIGHT.setId(300+count);
            labelWEIGHT.setText("Update");
            labelWEIGHT.setTextSize(14);
            labelWEIGHT.setTextColor(Color.WHITE);
            labelWEIGHT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("UPDATE CATEGORY"); //Set Alert dialog title here
                    //alert.setMessage("Enter Your Name Here"); //Message here

                    // Set an EditText view to get user input
                    final EditText input = new EditText(context);
                    //input.setHint("HELLO");
                    uid=labelWEIGHT.getId()-300;
                    input.setText(string[uid]);
                    alert.setView(input);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //You will get as string input data in this variable.
                            // here we convert the input to a string and show in a toast.
                            String srt = input.getEditableText().toString();
                            if( string[uid].compareTo("OTHERS")==0 ||string[uid].compareTo("FOOD")==0 ||string[uid].compareTo("TRANSPORTATION")==0){
                                Toast.makeText(AddCategory.this,"CANNOT UPDATE PRIMARY CATEGORIES",Toast.LENGTH_LONG).show();
                            }
                            else {
                                myDb.UpdateCat(string[uid],srt.toUpperCase());
                                finish();
                                startActivity(getIntent());
                            }

                            Toast.makeText(context,srt,Toast.LENGTH_LONG).show();
                        } // End of onClick(DialogInterface dialog, int whichButton)
                    }); //End of alert.setPositiveButton
                    alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // Canceled.
                            dialog.cancel();
                        }
                    }); //End of alert.setNegativeButton
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();


                }
            });
            tr.addView(labelWEIGHT);

            final TextView labelWEIGHT1 = new TextView(this);
            labelWEIGHT1.setId(400+count);
            labelWEIGHT1.setText("Delete");
            labelWEIGHT1.setTextSize(14);
            labelWEIGHT1.setTextColor(Color.WHITE);
            labelWEIGHT1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(AddCategory.this)
                            .setTitle("DELETE Warning")
                            .setMessage("Are You Sure Want to Delete?")
                            // dismisses by default
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sid=labelWEIGHT1.getId()-400;
                                    if( string[sid].compareTo("OTHERS")==0 ||string[sid].compareTo("FOOD")==0 ||string[sid].compareTo("TRANSPORTATION")==0){
                                        Toast.makeText(AddCategory.this,"CANNOT DELETE PRIMARY CATEGORIES",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        myDb.DeleteCat(string[sid]);
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }
                            }).setNegativeButton(android.R.string.cancel, null)
                            .create()
                            .show();

                }
            });

            tr.addView(labelWEIGHT1);

// finally add this to the table row
            tl.addView(tr, new TableLayout.LayoutParams(
                    ActionBar.LayoutParams.FILL_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT));
            count++;
        }
    }

    public void AddCat(){
        AddCat_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                final AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("ADD CATEGORY");
                final EditText input = new EditText(context);
                alert.setView(input);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String srt = input.getEditableText().toString();
                        if(srt.length() == 0)
                        {
                            Toast.makeText(AddCategory.this,"Empty Category",Toast.LENGTH_LONG).show();
                            return;
                        }
                        boolean isInserted = myDb.insertData_cat(srt.toString().toUpperCase());
                        finish();
                        startActivity(getIntent());
                        ///editText_cat.setText("");
                        if(isInserted == true)
                            Toast.makeText(AddCategory.this,"Category Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(AddCategory.this,"Category Not Inserted",Toast.LENGTH_LONG).show();
                    } // End of onClick(DialogInterface dialog, int whichButton)
                }); //End of alert.setPositiveButton
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                        dialog.cancel();
                    }
                }); //End of alert.setNegativeButton
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
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
