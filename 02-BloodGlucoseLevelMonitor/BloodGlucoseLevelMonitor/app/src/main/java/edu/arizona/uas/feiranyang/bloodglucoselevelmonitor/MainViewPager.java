package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class MainViewPager extends AppCompatActivity {
    ViewPager viewPager;
    levelSet set;
    boolean showHistory = true;
    int init = 0;
    bloodGlucoseLevel currentObj;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        set = levelSet.get(this);

        //start a new service if it is not on
        if(!ReminderService.isServiceAlarmOn(this)) {
            ReminderService.setServiceAlarm(this, true);
        }
        setContentView(R.layout.activity_pager);

        viewPager = findViewById(R.id.view_pager);
        FragmentManager fragmentManager = getSupportFragmentManager();
        try{
            if(getIntent().getSerializableExtra("ID") != null){
                showHistory = false;
                setTitle("Detail");
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }catch (Exception e){}
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                bloodGlucoseLevel level = set.getList().get(position);
                return LevelFragment.newInstance(level,set,showHistory);
            }

            @Override
            public int getCount() {
                return set.getList().size();
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                currentObj = ((LevelFragment) object).level;
                super.setPrimaryItem(container, position, object);
            }
        });

        // set default pos
        int id = set.getList().size()-1;
        // if calling from another activity, find the target pos
        if(!showHistory){
            id = getPos((UUID)getIntent().getSerializableExtra("ID"));
        }
        // set CurrentItem by request
        viewPager.setCurrentItem(id);
        // go to the list page if no entry was found.
        if(viewPager.getAdapter().getCount() == 0){
            Intent intent = new Intent(getApplicationContext(), ListActivity.class);
            startActivityForResult(intent,0);
            Toast.makeText(this, "no entry was found\n" +
                    "add a entry first.",Toast.LENGTH_LONG).show();
        }

    }

    public void onPause(){
        super.onPause();
        init = 1;
        getIntent().putExtra("current", currentObj);
    }

    public void onResume(){
        super.onResume();
        //super.onStop();
        if(init == 1) {
            resetAdapter();
        }

        Log.d("fuiasd",""+viewPager.getAdapter().getCount());
    }

    public void resetAdapter(){
        List<bloodGlucoseLevel> thisSet = levelSet.get(this).getList();
        int pos = thisSet.indexOf(getIntent().getSerializableExtra("current"));
        Log.d("重置控制器","成功");
        FragmentManager fragmentManager = getSupportFragmentManager();
        // set a new adapter in case the data entry has been changed
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                bloodGlucoseLevel level = set.getList().get(position);
                return LevelFragment.newInstance(level, set, showHistory);
            }

            @Override
            public int getCount() {
                return set.getList().size();
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                currentObj = ((LevelFragment) object).level;
                super.setPrimaryItem(container, position, object);
            }
        });
        Log.d("之前的Obj index",""+pos);
        if(pos<0){
            viewPager.setCurrentItem(thisSet.size()-1);
        }else {
            viewPager.setCurrentItem(pos);
        }
        //viewPager.getAdapter().notifyDataSetChanged();
    }

    // save old data to bundle before resume activity
    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
    }

    // if open this view pager from other activity, save the id number and data to intent before
    // open it
    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, MainViewPager.class);
        return intent;
    }

    public static Intent newIntent(Context context, String string){
        Intent intent = newIntent(context);
        //if this entry for today is not exist
        if(levelSet.get(context).checkDuplicate(new bloodGlucoseLevel(Calendar.getInstance()).getDate())==null){
            levelSet.get(context).add(new bloodGlucoseLevel(Calendar.getInstance()));
        }
        return intent;
    }

    // set a backButton action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int getPos(UUID uuid){
        int id = -1;

        List<bloodGlucoseLevel> levels = levelSet.get(this).getList();
        for(int i = 0; i< levels.size();i++){
            if(levels.get(i).getID().toString().equals(uuid.toString())){
                id = i;
            }
        }
        return id;
    }



}
