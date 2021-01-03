package com.aimal_khan.ctwitter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrendingTagsAdapter extends RecyclerView.Adapter<TrendingTagsAdapter.TrendingTagItemViewHolder> implements Filterable {



    Context mContext ;
    ArrayList<String> trendingTagList;
    List<String> tglf ;
    ArrayList<String> numOfTimeTweetList ;

    public TrendingTagsAdapter(Context mContext, ArrayList<String> trendingTagList, ArrayList<String> numOfTimeTweetList) {
        this.mContext = mContext;
        this.trendingTagList = trendingTagList;
        this.numOfTimeTweetList = numOfTimeTweetList;
        tglf = new ArrayList<>(trendingTagList) ;

    }

    @NonNull
    @Override
    public TrendingTagsAdapter.TrendingTagItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;

            v = LayoutInflater.from(mContext).inflate(R.layout.trending_tag_item, parent,false);

        assert v != null ;
        return new TrendingTagsAdapter.TrendingTagItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingTagItemViewHolder holder, int position) {
            holder.trendingTagTv.setText(trendingTagList.get(position));
            holder.numoftweeted.setText(numOfTimeTweetList.get(position));
    }

    @Override
    public int getItemCount() {
        return trendingTagList.size();
    }



    public static class TrendingTagItemViewHolder extends RecyclerView.ViewHolder{
            TextView numoftweeted , trendingTagTv ;
        public TrendingTagItemViewHolder(@NonNull View itemView) {
            super(itemView);

            numoftweeted = itemView.findViewById(R.id.numOfTimesTweeted_item);
            trendingTagTv = itemView.findViewById(R.id.trendingTag_item);

        }
    }

    // FilterIng Funtions

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filteredTagsResult = new FilterResults();
            ArrayList<String> filteredList = new ArrayList<>();
            Log.e("constrain_TAG","Size {{{ " + tglf.size()  +"  >>> SIZE <<<< " );

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(tglf);

            }
            else {
                String query = constraint.toString().toLowerCase().trim();

                for (String trendingTag : tglf){
              //      Log.e("constrain_TAG","Size {{{ " + query  +" --- if ka Trending QUERT ?????   )))) " );

                    if(trendingTag.toLowerCase().contains(query)){
                    //    Log.e("constrain_TAG","Size {{{ " + trendingTag  +" --- if ka Trending TAG ))))  }}} " );

                        filteredList.add(trendingTag);
                    }
                }

            }

            filteredTagsResult.values = filteredList ;
            return filteredTagsResult;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            trendingTagList.clear();
            ArrayList<String> newResult = (ArrayList<String>) results.values;
            trendingTagList.addAll(newResult);

            Log.e("constrain_TAG","Size {{{ DATA CHANGED ------------------- " );
            notifyDataSetChanged();


        }
    };

}
