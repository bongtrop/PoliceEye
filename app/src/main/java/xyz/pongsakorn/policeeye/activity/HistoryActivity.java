package xyz.pongsakorn.policeeye.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import xyz.pongsakorn.policeeye.adapter.HistoryAdapter;
import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.model.HistoryModel;
import xyz.pongsakorn.policeeye.utils.DatabaseHandler;

public class HistoryActivity extends AppCompatActivity {

    private final String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +
            "/PoliceEye";
    public static final int RESULT_DELETE = 99;

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
    ArrayList<HistoryModel> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initInstances();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        DatabaseHandler db = new DatabaseHandler(HistoryActivity.this);
        historyList = db.getAllHistory();
        if (recycViewAdapter == null) {
            recycViewAdapter = new HistoryAdapter(HistoryActivity.this, historyList);
            //notificationFetch.getInitNoti(GET_INIT_AMOUNT, new InitNotificationListener(act, recycViewAdapter, scrollListener));
        }
        recyclerView.setAdapter(recycViewAdapter);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED)) {
            finish();
        }
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

        /*fabSketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*Intent intent = new Intent(HistoryActivity.this, SketchActivity.class);
                startActivityForResult(intent, 0);*//*

                Intent intent = new Intent(HistoryActivity.this, IdentikitActivity.class);
                startActivityForResult(intent, 0);

                //openApp(HistoryActivity.this, "com.adsk.sketchbookhdexpress");
                //openApp(HistoryActivity.this, "com.sonymobile.sketch");
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("his", "req " + requestCode + ", res " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_DELETE) {
                DatabaseHandler db = new DatabaseHandler(HistoryActivity.this);
                historyList = db.getAllHistory();
                recycViewAdapter.updateData(historyList);
                recycViewAdapter.notifyDataSetChanged();
            }
        }
    }

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
