package com.example.poster_1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.poster_1.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String KEY_USER_DISPLAY_NAME = "userName";
    public static final String KEY_USER_DOB = "userDOB";

    TextView dateTextView;
    Calendar now;
    DatePickerDialog dpd;
    Boolean isSignUp = true;
    ActivityMainBinding mainBinding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore fbFs = FirebaseFirestore.getInstance();
    FirebaseUser cUser;
    String uName = "" , uDOB = ""  , uEmail  = "" , uPassword = ""  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());


        now = Calendar.getInstance();
        dateTextView = findViewById(R.id.uDOBTV_sUp);
        dpd = DatePickerDialog.newInstance(
                (DatePickerDialog.OnDateSetListener) com.example.poster_1.MainActivity.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );



    }

    @Override
    protected void onStart() {
        super.onStart();
        cUser = auth.getCurrentUser();
        if(cUser != null){
            startActivity(new Intent(this, com.example.poster_1.HomeActivity.class));
        }
    }

    public void onClick(View v){

        switch (v.getId()){
            case R.id.uDOBTV_sUp:
                mainBinding.uDOBTVSUp.setClickable(false);
                dpd.show(getSupportFragmentManager(), "Datepickerdialog");

                break;
            case R.id.togleBtn_sigUp_Lin:
                switchBwLoginSignUp();
                break;
            case R.id.signUpBtn:
                signUpUser();
               break;
            case R.id.loginBtn:
                logInUser();
                break;
        }


    }

    private void logInUser() {

        if(isValidEmail(mainBinding.uEmailTVLogIn.getText().toString())
                && mainBinding.uPasswordTVLogIn.getText().toString().length() >= 6) {
            mainBinding.loginBtn.setClickable(false);
            auth.signInWithEmailAndPassword(mainBinding.uEmailTVLogIn.getText().toString(),
                    mainBinding.uPasswordTVLogIn.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this,"LogiIn with USer : " + task.getResult().getUser().getDisplayName(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, com.example.poster_1.HomeActivity.class));
                            mainBinding.loginBtn.setClickable(true);
                            finish();
                        }else {
                            Toast.makeText(this,"Invalid Email or Password",Toast.LENGTH_SHORT).show();

                        }
                    });
            mainBinding.loginBtn.setClickable(true);


        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void signUpUser() {


        if(isAllSignUpFeildsOK()) {

            mainBinding.signUpBtn.setClickable(false);

            auth.createUserWithEmailAndPassword(uEmail, uPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updateProfile(new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mainBinding.uNameTVSUp.getText().toString())
                                    .build()).addOnSuccessListener(aVoid -> startActivity(
                                            new Intent(com.example.poster_1.MainActivity.this, com.example.poster_1.HomeActivity.class)));
                            mainBinding.signUpBtn.setClickable(true);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String,Object> userData = new HashMap<>();
                            userData.put(KEY_USER_DISPLAY_NAME,uName);
                            userData.put(KEY_USER_DOB,uDOB);
                                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(userData);
                            Toast.makeText(this,"SIGNED UP with USer : " + task.getResult().getUser().getDisplayName(),Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            Log.e("Loggg_main", "this Error" + task.getException());
                            Toast.makeText(this,"Invalid Fields Dedicated",Toast.LENGTH_SHORT).show();

                        }
                    });
        }else {
            Toast.makeText(this,"Invalid Field Dedicated",Toast.LENGTH_SHORT).show();
        }
        mainBinding.signUpBtn.setClickable(true);

    }

    private boolean isAllSignUpFeildsOK(){
        boolean nameOK = false , dobOK = false , emailOK = false , passwordOK = false;

        if(mainBinding.uNameTVSUp.getText().toString().length() != 0 ){
            uName = mainBinding.uNameTVSUp.getText().toString();
            nameOK = true;
        }else {
            mainBinding.uNameTVSUp.setError("UnValid Name");
        }

        if(mainBinding.uDOBTVSUp.getText().toString().length() != 0 ){
            uDOB = mainBinding.uDOBTVSUp.getText().toString();
            dobOK = true;
        }else {
            mainBinding.uDOBTVSUp.setError("UnValid Date of Birth");
        }
        if(isValidEmail(mainBinding.uEmailTVSUp.getText().toString())  ){
            uEmail = mainBinding.uEmailTVSUp.getText().toString();
            emailOK  = true;

        }else {
            mainBinding.uEmailTVSUp.setError("UnValid Email");
        }

        if(mainBinding.uPasswrodTVSUp.getText().toString().length() >= 6 ){
            uPassword = mainBinding.uPasswrodTVSUp.getText().toString();
            passwordOK = true;
        }else {
            mainBinding.uPasswrodTVSUp.setError("Need 6 Character Password");
        }

        return (passwordOK && emailOK && nameOK && dobOK);

    };

    private void switchBwLoginSignUp() {

        if(isSignUp){
            isSignUp = false;
            mainBinding.togleBtnSigUpLin.setText("LOGIN");
            mainBinding.signUpLayout.setVisibility(View.VISIBLE);
            mainBinding.logInLayout.setVisibility(View.GONE);
        }
        else {
            isSignUp = true;
            mainBinding.togleBtnSigUpLin.setText("SignUp");
            mainBinding.signUpLayout.setVisibility(View.GONE);
            mainBinding.logInLayout.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = ""+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        Log.e("DAte_AAAAA","this is DATE :" + date);
        dateTextView.setText(date);
        mainBinding.uDOBTVSUp.setClickable(true);

    }
}