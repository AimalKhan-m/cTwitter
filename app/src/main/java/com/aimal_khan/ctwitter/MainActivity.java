package com.aimal_khan.ctwitter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity{

    FragmentContainerView fragContainerView;
    BottomNavigationView bottomNavView;
    NavController navController;
    FloatingActionButton mainTM_fab;
    ImageView profileAvatar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTM_fab = findViewById(R.id.mainTM_fab);
        bottomNavView = findViewById(R.id.bottomNavigationView_mainA);
        navController = Navigation.findNavController(this, R.id.fragContainerViewMainA);
        NavigationUI.setupWithNavController(bottomNavView, navController);
        profileAvatar = findViewById(R.id.profileAvatar_mainTB);



        // setup Of all Listeners
        setListeners();


    }

    private void setListeners() {

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            Log.e("CHANGED_DES_TYPE" , "THIS IS DESS " + destination.getLabel());
            if (destination.getId() == R.id.messageFrag && mainTM_fab.getTag().toString().equals("tweetIcon")) {
                animFab(R.drawable.messages_icon, "messageIcon", 180);
            } else if (mainTM_fab.getTag().toString().equals("messageIcon") && !(destination.getId() == R.id.messageFrag)) {
                animFab(R.drawable.tweet_feather_icon_white, "tweetIcon", -180);
            }
            mainToolbarChanges(destination);
        });

        profileAvatar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,ProfileActivity.class)));


    }

    public void animFab(int imgSrc, String tag, int rotateValue) {
        mainTM_fab.animate().
                rotationBy(rotateValue)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(50)
                .withEndAction(() -> {
                    mainTM_fab.setImageResource(imgSrc);
                    mainTM_fab.animate()
                            .rotationBy(rotateValue)   //Complete the rest of the rotation
                            .setDuration(50)
                            .scaleX(1)              //Scaling back to what it was
                            .scaleY(1)
                            .start();

                }).start();
        mainTM_fab.setTag(tag);

    }

    private void mainToolbarChanges(NavDestination destination){

        LinearLayout home_ll = findViewById(R.id.home_CenterToolbarLayout);
        LinearLayout search_ll = findViewById(R.id.search_CenterToolbarLayout);
        LinearLayout notification_ll = findViewById(R.id.notification_CenterToolbarLayout);
        LinearLayout message_ll = findViewById(R.id.message_CenterToolbarLayout);

        if(destination.getId() == R.id.homeFrag && home_ll.getVisibility() == View.GONE){
            home_ll.setVisibility(View.VISIBLE);
            search_ll.setVisibility(View.GONE);
            notification_ll.setVisibility(View.GONE);
            message_ll.setVisibility(View.GONE);

        }
        if(destination.getId() == R.id.TrendingSearchFrag && search_ll.getVisibility() == View.GONE){
            home_ll.setVisibility(View.GONE);
            search_ll.setVisibility(View.VISIBLE);
            notification_ll.setVisibility(View.GONE);
            message_ll.setVisibility(View.GONE);

        }
        if(destination.getId() == R.id.notificationFrag && notification_ll.getVisibility() == View.GONE){
            home_ll.setVisibility(View.GONE);
            search_ll.setVisibility(View.GONE);
            notification_ll.setVisibility(View.VISIBLE);
            message_ll.setVisibility(View.GONE);

        }
        if(destination.getId() == R.id.messageFrag && message_ll.getVisibility() == View.GONE){
            home_ll.setVisibility(View.GONE);
            search_ll.setVisibility(View.GONE);
            notification_ll.setVisibility(View.GONE);
            message_ll.setVisibility(View.VISIBLE);

        }
    }


}