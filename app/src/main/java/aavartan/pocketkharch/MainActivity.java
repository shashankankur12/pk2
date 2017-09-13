package aavartan.pocketkharch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.bloder.magic.view.MagicButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseHelper myDb;
    TextView view_total_amt,textView3;
    TextView view_Exp_Cat;
    int activityBrequestCode;
    GridLayout gridLayout;
    MagicButton magicButton;
    Calendar calendar = Calendar.getInstance();
    String string[] = new String[20];
    String[] month  = new String[]{
            "1","2","3","4","5","6","7",
            "8","9","10","11","12"};
    String[] year  = new String[]{
            "2016","2017","2018","2019"};
    ArrayList<String> categoryList = new ArrayList<String>();
    Toolbar toolbar;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDb = new DatabaseHelper(this);
        view_total_amt = (TextView) findViewById(R.id.view_total_amt);
        gridLayout = (GridLayout) findViewById(R.id.tableGrid);
        magicButton = (MagicButton) findViewById(R.id.magic_button);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        calView();
        CurDateView();
        floatingActionBtn();
        drawerLayout();

    }
       void floatingActionBtn() {

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, Add_Exp.class);
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    // .setAction("Action", null).show();
                    startActivity(intent);

                }
            });
        }

        void drawerLayout(){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Search) {
            Intent intent= new Intent(MainActivity.this,Search.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_Add_Reminder) {
            return true;
        }
        if (id == R.id.action_Categories) {
            Intent intent= new Intent(MainActivity.this,AddCategory.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_Edit_Expence) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
        }else if (id == R.id.nav_calci) {

            Intent intent = new Intent(this,Calculator.class);
            startActivity(intent);


        } else if (id == R.id.nav_backup) {

        } else if (id == R.id.nav_convertor) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void viewTotalMonth(String CurMonth,String CurYear){
        //textView3.setText(CurMonth+" "+CurYear);
        Cursor res = myDb.getTotalMonth(CurMonth,CurYear);
        Integer sum = 0;
        if(res.getCount() == 0){
            view_total_amt.setText("Total = "+sum);
            return;
        }
        while(res.moveToNext()){
            sum = sum + Integer.parseInt(res.getString(0));
        }
        view_total_amt.setText("Total = "+sum);
        //Integer.toString(CurMonth)
    }

    public void grid_view(final String CurMonth,final String CurYear){
        Cursor res = myDb.getCat();
        Cursor resCat;
        Integer sum=0;
        String totalCat[] = new String[20];
        int count=0;
        while(res.moveToNext()) {
            string[count] = res.getString(0);
            resCat = myDb.getCatTotal(string[count],CurMonth,CurYear);
            while(resCat.moveToNext()){
                sum = sum + Integer.parseInt(resCat.getString(0));
            }
            totalCat[count]=sum.toString();
            sum=0;
            count++;
        }
        int total = res.getCount();
        int column = 2;
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);

        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }
            final TextView textView = new TextView(this);

            textView.setText(string[i]+"\n("+totalCat[i]+")");
            textView.setId(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int cid=textView.getId();
                    Cursor res = myDb.getCatData(string[cid],CurMonth,CurYear);
                    if(res.getCount() == 0){
                        //show message
                        showMessage("","Nothing Found");
                        return;
                    }

                    StringBuffer buffer = new StringBuffer();
                    while(res.moveToNext()){
                        buffer.append("DATE: "+res.getString(1)+"/"+res.getString(2)+"/"+res.getString(3)+"\n");
                        buffer.append("CATEGORY: "+ res.getString(4)+"\n");
                        buffer.append("AMOUNT: "+ res.getString(5)+"\n");
                        buffer.append("DETAIL: "+ res.getString(6)+"\n\n");
                    }
                    //show message
                    showMessage("Data",buffer.toString());
                }
            });
            // ImageView oImageView = new ImageView(this);
            // textView.setImageResource(R.drawable.wallet1);
            GridLayout.LayoutParams param = new GridLayout.LayoutParams();
            param.height = GridLayout.LayoutParams.WRAP_CONTENT;
            param.width = GridLayout.LayoutParams.WRAP_CONTENT;
            textView.setBackgroundColor(Color.parseColor("#a8e4e5"));
            textView.setWidth(320);
            textView.setHeight(150);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            param.rightMargin = 20;
            param.topMargin = 30;
            param.leftMargin = 20;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            textView.setLayoutParams(param);
            gridLayout.addView(textView);
        }
    }

    public void showMessage(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void calView(){
        magicButton.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // Create custom dialog object
                final Dialog dialog = new Dialog(MainActivity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog);
                // Set dialog title
                dialog.setTitle("Custom Dialog");

                // set values for custom dialog components - text, and button
                TextView text = (TextView) dialog.findViewById(R.id.textView);
                text.setText("Month");
                final Spinner spin = (Spinner)dialog.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter_month = new ArrayAdapter<String>(MainActivity.this,  android.R.layout.simple_spinner_item, month);
                adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setSelection(2);
                spin.setAdapter(adapter_month);

                TextView text1 = (TextView) dialog.findViewById(R.id.textView1);
                text1.setText("Year");
                final Spinner spin1 = (Spinner)dialog.findViewById(R.id.spinner1);
                ArrayAdapter<String> adapter_year = new ArrayAdapter<String>(MainActivity.this,  android.R.layout.simple_spinner_item, year);
                adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin1.setSelection(2);
                spin1.setAdapter(adapter_year);

                dialog.show();

                Button declineButton = (Button) dialog.findViewById(R.id.button);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String anymonth=spin.getSelectedItem().toString();
                        final String anyyear=spin1.getSelectedItem().toString();
                        AnyDateView(anymonth,anyyear);

                        // Close dialog
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void CurDateView(){
        Calendar c = Calendar.getInstance();
        String CurMonth=String.valueOf(c.get(Calendar.MONTH)+1);
        String CurYear=String.valueOf(c.get(Calendar.YEAR));
        viewTotalMonth(CurMonth,CurYear);
        grid_view(CurMonth,CurYear);
    }

    public void AnyDateView(String CurMonth,String CurYear){
        viewTotalMonth(CurMonth,CurYear);
        grid_view(CurMonth,CurYear);
    }

    
}
