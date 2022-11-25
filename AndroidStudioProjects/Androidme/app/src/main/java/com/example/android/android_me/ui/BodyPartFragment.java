package com.example.android.android_me.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.android_me.R;

import java.util.ArrayList;
import java.util.List;

public class BodyPartFragment extends Fragment {

    List<Integer> mImageId ;
    int mListIndex ;

    public BodyPartFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_body_part,container,false);

        if (savedInstanceState != null){
            mImageId = savedInstanceState.getIntegerArrayList("image_id");
            mListIndex = savedInstanceState.getInt("list_index");
        }

        final ImageView bodyImageView = root.findViewById(R.id.body_part_imageView);

        if (mImageId != null){
            bodyImageView.setImageResource(mImageId.get(mListIndex));
            bodyImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListIndex < mImageId.size()-1){
                        mListIndex++;
                    }else {
                        mListIndex = 0 ;
                    }
                    bodyImageView.setImageResource(mImageId.get(mListIndex));
                }
            });
        }else {
            Log.wtf("w","t");
        }

        return root ;

    }

    public void setmImageId(List<Integer> mImageId) {
        this.mImageId = mImageId;
    }

    public void setmListIndex(int mListIndex) {
        this.mListIndex = mListIndex;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntegerArrayList("image_id",(ArrayList<Integer>) mImageId);
        outState.putInt("list_index",mListIndex);
    }

}
