package xyz.pongsakorn.policeeye;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private final String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+
            "/PoliceEye";

    private static final int GET_INIT_AMOUNT = 10;
    private static final int GET_MORE_AMOUNT = 10;
    private static final int THRESHOLD_TO_GET_MORE = 6;
    private static final int REQ_CODE_NOTI = 44;
    SwipeRefreshLayout swipe_refresh_layout;
    RecyclerView recyclerView;
    FloatingActionButton fabSketch;
    LinearLayoutManager layoutManager;
    HistoryAdapter recycViewAdapter;
    //DefaultRecycViewOnScrollListener scrollListener;
    ArrayList<JSONObject> dd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initInstances();

        dd = new ArrayList<>();


        File dir = new File(file_path);
        if(dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = files.length-1;i >= 0;i--) {
                try {
                    JSONObject tmp = new JSONObject();
                    tmp.put("path", files[i].getAbsolutePath());
                    dd.add(tmp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (recycViewAdapter==null) {
                recycViewAdapter = new HistoryAdapter(this, dd);
                //notificationFetch.getInitNoti(GET_INIT_AMOUNT, new InitNotificationListener(act, recycViewAdapter, scrollListener));
            }
            recyclerView.setAdapter(recycViewAdapter);
        }

        /*swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notificationFetch.getInitNoti(GET_INIT_AMOUNT, new InitNotificationListener(act, recycViewAdapter, scrollListener));
            }
        });*/

        /*scrollListener = new DefaultRecycViewOnScrollListener(
                new DefaultRecycViewOnScrollListener.Builder()
                        .loadMoreThreshold(THRESHOLD_TO_GET_MORE)
                        .setLinearLayoutManager(layoutManager)) {
            @Override
            public void onLoadMore() {
                if (!recycViewAdapter.isNoMoreOldDataToFetch() && !isFetchingData) {
                    notificationFetch.getMoreNoti(recycViewAdapter.getItemCount(), GET_MORE_AMOUNT, new MoreNotificationListener(act, recycViewAdapter, scrollListener));
                }
            }
        };*/
        //recyclerView.setOnScrollListener(scrollListener);
    }

    private void initInstances() {
        swipe_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fabSketch = (FloatingActionButton) findViewById(R.id.fabSketch);

        swipe_refresh_layout.buildLayer();
        //swipe_refresh_layout.setColorSchemeResources(R.color.swipeRefreshIcon);
        swipe_refresh_layout.setEnabled(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        /*recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this,
                        DividerItemDecoration.VERTICAL_LIST,
                        R.drawable.devider_user_list));*/

        fabSketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, SketchActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==0) {
            File dir = new File(file_path);
            if(dir.exists()) {
                dd.clear();
                File[] files = dir.listFiles();
                for (int i = files.length-1;i >= 0;i--) {
                    try {
                        JSONObject tmp = new JSONObject();
                        tmp.put("path", files[i].getAbsolutePath());
                        dd.add(tmp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recycViewAdapter.notifyDataSetChanged();
            }
        }
    }
}