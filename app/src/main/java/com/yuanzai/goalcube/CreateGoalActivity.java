package com.yuanzai.goalcube;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class CreateGoalActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.yuanzai.goalcube.ACTIVITY_MESSAGE";
    AppData ad;
    Goal g;
    int goal_id;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_goal);
        ad = (AppData) getApplicationContext();
        Intent intent = getIntent();
        goal_id = intent.getIntExtra(MainActivity.EXTRA_MESSAGE,0);
        g = ad.getGoal(goal_id);
        showGoal();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.activity_create_goal, null);
        LinearLayout ll2 = new LinearLayout(this);
        //ll2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //ll2.setOrientation(LinearLayout.VERTICAL);
        View tv = g.routines.get(0).viewRoutine(this);
        //ll.addView(ll2);
        ll.addView(tv);
        setContentView(ll);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_goal, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            // i in the message might need to be a string
            // rmb to change layout to remove on click for button 1
            // rmb to add buttons in 1 by 1
            intent.putExtra(EXTRA_MESSAGE, goal_id);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showGoal() {
        //name = (EditText) findViewById(R.id.goalName);
        //name.setText(g.name);

    }

    public void saveGoal(View view) {
        Log.d(this.getClass().getSimpleName(), "Save Button Clicked");
        g.name = name.getText().toString();
        ad.saveGoals();
    }
}
