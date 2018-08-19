package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/*
  fragment for start screen activity.
 */
public class LevelFragment extends Fragment {
    bloodGlucoseLevel level;
    Button date;
    Button clear;
    Button history;

    EditText fasting;
    EditText bf;
    EditText lunch;
    EditText dinner;

    TextView fastingResult;
    TextView bfResult;
    TextView lunchResult;
    TextView dinnerResult;
    TextView note;

    CheckBox normalCheck;

    View view;
    levelSet set;

    int HYPOGLYCEMIC = -1;
    int NORMAL = 0;

    int REQUEST_DATE = 0;

    boolean showHistory = true;

    private static final String TAG = "Fragment";
    int egg;

    // return a new instance for creating a ViewPager element
    public static LevelFragment newInstance(bloodGlucoseLevel newLevel, levelSet set, boolean his){
        LevelFragment fragment = new LevelFragment();
        fragment.level = newLevel;
        fragment.set = levelSet.get(null);
        fragment.showHistory = his;
        return fragment;
    }

    public void onResume(){
        super.onResume();
        // update all information in case of data been changed
        setup();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_pager, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.remove:
                levelSet.get(getActivity()).remove(level);
                getActivity().finish();
                return true;
            case R.id.upload:
                FetchItemsTask task = new FetchItemsTask();
                task.entry = level;
                task.execute();
                task.context = getContext();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void onSaveInstanceState(Bundle saved){
        //saved.putSerializable("data",set);
        saved.putSerializable("level", level);
        saved.putBoolean("show",showHistory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        try{
            //set = (levelSet)savedInstanceState.getSerializable("data");
            level = (bloodGlucoseLevel) savedInstanceState.getSerializable("level");
            showHistory = savedInstanceState.getBoolean("show");
        }catch (Exception e){

        }
        View v = inflater.inflate(R.layout.activity_level_fragment, container, false);
        view = v;

        //set = new levelSet();
        init();
        // set UI text
        setup();
        setOnAction();
        setEdit();
        return v;
    }

    // set text for each UI widget
    private void setup(){
        date.setText(level.getDate());
        if(level.fasting == -1){
            clear();
            return;
        }
        fasting.setText(""+level.fasting);
        bf.setText(""+level.bf);
        lunch.setText(""+level.lunch);
        dinner.setText(""+level.dinner);
        note.setText(level.note);
        checkResult(level.normalFasting(),fastingResult);
        checkResult(level.normalBF(),bfResult);
        checkResult(level.normalLunch(),lunchResult);
        checkResult(level.normalDinner(),dinnerResult);
        normalCheck.setChecked(level.normalDay());
    }

    /*
     set Text changed Listener, update level obj and on screen text when user input new data
     */
    private void setEdit(){
        fasting.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    level.setFasting(Integer.parseInt(s.toString()));
                    int bloodLevel = level.normalFasting();
                    checkResult(bloodLevel,fastingResult);
                    easterEgg();
                }catch (Exception e){}
            }
        });

        bf.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    level.setBf(Integer.parseInt(s.toString()));
                    int bloodLevel = level.normalBF();
                    checkResult(bloodLevel,bfResult);
                }catch (Exception e){}
            }
        });

        lunch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    level.setLunch(Integer.parseInt(s.toString()));
                    int bloodLevel = level.normalLunch();
                    checkResult(bloodLevel,lunchResult);
                }catch (Exception e){}
            }
        });

        dinner.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    level.setDinner(Integer.parseInt(s.toString()));
                    int bloodLevel = level.normalDinner();
                    checkResult(bloodLevel,dinnerResult);
                }catch (Exception e){}
            }
        });

        note.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                level.setNote(s.toString());
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        levelSet.get(getActivity()).updateLevel(level);
    }


    /*
     set on action for each button
     */
    private void setOnAction(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                //reset all data that has been saved in to object
                level.fasting = 0;
                level.bf = 0;
                level.lunch = 0;
                level.dinner = 0;
                level.note = new String();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open a new activity with already saved data
                Intent intent = new Intent(getActivity(), ListActivity.class);
                //intent.putExtra("data",set);
                startActivityForResult(intent, REQUEST_DATE);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(level.date.getTime());
                dialog.setTargetFragment(LevelFragment.this, 0);
                dialog.show(manager, "Date");
            }
        });
    }



    /*
     set all UI widget
     */
    private void init(){
        date = view.findViewById(R.id.date_button);
        clear = view.findViewById(R.id.clear);
        history = view.findViewById(R.id.history);

        fasting = view.findViewById(R.id.fasting);
        bf = view.findViewById(R.id.bf);
        lunch = view.findViewById(R.id.lunch);
        dinner =view.findViewById(R.id.dinner);

        fastingResult = view.findViewById(R.id.fasting_result);
        bfResult = view.findViewById(R.id.bf_result);
        lunchResult = view.findViewById(R.id.lunch_result);
        dinnerResult = view.findViewById(R.id.dinner_result);
        note = view.findViewById(R.id.notes);

        normalCheck = view.findViewById(R.id.checkbox);

        // block history button if parent activity is history activity
        if(!showHistory){
            history.setVisibility(View.GONE);
        }
    }

    /*
      clear all input data
     */
    private void clear(){
        fasting.setText("");
        bf.setText("");
        lunch.setText("");
        dinner.setText("");
        note.setText("");
        fastingResult.setText("                          ");
        bfResult.setText("                          ");
        lunchResult.setText("                          ");
        dinnerResult.setText("                          ");
    }

    @Override
    // get date from user input and reset data in object, change text on button
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            bloodGlucoseLevel newL = new bloodGlucoseLevel(cal);
            if (newL.getDate().equals(level.getDate())) {
                return;// do nothing if date has not been changed
            } else if (levelSet.get(getContext()).checkDuplicate(newL.getDate()) != null) {
                // remove old data if selected date has been used
                levelSet.get(getContext()).remove(levelSet.get(getContext()).checkDuplicate(newL.getDate()));
            }
            // change the date
            levelSet.get(getContext()).getList().remove(level);
            level.date.setTime(date);
            levelSet.get(getContext()).add(level);
            this.date.setText(level.getDate());
            // reset the current MainViewPager to apply changes
            MainViewPager activity = (MainViewPager)getContext();
            activity.getIntent().putExtra("current",level);
            activity.resetAdapter();
        }
    }


    // return result of each data
    public void checkResult(int bloodLevel, TextView text) {
        if (bloodLevel == HYPOGLYCEMIC) {
            text.setTextColor(Color.RED);
            text.setText("Hypoglycemic");
        } else if (bloodLevel == NORMAL) {
            text.setTextColor(Color.GRAY);
            text.setText("      Normal       ");
        } else {
            text.setTextColor(Color.RED);
            text.setText("     Abnormal    ");
        }
        normalCheck.setChecked(level.normalDay());
    }

    private void easterEgg(){
        if(level.fasting == 114514 &&(!showHistory)){
            history.setVisibility(View.VISIBLE);
            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "我的心中毫无波澜\n甚至不想再new 一个object",
                                Toast.LENGTH_SHORT).show();
//                    egg++;
//                    if(egg == 10){
//                        Toast.makeText(getContext(), "我不会编程 也懒得表白",
//                                Toast.LENGTH_SHORT).show();
//                    }else if(egg == 20){
//                        Toast.makeText(getContext(), "我有100个贰刺螈纸片人老婆",
//                                Toast.LENGTH_SHORT).show();
//                    }else if(egg == 30){
//                        Toast.makeText(getContext(), "再见了吧您嘞",
//                                Toast.LENGTH_SHORT).show();
//                        date.setText("有什么好点的 啊？(　^ω^)");
//                        date.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //自杀
//                                throw new IllegalStateException();
//                            }
//                        });
//                    }
                }
            });
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        bloodGlucoseLevel entry;
        Context context;
        boolean res = true;
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d("data", entry.toJSON().toString());
            }catch (Exception e){}
            try{
                String result = new Fetchr().
                        getUrlString("http://u.arizona.edu/~lxu/cscv381/local_glucose.php",level.toJSON().toString());
                res = true;
                //Toast.makeText(context,"Upload Success",Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                res = false;
                //Toast.makeText(context,"Upload Failed",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to fetch URL: ");
            }

            System.out.println(context == null);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // show a toast on screen to indicate upload status
            if(res){
                Toast.makeText(context,"Upload Success",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"Upload Failed",Toast.LENGTH_SHORT).show();
            }
        }
    }






}