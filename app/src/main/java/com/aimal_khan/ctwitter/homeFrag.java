package com.aimal_khan.ctwitter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;


public class homeFrag extends Fragment {
    Context mContext;
    RecyclerView homeTimelineRv;
    ArrayList<Integer> numOfTweets;
    ArrayList<Integer> tweetItemList;
    public homeFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getContext();
        homeTimelineRv = v.findViewById(R.id.tweetTimeLine_RV_home);
        numOfTweets = new ArrayList<>();
        tweetItemList = new ArrayList<>();

        Random r = new Random();
        int low = 1;
        int high = 4;
        for(int i = 1 ; i <= 10 ; i++){
            numOfTweets.add(i);
            int result = r.nextInt(high-low) + low;
            tweetItemList.add(result);
        }
        homeTimelineRv.setAdapter(new TweetsTimeLineAdapter(mContext,numOfTweets,tweetItemList));
        homeTimelineRv.setLayoutManager(new LinearLayoutManager(mContext));
        return v;
    }
}