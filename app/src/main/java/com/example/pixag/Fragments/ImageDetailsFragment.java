package com.example.pixag.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pixag.Activities.MainActivity;
import com.example.pixag.CustomListeners.OnScreenSwipeTouchListener;
import com.example.pixag.R;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class ImageDetailsFragment extends Fragment {

    private TextView number_likes_LBL;
    private TextView number_downloads_LBL;
    private TextView number_of_views_LBL;
    private TextView the_tags_LBL;
    private ImageView selected_imageview;
    private Button BTN_back;
    private Intent myIntent;

    public ImageDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
            super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_details, container, false);
        myIntent = getActivity().getIntent();
        HashMap<String, String> imageData = (HashMap<String, String>) myIntent.getSerializableExtra("imageData");


        number_likes_LBL = v.findViewById(R.id.number_likes_LBL);
        number_downloads_LBL = v.findViewById(R.id.number_downloads_LBL);
        number_of_views_LBL = v.findViewById(R.id.number_of_views_LBL);
        the_tags_LBL = v.findViewById(R.id.the_tags_LBL);
        selected_imageview = v.findViewById(R.id.selected_imageview);
        BTN_back = v.findViewById(R.id.BTN_back);

        number_likes_LBL.setText(imageData.get("Likes"));
        number_downloads_LBL.setText(imageData.get("Downloads"));
        number_of_views_LBL.setText(imageData.get("Views"));
        the_tags_LBL.setText(imageData.get("Tags"));
        Glide.with(getActivity()).
                load(imageData.get("ImageUrl")).
                into(selected_imageview);


        v.setOnTouchListener(new OnScreenSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                MainActivity ma = (MainActivity) getActivity();
                ma.goBack();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        BTN_back.setOnClickListener(v1 -> {
            final Animation shakeAnim = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
            v1.startAnimation(shakeAnim);
            MainActivity ma = (MainActivity) getActivity();
            ma.goBack();
        });
    }

    public interface onImageDetailsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onImageDetailsFragmentInteraction(Uri uri);
    }
}