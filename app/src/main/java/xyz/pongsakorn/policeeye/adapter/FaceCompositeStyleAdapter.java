package xyz.pongsakorn.policeeye.adapter;

import android.app.Activity;
import android.content.res.TypedArray;
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
 * Created by Porpeeranut on 13/2/2559.
 */

public class FaceCompositeStyleAdapter extends RecyclerView.Adapter<FaceCompositeStyleAdapter.FaceCompositeStyleViewHolder> {

    ArrayList<Integer> mDataset;

    View rootView;
    long uid;
    Activity act;
    AdapterListener mListener;
    int activePos;


    public FaceCompositeStyleAdapter(Activity act, IdentikitActivity.FacialComposite facialComposite, AdapterListener mListener) {
        rootView = act.getWindow().getDecorView().findViewById(android.R.id.content);
        this.act = act;
        this.mListener = mListener;
        initDataset(facialComposite, 0);
    }

    @Override
    public FaceCompositeStyleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_face_composite_style, parent, false);
        return new FaceCompositeStyleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FaceCompositeStyleViewHolder holder, final int position) {

        final int res = mDataset.get(position);
        holder.txtNumber.setText(position + "");
        if (activePos == position)
            holder.txtNumber.setBackgroundColor(act.getResources().getColor(R.color.background_txt_list_active));
        else
            holder.txtNumber.setBackgroundColor(act.getResources().getColor(R.color.background_txt_list_inactive));
        holder.imStyle.setImageResource(res);
        holder.imStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelect(res, position, activePos);
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

    public void initDataset(IdentikitActivity.FacialComposite faceComposite, int selectedStylePos) {
        mDataset = new ArrayList<>();
        activePos = selectedStylePos;
        TypedArray array = act.getResources().obtainTypedArray(R.array.res_jaw_styles);
        switch (faceComposite) {
            case JAW:
                array = act.getResources().obtainTypedArray(R.array.res_jaw_styles);
                break;
            case HAIR:
                array = act.getResources().obtainTypedArray(R.array.res_hair_styles);
                break;
            case EYEBROWS:
                array = act.getResources().obtainTypedArray(R.array.res_eyebrows_styles);
                break;
            case EYES:
                array = act.getResources().obtainTypedArray(R.array.res_eyes_styles);
                break;
            case NOSE:
                array = act.getResources().obtainTypedArray(R.array.res_nose_styles);
                break;
            case MOUTH:
                array = act.getResources().obtainTypedArray(R.array.res_mouth_styles);
                break;
        }
        for (int i = 0; i < array.length(); i++)
            mDataset.add(array.getResourceId(i, -1));
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onSelect(int resId, int activePos, int oldPos);
    }

    public static class FaceCompositeStyleViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layoutList;
        public ImageView imStyle;
        public TextView txtNumber;

        public FaceCompositeStyleViewHolder(View v) {
            super(v);
            v.setOnClickListener(null);
            layoutList = (LinearLayout) v.findViewById(R.id.layoutList);
            imStyle = (ImageView) v.findViewById(R.id.imStyle);
            txtNumber = (TextView) v.findViewById(R.id.txtNumber);
        }
    }
}