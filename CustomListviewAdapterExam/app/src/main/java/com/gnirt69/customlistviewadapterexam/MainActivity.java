package com.gnirt69.customlistviewadapterexam;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private ListView lvPlane;
    private PlaneListAdapter adapter;
    private List<Plane> mPlaneList;
    public Handler mHandler;
    public View ftView;
    public boolean isLoading = false;
    public int currentId=11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvPlane = (ListView)findViewById(R.id.listview_plane);

        LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);
        mHandler = new MyHandler();
        mPlaneList = new ArrayList<>();
        //Add sample data for list
        //We can get data from DB, webservice here
        mPlaneList.add(new Plane(1, "Qatar", 200, "Cluj-Timisoara"));
        mPlaneList.add(new Plane(3, "Qatar", 250, "Cluj-Bucuresti"));
        mPlaneList.add(new Plane(4, "WizzAir", 300, "Cluj-Oradea"));
        mPlaneList.add(new Plane(5, "Tarom", 350, "Satu Mare-Oradea"));
        mPlaneList.add(new Plane(6, "Tarom", 400, "Arad-Paris"));
        mPlaneList.add(new Plane(7, "WizzAir", 450, "Paris-Roma"));
        mPlaneList.add(new Plane(8, "WizzAir", 500, "Roma-LA"));
        mPlaneList.add(new Plane(9, "WizzAir", 600, "LA-Roma"));
        mPlaneList.add(new Plane(10, "Tarom", 700, "Budapesta-Chisinau"));
        mPlaneList.add(new Plane(11, "WizzAir", 800, "Chisina-Bucuresti"));

        //Init adapter
        adapter = new PlaneListAdapter(getApplicationContext(), mPlaneList);
        lvPlane.setAdapter(adapter);

        lvPlane.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Do something
                //Ex: display msg with plane id get from view.getTag
                Toast.makeText(getApplicationContext(), "Clicked plane id =" + view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });
        lvPlane.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Check when scroll to last item in listview, in this tut, init data in listview = 10 item
                if(view.getLastVisiblePosition() == totalItemCount-1 && lvPlane.getCount() >=10 && isLoading == false) {
                    isLoading = true;
                    Thread thread = new ThreadGetMoreData();
                    //Start thread
                    thread.start();
                }

            }
        });
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Add loading view during search processing
                    lvPlane.addFooterView(ftView);
                    break;
                case 1:
                    //Update data adapter and UI
                    adapter.addListItemToAdapter((ArrayList<Plane>)msg.obj);
                    //Remove loading view after update listview
                    lvPlane.removeFooterView(ftView);
                    isLoading=false;
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<Plane> getMoreData() {
        ArrayList<Plane>lst = new ArrayList<>();
        //Sample code get new data :P
        lst.add(new Plane(++currentId, "Qatar", 200, "Cluj-Timisoara"));
        lst.add(new Plane(++currentId, "Qatar", 250, "Cluj-Bucuresti"));
        lst.add(new Plane(++currentId, "WizzAir", 300, "Cluj-Oradea"));
        lst.add(new Plane(++currentId, "Tarom", 350, "Satu Mare-Oradea"));
        lst.add(new Plane(++currentId, "Tarom", 400, "Arad-Paris"));
        lst.add(new Plane(++currentId, "WizzAir", 450, "Paris-Roma"));
        lst.add(new Plane(++currentId, "WizzAir", 500, "Roma-LA"));
        return lst;
    }
    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            //Add footer view after get data
            mHandler.sendEmptyMessage(0);
            //Search more data
            ArrayList<Plane> lstResult = getMoreData();
            //Delay time to show loading footer when debug, remove it when release
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Send the result to Handle
            Message msg = mHandler.obtainMessage(1, lstResult);
            mHandler.sendMessage(msg);

        }
    }
}
