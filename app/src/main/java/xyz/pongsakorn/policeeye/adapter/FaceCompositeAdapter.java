package xyz.pongsakorn.policeeye.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.activity.IdentikitActivity;

/**
 * Created by Porpeeranut on 12/2/2559.
 */

public class FaceCompositeAdapter extends RecyclerView.Adapter<FaceCompositeAdapter.FaceCompositeViewHolder> {

    ArrayList<IdentikitActivity.FacialComposite> mDataset;

    View rootView;
    long uid;
    Activity act;
    AdapterListener mListener;
    int activePos;


    public FaceCompositeAdapter(Activity act, AdapterListener mListener) {
        rootView = act.getWindow().getDecorView().findViewById(android.R.id.content);
        this.act = act;
        this.mListener = mListener;
        mDataset = new ArrayList<>();
        mDataset.add(IdentikitActivity.FacialComposite.HAIR);
        mDataset.add(IdentikitActivity.FacialComposite.EYEBROWS);
        mDataset.add(IdentikitActivity.FacialComposite.EYES);
        mDataset.add(IdentikitActivity.FacialComposite.NOSE);
        mDataset.add(IdentikitActivity.FacialComposite.MOUTH);
        mDataset.add(IdentikitActivity.FacialComposite.JAW);
        activePos = 0;
    }

    @Override
    public FaceCompositeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_face_composite, parent, false);
        return new FaceCompositeViewHolder(itemView);
    }

    /*private int getCurrentPosition(MinimalUserModel data) {
        for (int i=0;i<mDataset.size();i++) {
            if (mDataset.get(i).uid==data.uid)
                return i;
        }
        return -1;
    }*/

    @Override
    public void onBindViewHolder(final FaceCompositeViewHolder holder, final int position) {

        final IdentikitActivity.FacialComposite data = mDataset.get(position);

        int res = R.mipmap.ic_pencil;
        switch (data) {
            case JAW:
                res = R.mipmap.ic_pencil;
                holder.txtName.setText("jaw");
                break;
            case HAIR:
                res = R.mipmap.ic_pencil;
                holder.txtName.setText("hair");
                break;
            case EYEBROWS:
                res = R.mipmap.ic_pencil;
                holder.txtName.setText("eyebrows");
                break;
            case EYES:
                res = R.mipmap.ic_eraser;
                holder.txtName.setText("eyes");
                break;
            case NOSE:
                res = R.mipmap.ic_eraser;
                holder.txtName.setText("nose");
                break;
            case MOUTH:
                res = R.mipmap.ic_eraser;
                holder.txtName.setText("mouth");
                break;
        }
        if (activePos == position)
            holder.txtName.setBackgroundColor(act.getResources().getColor(R.color.background_txt_list_active));
        else
            holder.txtName.setBackgroundColor(act.getResources().getColor(R.color.background_txt_list_inactive));
        holder.imComposite.setImageResource(res);
        holder.imComposite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelect(mDataset.get(position));
                int tmp = activePos;
                activePos = position;
                notifyItemChanged(tmp);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface AdapterListener {
        void onSelect(IdentikitActivity.FacialComposite composite);
    }

    public static class FaceCompositeViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutList;
        public ImageView imComposite;
        public TextView txtName;

        public FaceCompositeViewHolder(View v) {
            super(v);
            v.setOnClickListener(null);
            layoutList = (LinearLayout) v.findViewById(R.id.layoutList);
            imComposite = (ImageView) v.findViewById(R.id.imComposite);
            txtName = (TextView) v.findViewById(R.id.txtName);
        }
    }
}