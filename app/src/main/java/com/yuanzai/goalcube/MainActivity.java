package com.yuanzai.goalcube;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.yuanzai.goalcube.MAIN_MESSAGE";
    int[] rArray;
    AppData ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ad = (AppData)getApplicationContext();
        ad.goalsInit();
        initRArray();
        
        //Button b1 = (Button) findViewById(R.id.button1);
        //Goal g = ad.getGoal(0);
        //b1.setText(g.name);




        for (int i = 0; i < 8; i++){
            Goal g = ad.getGoal(i);
            Button b = (Button) findViewById(rArray[i]);
            final int goal_id = i;
            if (g.name != null) {
                b.setText(g.name);
            } else {
                g.name = "+";
            }

            b.setHeight(b.getWidth());
            b.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), CreateGoalActivity.class);
                    startActivity(intent);
                    
                    // i in the message might need to be a string
                    // rmb to change layout to remove on click for button 1
                    // rmb to add buttons in 1 by 1
                    intent.putExtra(EXTRA_MESSAGE, goal_id);
                }
            });
        }

        /*
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.activity_main, null);
        LinearLayout tv = (LinearLayout) inflater.inflate(R.layout.act_count, null);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.addView(tv);
        */

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
        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(this, CreateActivityActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void initRArray() {
        rArray = new int[8];
        rArray[0] = R.id.button0;
        rArray[1] = R.id.button1;
        rArray[2] = R.id.button2;
        rArray[3] = R.id.button3;
        rArray[4] = R.id.button4;
        rArray[5] = R.id.button5;
        rArray[6] = R.id.button6;
        rArray[7] = R.id.button7;
    }
}
