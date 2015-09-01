package com.yuanzai.goalcube;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by junyuanlau on 20/7/15.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppData extends Application{
    public ArrayList<Goal> goals;
    public static final String FILENAME = "goals.json";

    public Goal getGoal(int i) {
        System.out.println(Uri.parse("android.resource://com.yuanzai.goalcube/raw/goals"));

        if (goals == null) {
            goalsInit();
            Log.d(this.getClass().getSimpleName(),"Load goals");
            loadGoals();
        }

        return goals.get(i);
    }

    public void loadGoals(){
        BufferedReader br;
        File f = new File(getApplicationContext().getFilesDir(),FILENAME);

            try {
                br = new BufferedReader(new FileReader(f.getAbsolutePath()));
                Log.d(this.getClass().getSimpleName(),"Goals loaded via file");
            } catch (FileNotFoundException e) {
                InputStream is = getResources().openRawResource(R.raw.goals);
                Reader reader = new InputStreamReader(is);
                br = new BufferedReader(reader);
                Log.d(this.getClass().getSimpleName(),"Goals loaded via raw resource");
            }

        Gson gson = new Gson();
        goals = gson.fromJson(br, new TypeToken<ArrayList<Goal>>(){}.getType());
    }

    public void goalsInit() {
        goals = new ArrayList<Goal>();
        for (int i = 0; i< 9; i++){
            goals.add(new Goal());
        }
        goals.get(0).name = "Weight";
        goals.get(0).isLive = true;
        goals.get(0).addActivityToDefaultRoutine(new CountActivity("Activity Name","Activity Unit", 5 ,true));
        goals.get(0).addActivityToDefaultRoutine(new CountActivity("Activity Name 2","Activity Unit 2", 15 ,true));
    }

    public void saveGoals() {
        Gson gson = new Gson();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        String json = gson.toJson(this.goals);

        try {
            //write converted json data to a file named "file.json"
            FileWriter writer = new FileWriter(new File(getApplicationContext().getFilesDir(),FILENAME).getAbsolutePath());
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(this.getClass().getSimpleName(),json);
        Log.d(this.getClass().getSimpleName(),"Goals saved to file");

    }
}


class Goal {
    ArrayList<Routine> routines;
    boolean isLive;
    String name;

    public Goal(){
        isLive = false;
        routines = new ArrayList<Routine>();
        routines.add(new Routine("Standard"));
    }

    public View getDefaultRoutineView(Context c) {
        return routines.get(0).viewRoutine(c);
    }

    public static class Routine {
        ArrayList<Activity> activities;
        String name;

        public Routine(String name) {
            this.name = name;
            this.activities = new ArrayList<Activity>();
        }
    }
    
    public void addActivityToDefaultRoutine(Activity act) {
        routines.get(0).activities.add(act);
    }
}

class GoalViewController {
    Context c;
    AppData ad;
    Goal g;
    Date date;
    
    public GoalViewController(Context c, AppData ad, Goal g, Date date) {
        this.c = c;
        this.ad = ad;
        this.g = g;
        this.date = date;
    }
    
    public View viewRoutine() {
        LinearLayout ll = new LinearLayout(c);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView dateText = new TextView();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateText.setText(dateFormat.format(date));
        ll.addView(dateText);
        
        Button editButton = new Button();
        editButton.setText("Edit");
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(this.getClass().getSimpleName(),"Edit Button");
                //Intent intent = new Intent(v.getContext(), CreateGoalActivity.class);
                //startActivity(intent);
                //intent.putExtra(EXTRA_MESSAGE, goal_id);
            }
        });
        
        for (Activity act : g.activities) {
            ll.addView(act.viewDataPointView(c));
        }
        return ll;
    }
    
    public View addEditActivityDataPointInRoutine() {
        LinearLayout ll = new LinearLayout(c);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.VERTICAL);
        TextView dateText = new TextView();
        
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        dateText.setText(dateFormat.format(date));
        ll.addView(dateText);
        

        
        for (Activity act : g.activities) {
            ll.addView(act.getActivityViewController(c, date));
        }
        Button editButton = new Button();
        editButton.setText("Save");
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(this.getClass().getSimpleName(),"Save Button");
                for (Activity act : g.activities) {
                    act.getActivityViewController.saveDataPoint();
                }
                c.saveGoals();
                ad.saveGoals();
                //Intent intent = new Intent(v.getContext(), CreateGoalActivity.class);
                //startActivity(intent);
                //intent.putExtra(EXTRA_MESSAGE, goal_id);
            }
        });
        ll.addView(b);
        
        return ll;
        

    }
}
// Boolean
// Timing (Implies boolean)
// Reps (Implies boolean) Max, targeted
// Count (Implies boolean) Ascending, untargeted, with base
// Distance (Implies boolean)
// Weight (Implies boolean)

abstract class Activity {
    String name;
    abstract ActivityViewController getActivityViewController(Context c,Date date);
}

abstract class ActivityViewController {
    abstract View viewDataPointView();
    abstract View updateDataPointView();
    abstract View addDataPointView();
    abstract View dataView();
    abstract void saveDataPoint();
}

class CountActivity extends Activity {
    int target;
    String units;
    boolean isIncreasing;
    Map<Date, DataPoint> data;
    Date date;
    
    public CountActivity(String name, String units, int target, boolean isIncreasing){
        this.name = name;
        this.units = units;
        this.target = target;
        this.isIncreasing = isIncreasing;
        this.data = new Map<Date, DataPoint>();
    }

    public static class DataPoint {
        int count;
        Date date;
        Date entered;
        
        public DataPoint(int count, Date date, Date entered) {
            this.count = count;
            this.date = date;
            this.entered = entered;
        }
    }
    
    public ActivityViewController getActivityViewController(Context c, Date date) {
        return new CountActivityViewController(this, c, date);
    }
}

class CountActivityViewController extends ActivityViewController {
    CountActivity act;
    Context c;
    Date date;
    
    EditText count;
    
    public CountActivityViewController(CountActivity act, Context c,Date date){
        self.act = act;
        self.c = c;
        self.date = date;
    }
    
    public View viewDataPointView() {

        return v;
    }

    public View updateDataPointView() {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.act_count, null);
        EditText count = (EditText) v.findViewById(R.id.act_count_input);
        count.setText("" + act.get(date).count);
        TextView measure = (TextView) v.findViewById(R.id.act_count_name);
        measure.setText(act.name);
        
        TextView units = (TextView) v.findViewById(R.id.act_count_units);
        measure.setText(act.units);

        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Log.d(this.getClass().getSimpleName(),Integer.toString(target));

        return v;

    }

    public View dataView() {
        return null;
    }
    
    public View addDataPointView() {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout v = (LinearLayout) inflater.inflate(R.layout.act_count, null);
        EditText count = (EditText) v.findViewById(R.id.act_count_input);
        count.setText("" + act.target);
        TextView measure = (TextView) v.findViewById(R.id.act_count_name);
        measure.setText(act.name);

        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Log.d(this.getClass().getSimpleName(),Integer.toString(target));

        return v;
    }
    
    public saveDataPoint() {
        act.data.put(date, new act.DataPoint(Integer.valueOf(count.getText()), date, date);
    }
}

/*
class MeasureActivity extends Activity {
    double measure;
    String units;
    double target;
}
*/
