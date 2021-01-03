package com.aimal_khan.ctwitter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TweetsTimeLineAdapter extends RecyclerView.Adapter<TweetsTimeLineAdapter.TweetsViewHolder> {
    public static final int TWEET_ITEM  = 1;
    public static final int RETWEET_ITEM = 2;
    public static final int QUOTE_RETWEET_ITEM = 3;

    Context mContext ;
    ArrayList<Integer> numOftweets ;
    ArrayList<Integer> tweetItemType ; ;

    public TweetsTimeLineAdapter(Context mContext, ArrayList<Integer> numOftweets, ArrayList<Integer> tweetItemType) {
        this.mContext = mContext;
        this.numOftweets = numOftweets;
        this.tweetItemType = tweetItemType;
    }

    @NonNull
    @Override
    public TweetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
     //   Log.e("THIS_TYPE",viewType + " ite on Create view holder ");
        if(viewType == TWEET_ITEM){
            v = LayoutInflater.from(mContext).inflate(R.layout.tweet_item_layout, parent,false);
        }
        if(viewType == RETWEET_ITEM){
            v = LayoutInflater.from(mContext).inflate(R.layout.retweet_item_layout, parent,false);
        }
        if(viewType == QUOTE_RETWEET_ITEM){
            v = LayoutInflater.from(mContext).inflate(R.layout.quote_retweet_item_layout, parent,false);
        }
        assert v != null ;
        return new TweetsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return numOftweets.size();
    }

    public static class TweetsViewHolder extends RecyclerView.ViewHolder{

        public TweetsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
    //    Log.e("THIS_TYPE",tweetItemType.get(position) + "  in Get item Type");
        return tweetItemType.get(position);
    }
}
