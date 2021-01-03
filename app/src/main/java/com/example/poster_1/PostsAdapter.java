package com.example.poster_1;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.poster_1.HomeActivity.KEY_POSTS;
import static com.example.poster_1.HomeActivity.KEY_POST_TEXT;
import static com.example.poster_1.HomeActivity.KEY_QUOTE_TWEETS;
import static com.example.poster_1.HomeActivity.KEY_USER_ID;
import static com.example.poster_1.HomeActivity.KEY_USER_NAME;
import static com.example.poster_1.HomeActivity.cUser;

public class PostsAdapter extends RecyclerView.Adapter<com.example.poster_1.PostsAdapter.PostsViewHolder> {


    public static final String KEY_LIKE = "Likes";
    public static final String KEY_POST_TYPE = "postType";
    public static final String KEY_POST_TYPE_TWEET = "tweet";
    public static final String KEY_POST_TYPE_RETWEET = "retweet";
    public static final String KEY_POST_TYPE_QUOTE_TWEET = "quote_tweet";
    public static final String KEY_RETWEETS = "Retweets";
    public static final String KEY_POST_Q_POSTID = "quotedPostID";



    Context mContext;
    List<com.example.poster_1.PostModel> postsList;
    FirebaseFirestore fbFs = FirebaseFirestore.getInstance();

    public PostsAdapter(Context mContext, List<com.example.poster_1.PostModel> postsList) {
        this.mContext = mContext;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public com.example.poster_1.PostsAdapter.PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
      /*  // tweet
        if (viewType == 1) {
            v = LayoutInflater.from(mContext).inflate(R.layout.post_item_layout, parent, false);
        }
        //retweet
        else if (viewType == 2) {
            v = LayoutInflater.from(mContext).inflate(R.layout.re_posted_layout_item, parent, false);
        }
        //quoteTweet
        else {
            v = LayoutInflater.from(mContext).inflate(R.layout.quoted_post_layout_item, parent, false);
        }*/
        v = LayoutInflater.from(mContext).inflate(R.layout.quoted_post_layout_item, parent, false);
        assert v != null;
        return new com.example.poster_1.PostsAdapter.PostsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        Log.e("Log_holderCall", "POSITION + " + position);
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return postsList.size() > 0 ? postsList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        String postType = postsList.size() < 0 ? KEY_POST_TYPE_TWEET : postsList.get(position).getPostType();
        if (postType.equals(KEY_POST_TYPE_TWEET)) {
            return 1;
        } else if ((postType.equals(KEY_POST_TYPE_RETWEET))) {
            return 2;
        } else {
            return 3;
        }

    }

    public class PostsViewHolder extends RecyclerView.ViewHolder {

        int position;
        ListenerRegistration likeSnapSListener = null;
        ListenerRegistration islikedSnapSListener = null;
        ListenerRegistration rtSnapSListener = null;
        ListenerRegistration isRtSnapSListener = null;
        ListenerRegistration qtSnapSListener = null;


        TextView mUserNameTv, mPostTimeTv, mPostTexTv, mPostTypeTv;
        Button pLikeBtn, rePostBtn, quoteReTweetBtn;
        String postID;
        com.example.poster_1.PostModel post;
        LinearLayout quoteT_LL;


        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            //Common Widgets
            pLikeBtn = itemView.findViewById(R.id.tweetLikeIB);
            rePostBtn = itemView.findViewById(R.id.tweetReTweetIB);
            quoteReTweetBtn = itemView.findViewById(R.id.tweetCommentIB);
            mUserNameTv = itemView.findViewById(R.id.postUserName_playout);
            mPostTimeTv = itemView.findViewById(R.id.postDateTv_playout);
            mPostTexTv = itemView.findViewById(R.id.postText_playout);
            mPostTypeTv = (TextView) itemView.findViewById(R.id.postTypeTextTv);
            quoteT_LL = (LinearLayout) itemView.findViewById(R.id.reTweet_LL_layout);

        }

        public void setData(int position) {
            this.position = position;
            post = postsList.get(position);


            Log.e("loggg_postTYPE", "{{{TYPE ---" + post.getPostType() + "\n POST TEXT --- : PT : " + post.getPostText() + ": QID : " + post.getQuotedPostID() + "\n {{[[ POSITION :: " + position
                    + "\n ==================================================================================== \n") ;

            String postType = postsList.get(position).getPostType();
            if (postType.equals(KEY_POST_TYPE_TWEET)) {
                quoteT_LL.setVisibility(View.GONE);
                setTweetData(itemView);
            } else if ((postType.equals(KEY_POST_TYPE_RETWEET))) {
                setReTweetData(itemView);
            } else {
                setQReTweetData(itemView);

            }
            mPostTimeTv.setText(post.getPostedTime().toString());
            mPostTypeTv.setText(post.getPostType());
        }


        public void setTweetData(View itemView) {
            if (postsList.size() >= 1) {
                mUserNameTv.setText(post.getUserName());
                mPostTexTv.setText(post.getPostText());

                //<BUTTONS
                fbFs.collection(KEY_POSTS)
                        .orderBy(HomeActivity.KEY_POSTED_TIME, Query.Direction.DESCENDING)
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    DocumentSnapshot mCurrentTPostDs = queryDocumentSnapshots.getDocuments().get(position);
                    String postID = mCurrentTPostDs.getId();
                    Log.e("Loggg_TT", "in LIKE POst ID" + postID + " POsition " + position);
                    tweetBtnsFunction(postID);


                });


            }


        }
        //
        public void setReTweetData(View itemView) {

            LinearLayout tag_LL = (LinearLayout) itemView.findViewById(R.id.retweetTagTv_LL_RTlayout);
            tag_LL.setVisibility(View.VISIBLE);
            TextView reTweetTagTv = itemView.findViewById(R.id.retweetTagTv_RTlayout);
            if (post.getUserID().equals(cUser.getUid())) {
                reTweetTagTv.setText("You" + " Retweeted");
            } else {
                reTweetTagTv.setText(post.getUserName() + " Retweeted");
            }


            fbFs.collection(KEY_POSTS).document(post.getQuotedPostID()).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot postDs = task.getResult();
                            mUserNameTv.setText(postDs.getString(KEY_USER_NAME));
                            mPostTexTv.setText(postDs.getString(KEY_POST_TEXT) + "\n(RETWEETED)");
                            com.example.poster_1.PostModel post = postDs.toObject(com.example.poster_1.PostModel.class);
                            String postID = postDs.getId();
                            if (postDs.getString(KEY_POST_TYPE).equals(KEY_POST_TYPE_QUOTE_TWEET)) {
                                setQuotePostData(itemView, post);
                                Log.e("Loggg_RT  ", post.getPostType() + "]]]\n>>>> POst ID" + postID + "\n >>>> POST TEXT >>> " + post.getPostText());
                                tweetBtnsFunction(postID);

                            } else if (postDs.getString(KEY_POST_TYPE).equals(KEY_POST_TYPE_TWEET)) {
                                quoteT_LL.setVisibility(View.GONE);
                                Log.e("Loggg_RT  ", post.getPostType() + "]]]\n>>>> POst ID" + postID + "\n >>QT>> POST TEXT >>> " + post.getPostText());
                                tweetBtnsFunction(postID);
                            }
                            // BTNS Function
                            //    reTweetsBtnsFunct(task);

                        } else {
                            mPostTexTv.setText("POST DOESN'T EXIT");
                        }

                    });


        }
        //
        public void setQReTweetData(View itemView) {
            if (postsList.size() >= 1) {

                setQuotePostData(itemView, post);
                //<BUTTONS
                fbFs.collection(KEY_POSTS)
                        .orderBy(HomeActivity.KEY_POSTED_TIME, Query.Direction.DESCENDING)
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    postID = queryDocumentSnapshots.getDocuments().get(position).getId();
                    Log.e("Loggg_QT  ", post.getPostType() + "]]QT]\n>>>> POst ID" + postID + "\n >>>> POST TEXT >>> " + post.getPostText());
                    tweetBtnsFunction(postID);

                     /*    if (queryDocumentSnapshots.getDocuments().get(position).exists()) {
                        //<TWEET LIKE BTN
                        pLikeBtn.setOnClickListener(v -> {
                            fbFs.collection(KEY_LIKE).document(postID)
                                    .get()
                                    .addOnCompleteListener(task -> {
                                        if (task.isComplete()) {
                                            DocumentSnapshot like_Doc = task.getResult();
                                            if (like_Doc.getBoolean(cUser.getUid()) == null) {
                                                Map<String, Object> data = new HashMap<>();
                                                data.put(cUser.getUid(), true);
                                                fbFs.collection(KEY_LIKE).document(postID).set(data, SetOptions.merge());

                                            } else {
                                                fbFs.collection(KEY_LIKE).document(postID).update(cUser.getUid(), FieldValue.delete());

                                            }
                                        }
                                    });
                        });
                        fbFs.collection(KEY_LIKE).document(postID)
                                .addSnapshotListener((AppCompatActivity) mContext, (value, error) -> {
                                    Log.e("Loggg_likeQT", "i am in Like SIZE : POst ID" + postID);

                                    if (error != null) {
                                        Toast.makeText(mContext, error.toString() + " In Like", Toast.LENGTH_SHORT).show();

                                    } else {
                                        assert value != null;
                                        if (value.exists() && Objects.requireNonNull(value.getData()).size() > 0) {
                                            Log.e("Loggg_likeSizw", "Like SIZE : " + value.getData().size());
                                            pLikeBtn.setText("QT" + value.getData().size());
                                        } else {
                                            pLikeBtn.setText("QT");

                                        }
                                    }

                                });
                        // RETWEET BTN
                        fbFs.collection(KEY_RETWEETS).document(postID)
                                .get().addOnCompleteListener(tast0 -> {
                            if (tast0.isComplete()) {
                                //<RETWEET BTN
                                rePostBtn.setOnClickListener(v -> {
                                    fbFs.collection(KEY_POSTS)
                                            .whereEqualTo(KEY_USER_ID, cUser.getUid())
                                            .whereEqualTo(KEY_POST_Q_POSTID, postID)
                                            .whereEqualTo(KEY_POST_TYPE, KEY_POST_TYPE_RETWEET)
                                            .get().addOnCompleteListener(task2 -> {
                                        if (task2.getResult().getDocuments().size() >= 1) {
                                            fbFs.collection(KEY_RETWEETS).document(postID).update(cUser.getUid(), FieldValue.delete());
                                            for (QueryDocumentSnapshot qds : task2.getResult()) {
                                                Log.e("Loggg_rePOst ADDED", "REPOSTS FOR LOOP IDS : + " + qds.getId());

                                                fbFs.collection(KEY_POSTS).document(qds.getId()).delete();
                                            }
                                        } else {
                                            PostModel post = new PostModel(cUser.getUid(), cUser.getDisplayName(), "",
                                                    KEY_POST_TYPE_RETWEET, new Date(System.currentTimeMillis()), postID);
                                            fbFs.collection(KEY_POSTS).document().set(post).addOnSuccessListener(pQueryS -> {
                                                Map<String, Object> data = new HashMap<>();
                                                data.put(cUser.getUid(), true);
                                                Log.e("Loggg_rePOst ADDED", " +++++++ RETWEET POST  ID + " + postID);
                                                fbFs.collection(KEY_RETWEETS).document(postID).set(data, SetOptions.merge());

                                     */
                    /*   fbFs.collection(KEY_RETWEETS).document(postID)
                                                .get().addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot retweeterDoc = task.getResult();
                                                if (retweeterDoc.getBoolean(cUser.getUid()) == null) {
                                                    Map<String, Object> data = new HashMap<>();
                                                    data.put(cUser.getUid(), true);
                                                    fbFs.collection(KEY_RETWEETS).document(postID).set(data, SetOptions.merge());

                                                } else {
                                                    fbFs.collection(KEY_RETWEETS).document(postID).update(cUser.getUid(), FieldValue.delete());

                                                }

                                            }
                                        });*/
                    /*


                                            });
                                        }
                                    });


                                });
                                fbFs.collection(KEY_RETWEETS).document(postID)
                                        .addSnapshotListener((AppCompatActivity) mContext, (value, error) -> {
                                            Log.e("Loggg_likeSizw", "i am in RETWEETS POst ID" + postID);
                                            if (error != null) {
                                                Toast.makeText(mContext, error.toString() + " In RETWEET", Toast.LENGTH_SHORT).show();

                                            } else {
                                                assert value != null;
                                                if (value.exists() && Objects.requireNonNull(value.getData()).size() > 0) {
                                                    Log.e("Loggg_likeSizw", "RETWEETS SIZE : " + value.getData().size());
                                                    rePostBtn.setText("" + value.getData().size());
                                                } else {
                                                    rePostBtn.setText("");

                                                }
                                            }

                                        });
                            }
                        });
                        // QUOTED TWEET
                        quoteReTweetBtn.setOnClickListener(v -> {
                            HomeActivity hA = (HomeActivity) mContext;
                            hA.showaAddPostDialogue(KEY_POST_TYPE_QUOTE_TWEET, postID);

                        });

                    }*/

                });
            }
        }
        private void setQuotePostData(View itemView, com.example.poster_1.PostModel post) {
            mUserNameTv.setText(post.getUserName());
            mPostTexTv.setText(post.getPostText());
            quoteT_LL.setVisibility(View.VISIBLE);
            TextView rpUserName, rpTimeStmp, rpText;
            rpUserName = itemView.findViewById(R.id.qRePostUN);
            rpTimeStmp = itemView.findViewById(R.id.qRePostDateTv_playout);
            rpText = itemView.findViewById(R.id.qRePostTextv);

            fbFs.collection(KEY_POSTS).document(post.getQuotedPostID())
                    .get().addOnSuccessListener(documentSnapshot -> {
                Log.e("loggg_QUOTED", "<><QUOTEED POST ><><MADE  IN ADAPTER  " + documentSnapshot.getId());
                if (documentSnapshot.exists()) {
                    com.example.poster_1.PostModel rpModel = documentSnapshot.toObject(com.example.poster_1.PostModel.class);
                    rpUserName.setText(rpModel.getUserName());
                    rpTimeStmp.setText(rpModel.getPostedTime().toString());
                    rpText.setText(rpModel.getPostText());
                }
            });

        }
        //
        private void tweetBtnsFunction(String postID) {
            Log.e("Loggg_TT_BTNFUN", "AT POSITION : " + position + "\n POST TYPE : " + post.getPostType()
                    + "\n POst ID : " + postID + "\n\n>>>> POST DATA : >>>>>\n " + post.getPostText() +
                    "\n post TIME  : " + post.getPostedTime().toString() +
                    ""
                    +
                    "\n |||||||||||||||||||||||||||||||||||");


            //<TWEET LIKE BTN
            if(islikedSnapSListener != null){
                islikedSnapSListener.remove();
            }
            islikedSnapSListener = fbFs.collection(KEY_LIKE).document(postID).addSnapshotListener((documentSnapshot,e) -> {
                if(e==null){
                    if(documentSnapshot.exists()){
                        if(documentSnapshot.getBoolean(cUser.getUid()) != null){
                            pLikeBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.likeBtnC));
                        }else{
                            pLikeBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btnBlueC));
                        }
                    }
                    else{
                        pLikeBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btnBlueC));
                    }
                }
            });


            pLikeBtn.setOnClickListener(v -> {
                fbFs.collection(KEY_LIKE).document(postID)
                        .get()
                        .addOnCompleteListener(task -> {
                            Log.e("loggg_SetLIKE ", "\n position :" + position + "\n ID : " + postID);
                            if (task.isSuccessful()) {
                                DocumentSnapshot like_Doc = task.getResult();
                                if (like_Doc.getBoolean(cUser.getUid()) == null) {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put(cUser.getUid(), true);
                                    fbFs.collection(KEY_LIKE).document(postID).set(data, SetOptions.merge());
                                    pLikeBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.likeBtnC));

                                } else {
                                    fbFs.collection(KEY_LIKE).document(postID).update(cUser.getUid(), FieldValue.delete());
                                    pLikeBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btnBlueC));

                                }
                            }
                        });
            });
            EventListener<DocumentSnapshot> rtEventListener = (value, error) -> {
                Log.e("Loggg_TT_LIKEBTN",position +  " :|:  i am in Like SIZE : POst ID : " + postID.toString().substring(0,4) + " : P=txt : "  + post.getPostText() + " : TP : "  +  post.getPostType() + " | Q : " + post.getQuotedPostID() );

                if (error != null) {
                    Toast.makeText(mContext, error.toString() + " In Like", Toast.LENGTH_SHORT).show();

                } else {
                    assert value != null;
                    if (value.exists() && Objects.requireNonNull(value.getData()).size() > 0) {
                        Log.e("Loggg_TT", "Like SIZE : " + value.getData().size());
                        //pLikeBtn.setText(postID.toString().substring(0,4) + " : " + value.getData().size());
                        pLikeBtn.setText( value.getData().size() + "");
                    } else {
                        pLikeBtn.setText("");

                    }
                }

            };

            if(likeSnapSListener != null){
                likeSnapSListener.remove();
            }
            likeSnapSListener = fbFs.collection(KEY_LIKE).document(postID).addSnapshotListener((AppCompatActivity) mContext, rtEventListener);



            // RETWEET BTN

            if(isRtSnapSListener != null){
                isRtSnapSListener.remove();
            }
            isRtSnapSListener = fbFs.collection(KEY_RETWEETS).document(postID).addSnapshotListener((documentSnapshot,e)-> {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.getBoolean(cUser.getUid()) != null){
                        rePostBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rtGreenC));
                    }else{
                        rePostBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btnBlueC));
                    }
                }
                else{
                    rePostBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btnBlueC));
                }
            });

            if(rtSnapSListener != null){
                rtSnapSListener.remove();
            }
             fbFs.collection(KEY_RETWEETS).document(postID).get().addOnCompleteListener(tast0 -> {
                if (tast0.isComplete()) {
                    //<RETWEET BTN
                    rePostBtn.setOnClickListener(v -> {
                        assert cUser != null;
                        fbFs.collection(KEY_POSTS)
                                .whereEqualTo(KEY_USER_ID, cUser.getUid())
                                .whereEqualTo(KEY_POST_Q_POSTID, postID)
                                .whereEqualTo(KEY_POST_TYPE, KEY_POST_TYPE_RETWEET)
                                .get().addOnCompleteListener(task2 -> {
                            if (task2.getResult().getDocuments().size() >= 1 ) {

                                DialogInterface.OnClickListener ynClcik  = (d,w) -> {
                                    switch(w){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Log.e("Loggg_TT ADDED", "-------- DELETE RE_POST ID + " + postID);
                                            fbFs.collection(KEY_RETWEETS).document(postID).update(cUser.getUid(), FieldValue.delete());
                                            for (QueryDocumentSnapshot qds : task2.getResult()) {
                                                Log.e("Loggg_TT ADDED", "REPOSTS FOR LOOP IDS : + " + qds.getId());
                                                fbFs.collection(KEY_POSTS).document(qds.getId()).delete();
                                            }
                                            rePostBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btnBlueC));

                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:

                                            break ;
                                    }
                                };
                                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                                ab.setTitle("Cancel Retweet")
                                        .setMessage("Are u Sure ?")
                                        .setPositiveButton("Yes",ynClcik)
                                        .setNegativeButton("No",ynClcik)
                                        .show();


                            } else {
                                    Log.e("Loggg_TT ADDED", " +++++++ RE_POST ID + " + postID);

                                    com.example.poster_1.PostModel post = new com.example.poster_1.PostModel(cUser.getUid(), cUser.getDisplayName(), "",
                                            KEY_POST_TYPE_RETWEET, new Date(System.currentTimeMillis()), postID);
                                    fbFs.collection(KEY_POSTS).document().set(post).addOnSuccessListener(pQueryS -> {
                                        Map<String, Object> data = new HashMap<>();
                                        data.put(cUser.getUid(), true);
                                        Log.e("Loggg_TT ADDED", " +++++++ RETWEET POST  ID + " + postID);
                                        fbFs.collection(KEY_RETWEETS).document(postID).set(data, SetOptions.merge());
                                    });
                                rePostBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rtGreenC));

                            }
                        });
                    });
                    rtSnapSListener  =  fbFs.collection(KEY_RETWEETS).document(postID).addSnapshotListener((AppCompatActivity) mContext, (value, error) -> {
                        Log.e("Loggg_TT", "i am in RETWEETS POst ID" + postID);
                        if (error != null) {
                            Toast.makeText(mContext, error.toString() + " In RETWEET", Toast.LENGTH_SHORT).show();
                        } else {
                            assert value != null;
                            if (value.exists() && Objects.requireNonNull(value.getData()).size() > 0) {
                                Log.e("Loggg_likeSizw", "RETWEETS SIZE : " + value.getData().size());
                                rePostBtn.setText( value.getData().size() + "");
                            } else {
                                rePostBtn.setText("");

                            }
                        }

                    });
                }
            });



            // QUOTED TWEET
            if(qtSnapSListener != null){
                qtSnapSListener.remove();
            }
            quoteReTweetBtn.setOnClickListener(v -> {
                HomeActivity hA = (HomeActivity) mContext;
                hA.showaAddPostDialogue(KEY_POST_TYPE_QUOTE_TWEET, postID);

            });
            qtSnapSListener  = fbFs.collection(KEY_QUOTE_TWEETS).document(postID).addSnapshotListener((AppCompatActivity) mContext, (v, e) -> {
                assert v != null;
                if (e != null) {
                    Toast.makeText(mContext, e.toString() + " In RETWEET", Toast.LENGTH_SHORT).show();
                } else {
                    assert v != null;
                    if (v.exists() && Objects.requireNonNull(v.getData()).size() > 0) {
                        quoteReTweetBtn.setText(v.getData().size() + "");
                    } else {
                        quoteReTweetBtn.setText("");
                    }
                    Log.e("loggg_SetQTWEET ", "\n position :" + position + "\n ID : " + postID + "\n Q_SIZE : " + quoteReTweetBtn.getText());

                }
            });
            quoteReTweetBtn.setBackgroundColor(ContextCompat.getColor(mContext, R.color.btnBlueC));

        }




    }


}
