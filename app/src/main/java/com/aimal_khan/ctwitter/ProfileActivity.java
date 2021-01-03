package com.aimal_khan.ctwitter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView homeTimelineRv;
    ArrayList<Integer> numOfTweets;
    ArrayList<Integer> tweetItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        homeTimelineRv = findViewById(R.id.tweetsRv_profileA);
        numOfTweets = new ArrayList<>();
        tweetItemList = new ArrayList<>();

        ImageButton backButton = findViewById(R.id.backButton_profileA);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Random r = new Random();
        int low = 1;
        int high = 4;
        for(int i = 1 ; i <= 10 ; i++){
            numOfTweets.add(i);
            int result = r.nextInt(high-low) + low;
            tweetItemList.add(result);
        }
        homeTimelineRv.setAdapter(new TweetsTimeLineAdapter(this,numOfTweets,tweetItemList));
        homeTimelineRv.setLayoutManager(new LinearLayoutManager(this));


    }
}