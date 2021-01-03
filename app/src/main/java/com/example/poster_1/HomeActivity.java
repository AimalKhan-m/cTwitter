package com.example.poster_1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poster_1.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.poster_1.PostsAdapter.KEY_POST_TYPE_QUOTE_TWEET;
import static com.example.poster_1.PostsAdapter.KEY_POST_TYPE_TWEET;

public class HomeActivity extends AppCompatActivity implements com.example.poster_1.AddPostDialogue.AddPostTextListener {

    public static final String KEY_POSTS = "Posts";
    public static final String KEY_QUOTE_TWEETS = "QTweets";
    ActivityHomeBinding homeBinding;
    public static   FirebaseUser cUser = FirebaseAuth.getInstance().getCurrentUser();;
    FirebaseFirestore fbFs = FirebaseFirestore.getInstance();
    List<com.example.poster_1.PostModel> postList;
    RecyclerView postsRv;
    com.example.poster_1.PostsAdapter postsAdapter;
    EventListener<QuerySnapshot> postChangeListener;
    public static final String KEY_USER_ID = "userID";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_POST_TEXT = "postText";
    public static final String KEY_POSTED_TIME = "postedTime";
    private String postType = "";
    private String postID = "";
    AtomicBoolean isFirstLoad = new AtomicBoolean(true) ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());
        cUser = FirebaseAuth.getInstance().getCurrentUser();
        postList = new ArrayList<>();
        homeBinding.uNameHomeA.setText(cUser.getDisplayName());
        postsAdapter = new com.example.poster_1.PostsAdapter(this, postList);
        postsRv = homeBinding.postsRv;
        postsRv.setLayoutManager(new LinearLayoutManager(this));
        postsRv.setAdapter(postsAdapter);
        homeBinding.addaPostFabHome.setOnClickListener(v -> {
            showaAddPostDialogue(KEY_POST_TYPE_TWEET, null);
        });


        postChangeListener = (value, error) -> {
            if (error != null) {
                Toast.makeText(com.example.poster_1.HomeActivity.this, "ERROR" + error.toString(), Toast.LENGTH_SHORT).show();
            } else {
                assert value != null;
                for (DocumentChange dc : value.getDocumentChanges()) {
                    //    Log.e("loggg_home_change", value.getDocumentChanges().size() + " SIZE < - >  change " + dc.getType());
                    switch (dc.getType()) {
                        case ADDED:
                            if(!isFirstLoad.get()){
                                postList.add(0,dc.getDocument().toObject(com.example.poster_1.PostModel.class));
                                 postsAdapter.notifyItemInserted(0);
                                //postsAdapter.notifyDataSetChanged();
                                Log.e("loggg_home_change", postList.size() + " >>> POST LIST SIZE +++++++ ADDED DOC");
                                isFirstLoad.set(false);
                            }
                            break;
                        case REMOVED:
                            com.example.poster_1.PostModel pm = dc.getDocument().toObject(com.example.poster_1.PostModel.class);

                            int i = 0;
                            for (com.example.poster_1.PostModel pM : postList) {
                                if ((pM.getPostText().equals( dc.getDocument().getString(KEY_POST_TEXT)) && pM.getUserID().equals( dc.getDocument().getString(KEY_USER_ID)))) {
                                    postList.remove(i);
                                  postsAdapter.notifyItemRemoved(i);

                                    Log.e("loggg_home_R_DATA", "\n ----- " +  pm.getPostText().trim()  + "\n " );

                                    Log.e("loggg_home_R_DATA", "\n ----- " +  pM.getPostText().trim()  + "\n " );

                                    Log.e("loggg_home_change_R",  postList.size() + "------ REMOVE REPOST AT ---- : " + i);

                                  //  postsAdapter.notifyDataSetChanged();

                                    break;
                                }
                                i++;
                            }


                            break;

                    }

                }
            }
        };

        fbFs.collection(KEY_POSTS)
                .orderBy(KEY_POSTED_TIME, Query.Direction.DESCENDING)
                .addSnapshotListener(this, postChangeListener);

        if(isFirstLoad.get()){
            Log.e("FIRST", "LOSD FIRET  "  + isFirstLoad.get() );
            loadPosts();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
      Toast.makeText(this,cUser.getDisplayName().toString() + "is SignedIn",Toast.LENGTH_SHORT).show();

    }

    private void loadPosts() {
        Log.e("logggg_home", "IN LPOST === " + postList.size());

        fbFs.collection(KEY_POSTS)
                .orderBy(KEY_POSTED_TIME, Query.Direction.DESCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
            Log.e("logggg_home", "IN LPOST === " + queryDocumentSnapshots.size());

            if (queryDocumentSnapshots.size() != 0) {
                postList.clear();
                for (QueryDocumentSnapshot qDs : queryDocumentSnapshots) {
                    if (qDs.exists()) {
                        com.example.poster_1.PostModel pm = qDs.toObject(com.example.poster_1.PostModel.class);
                        postList.add(pm);
                        Log.e("logggg_home", " POST TEXT " + pm.getPostText());

                    }
                    postsAdapter.notifyDataSetChanged();

                }
                Log.e("logggg_home", "LIST SIZE " + postList.size());
            }
            isFirstLoad.set(false);
        });

    /*    fbFs.collection(KEY_POSTS)
                .addSnapshotListener((value, error) -> {
                    QueryDSnapshot qs = value;
                    if(!qs.isEmpty()){
                        postList.add( (PostModel) qs.toObjects(PostModel.class));
                        postsAdapter.notifyDataSetChanged();
                    }
                });*/
    }

    public void showaAddPostDialogue(String postType, String postID) {
        this.postType = postType;
        this.postID = postID;
        com.example.poster_1.AddPostDialogue addPostD = new com.example.poster_1.AddPostDialogue();
        addPostD.setCancelable(false);
        addPostD.show(getSupportFragmentManager(), "Add Post");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logOutBtn_homeA: {
                logOutUser();
            }
        }
    }

    private void logOutUser() {

        FirebaseAuth.getInstance().signOut();
       if(FirebaseAuth.getInstance().getCurrentUser() == null) {
           finish();
           Toast.makeText(this, cUser.getDisplayName().toString() + "is SignedOut", Toast.LENGTH_SHORT).show();
           startActivity(new Intent(this, MainActivity.class));
       }
    }

    @Override
    public void addPostTextListener(String postText) {
        Toast.makeText(this, postText, Toast.LENGTH_SHORT).show();
        Log.e("loggg_QUOTED", "MADE  IN HOME " + postType + " <><><><> ID " + postID);
        if (postType.equals(KEY_POST_TYPE_TWEET)) {
            com.example.poster_1.PostModel post = new com.example.poster_1.PostModel(cUser.getUid(), cUser.getDisplayName(), postText, KEY_POST_TYPE_TWEET, new Date(System.currentTimeMillis()));
            fbFs.collection(KEY_POSTS).document().set(post);
        } else if (postType.equals(KEY_POST_TYPE_QUOTE_TWEET)) {
            com.example.poster_1.PostModel post = new com.example.poster_1.PostModel(cUser.getUid(), cUser.getDisplayName(), postText, KEY_POST_TYPE_QUOTE_TWEET, new Date(System.currentTimeMillis()), postID);
            DocumentReference qtDr = fbFs.collection(KEY_POSTS).document();
            qtDr.set(post).addOnSuccessListener(aVOid -> {
                qtDr.get().addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put(qtDr.getId(), cUser.getUid());
                    fbFs.collection(KEY_QUOTE_TWEETS).document(postID).set(data, SetOptions.merge());
                });
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cUser = null;


    }
}