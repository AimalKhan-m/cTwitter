package com.aimal_khan.ctwitter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import java.util.ArrayList;

public class TrendingSearchFrag extends Fragment {
    Context mContext ;
    ArrayList<String> trendingTagArrayList ;
    ArrayList<String> numOfTimeTweetArrayList ; ;
   RecyclerView trendingTagsRv ;
   TrendingTagsAdapter tagsRvAdapter ;

    public TrendingSearchFrag() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getContext();
        MainActivity mainActivity = (MainActivity) mContext;

        trendingTagsRv = v.findViewById(R.id.trendingTags_Rv_Search);
        trendingTagArrayList = new ArrayList<>();
        numOfTimeTweetArrayList = new ArrayList<>();

        for(int i = 1 ; i <= 15 ; i++){
            numOfTimeTweetArrayList.add("100M");
        }

        trendingTagArrayList.add("this");
        trendingTagArrayList.add("there");
        trendingTagArrayList.add("khan");
        trendingTagArrayList.add("table");
        trendingTagArrayList.add("Chair");
        trendingTagArrayList.add("CubCake");
        trendingTagArrayList.add("Dance");
        trendingTagArrayList.add("TAblet");
        trendingTagArrayList.add("Laptop");
        trendingTagArrayList.add("Wall");
        trendingTagArrayList.add("Room");
        trendingTagArrayList.add("Gaming");
        trendingTagArrayList.add("Spontenously");
        trendingTagArrayList.add("the_End");

        tagsRvAdapter = new TrendingTagsAdapter(mContext,trendingTagArrayList,numOfTimeTweetArrayList);

        trendingTagsRv.setAdapter(tagsRvAdapter);
        trendingTagsRv.setLayoutManager(new LinearLayoutManager(mContext));



        assert mainActivity != null;
        SearchView trendSv = mainActivity.findViewById(R.id.trendsSearchView);

        trendSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                tagsRvAdapter.getFilter().filter(newText);

                return false;
            }
        });


        return v ;
    }
}