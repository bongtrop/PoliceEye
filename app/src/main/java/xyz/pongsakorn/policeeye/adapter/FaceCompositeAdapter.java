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

import xyz.pongsakorn.policeeye.model.FacialCompositeListModel;
import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.activity.IdentikitActivity;

/**
 * Created by Porpeeranut on 12/2/2559.
 */

public class FaceCompositeAdapter extends RecyclerView.Adapter<FaceCompositeAdapter.FaceCompositeViewHolder> {

    ArrayList<FacialCompositeListModel> mDataset;

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
        mDataset.add(new FacialCompositeListModel(IdentikitActivity.FacialComposite.HAIR));
        mDataset.add(new FacialCompositeListModel(IdentikitActivity.FacialComposite.EYEBROWS));
        mDataset.add(new FacialCompositeListModel(IdentikitActivity.FacialComposite.EYES));
        mDataset.add(new FacialCompositeListModel(IdentikitActivity.FacialComposite.NOSE));
        mDataset.add(new FacialCompositeListModel(IdentikitActivity.FacialComposite.MOUTH));
        mDataset.add(new FacialCompositeListModel(IdentikitActivity.FacialComposite.JAW));
        activePos = 0;
    }

    @Override
    public FaceCompositeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_face_composite, parent, false);
        return new FaceCompositeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FaceCompositeViewHolder holder, final int position) {

        final IdentikitActivity.FacialComposite facialComposite = mDataset.get(position).facialComposite;

        int res = R.mipmap.ic_pencil;
        switch (facialComposite) {
            case JAW:
                res = R.mipmap.jaw487;
                holder.txtName.setText("jaw");
                break;
            case HAIR:
                res = R.mipmap.hair487;
                holder.txtName.setText("hair");
                break;
            case EYEBROWS:
                res = R.mipmap.eyebrows487;
                holder.txtName.setText("eyebrows");
                break;
            case EYES:
                res = R.mipmap.eyes487;
                holder.txtName.setText("eyes");
                break;
            case NOSE:
                res = R.mipmap.nose487;
                holder.txtName.setText("nose");
                break;
            case MOUTH:
                res = R.mipmap.mouth487;
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
                mListener.onSelect(facialComposite, mDataset.get(position).selectedStylePos);
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

    public void changeSelectedStylePos(int selectedStylePos) {
        mDataset.get(activePos).setSelectedStylePos(selectedStylePos);
    }

    public interface AdapterListener {
        void onSelect(IdentikitActivity.FacialComposite composite, int selectedStylePos);
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