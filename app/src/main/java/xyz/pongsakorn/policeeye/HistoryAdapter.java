package xyz.pongsakorn.policeeye;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> data;

    public HistoryAdapter(Context context, ArrayList<JSONObject> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        try {
            String path = data.get(position).getString("path");
            String[] paths = path.split("/");
            Glide.with(context)
                    .load(new File(path))
                    .into(viewHolder.imSketch);


            viewHolder.txtName.setText(paths[paths.length-1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imSketch;
        public TextView txtName;

        private ViewHolder(View v) {
            super(v);
            this.imSketch = (ImageView) v.findViewById(R.id.imSketch);
            this.txtName = (TextView) v.findViewById(R.id.txtName);
        }
    }
}
