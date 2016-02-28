package xyz.pongsakorn.policeeye.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.activity.ResultActivity;
import xyz.pongsakorn.policeeye.model.HistoryModel;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PoliceEye/";

    Context context;
    ArrayList<HistoryModel> data;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_history, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("fileName", data.get(position).fileName);
                intent.putExtra("gender", data.get(position).gender);
                intent.putExtra("note", data.get(position).note);
                intent.putExtra("name", data.get(position).name);
                intent.putExtra("people", data.get(position).people);
                context.startActivity(intent);
            }
        });
        String[] tmp = data.get(position).fileName.split("-");
        String date = tmp[2] + "/" + tmp[1] + "/" + tmp[0];
        Glide.with(context)
                .load(new File(file_path + data.get(position).fileName))
                .into(viewHolder.imSketch);
        viewHolder.txtName.setText(data.get(position).name);
        viewHolder.txtGender.setText(data.get(position).gender);
        viewHolder.txtNote.setText(data.get(position).note);
        viewHolder.txtDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout layoutItem;
        public ImageView imSketch;
        public TextView txtName;
        public TextView txtGender;
        public TextView txtNote;
        public TextView txtDate;

        private ViewHolder(View v) {
            super(v);
            layoutItem = (LinearLayout) v.findViewById(R.id.layoutItem);
            imSketch = (ImageView) v.findViewById(R.id.imSketch);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtGender = (TextView) v.findViewById(R.id.txtGender);
            txtNote = (TextView) v.findViewById(R.id.txtNote);
            txtDate = (TextView) v.findViewById(R.id.txtDate);
        }
    }
}
