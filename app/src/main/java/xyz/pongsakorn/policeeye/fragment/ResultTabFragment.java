package xyz.pongsakorn.policeeye.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.signature.StringSignature;

import xyz.pongsakorn.policeeye.R;

/**
 * Created by Porpeeranut on 25/2/2559.
 */
public class ResultTabFragment extends Fragment {

    String url;

    public ResultTabFragment(){ }

    @SuppressLint("ValidFragment")
    public ResultTabFragment(String url){
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_result_tab, container, false);
        final ImageView image = (ImageView) rootView.findViewById(R.id.image);
        final ProgressBar pb1 = (ProgressBar) rootView.findViewById(R.id.pb1);

        Glide.with(getActivity().getApplicationContext())
                .load(url)
                .signature(new StringSignature(url))
                .fitCenter()
                .error(R.mipmap.imagetest)
                .into(new GlideDrawableImageViewTarget(image) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        pb1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        pb1.setVisibility(View.GONE);
                        rootView.findViewById(R.id.txtErr).setVisibility(View.VISIBLE);
                        image.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        pb1.setVisibility(View.GONE);
                    }
                });

        return rootView;
    }
}