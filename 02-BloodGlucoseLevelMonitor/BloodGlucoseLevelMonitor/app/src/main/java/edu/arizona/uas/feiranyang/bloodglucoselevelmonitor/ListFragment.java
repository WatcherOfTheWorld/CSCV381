package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ListFragment extends Fragment{
    private RecyclerView recyclerView;
    private Adapter adapter;
    levelSet set;
    int REQUEST_DATE = 0;
    Uri URL = Uri.parse("http://u.arizona.edu/~lxu/cscv381/myglucose.php?username=feiranyang&password=a5502");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        try {
            levelSet newSet = levelSet.get(getContext());
            if (newSet != null) {
                set = newSet;
            }
        }catch (Exception e){
        }
        set = levelSet.get(getActivity());

        updateUI();


        return view;
    }


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.activity_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_data:
                // show a dialog, let user select a date before create a new level object
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(Calendar.getInstance().getTime());
                dialog.setTargetFragment(ListFragment.this, 0);
                dialog.show(manager, "Date");
                return true;
            case R.id.record:
                // todo: open a WebView
//                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://codem.xyz/"));
                Intent i = WebViewActivity.newIntent(getActivity(),URL);
                startActivity(i);
                return true;
            case R.id.custom_tab:
                // open a CustomTab instead of WebView
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setShowTitle(true);
                builder.setToolbarColor(Color.rgb(65,81,181));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getActivity(), URL);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            bloodGlucoseLevel level = new bloodGlucoseLevel(cal);
            levelSet.get(getActivity()).add(level);
            Intent intent = MainViewPager.newIntent(getActivity());
            intent.putExtra("ID",level.getID());
            startActivityForResult(intent,1);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        }

    private void updateUI(){
        Log.d("从db读取数据",set.toString());
        //set new adapter
        adapter = new Adapter(set.getList());
        recyclerView.setAdapter(adapter);

    }

    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date;
        TextView note;
        TextView avg;
        CheckBox checked;
        int ID;
        bloodGlucoseLevel level;

        public Holder(LayoutInflater inflater,ViewGroup parent){
            super(inflater.inflate(R.layout.activity_fragment,parent,false));
            // set UI widget
            date = itemView.findViewById(R.id.date);
            note = itemView.findViewById(R.id.notes);
            checked = itemView.findViewById(R.id.normal_check);
            avg = itemView.findViewById(R.id.average);
            itemView.setOnClickListener(this);
        }

        public void update(bloodGlucoseLevel level,int index){
            // update widget data
            this.level = level;
            date.setText(level.getDate());
            note.setText(level.note);
            avg.setText(""+level.getAvg());
            checked.setChecked(level.normalDay());
            ID = index;
        }

        @Override
        public void onClick(View view) {
            Intent intent = MainViewPager.newIntent(getActivity());
            intent.putExtra("ID",level.getID());
            startActivityForResult(intent,1);
        }



    }

    private class Adapter extends RecyclerView.Adapter<Holder>{
        private List<bloodGlucoseLevel> list;
        public Adapter(List<bloodGlucoseLevel> list){
            this.list = list;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new Holder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(Holder holder, int position){
            bloodGlucoseLevel level = list.get(position);
            holder.update(level,position);
        }
        @Override
        public int getItemCount(){
            return list.size();
        }


    }


}
