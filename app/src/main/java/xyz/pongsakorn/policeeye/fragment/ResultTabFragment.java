package xyz.pongsakorn.policeeye.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.signature.StringSignature;

import xyz.pongsakorn.policeeye.R;
import xyz.pongsakorn.policeeye.utils.SketchMatchSDK;

/**
 * Created by Porpeeranut on 25/2/2559.
 */
public class ResultTabFragment extends Fragment {

    SketchMatchSDK.Person person;
    TextView txtName;
    TextView txtGender;
    TextView txtID;
    TextView txtPoint;

    public ResultTabFragment() {
    }

    @SuppressLint("ValidFragment")
    public ResultTabFragment(SketchMatchSDK.Person person) {
        this.person = person;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_result_tab, container, false);
        final ImageView image = (ImageView) rootView.findViewById(R.id.image);
        txtName = (TextView) rootView.findViewById(R.id.txtName);
        txtGender = (TextView) rootView.findViewById(R.id.txtGender);
        txtID = (TextView) rootView.findViewById(R.id.txtID);
        txtPoint = (TextView) rootView.findViewById(R.id.txtPoint);

        txtName.setText(person.name);
        txtGender.setText(person.sex.equals("M")?"Male":"Female");
        txtID.setText("("+person.id+")");
        txtPoint.setText(((int)(person.point*100))+"%");
        Glide.with(getActivity().getApplicationContext())
                .load("http://pongsakorn.xyz:8080/" + person.id)
                .signature(new StringSignature(person.id))
                .fitCenter()
                .error(R.mipmap.imagetest)
                .into(new GlideDrawableImageViewTarget(image) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        image.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                    }
                });

        return rootView;
    }
}