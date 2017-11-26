package com.android.gdgvit.aiesec.activity.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.gdgvit.aiesec.R;
import com.android.gdgvit.aiesec.activity.Aiesecer.AiesecMainActivity;
import com.android.gdgvit.aiesec.activity.EP.ActivityEpMain;
import com.android.gdgvit.aiesec.model.LoginResponse;
import com.android.gdgvit.aiesec.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shuvam Ghosh on 1/25/2017.
 */

public class ActivityLogin extends AppCompatActivity{

    private TextSwitcher textSwitcher;
    private ImageButton nextImageButton;
    private ImageButton previousImageButton;
    private String textNonAisec = "EP";
    private String textAisec = "AIESECER";
    private Button btnSignIn;
    private EditText etUserEmail;
    private EditText etUserPassword;
    private TextView tvCreateAccount;
    private String emailEntered;
    private String passwordEntered, memberEntered;
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private String BaseUrl = "http://139.59.62.236:8000/ep/";
    private Integer flagmember=0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textSwitcher = (TextSwitcher)findViewById(R.id.textSwitcher);
        textSwitcher.setFactory(mFactory);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        previousImageButton = (ImageButton)findViewById(R.id.previousImageButton);
        nextImageButton = (ImageButton)findViewById(R.id.nextImageButton);
        textSwitcher.setText(textAisec);
        etUserEmail = (EditText)findViewById(R.id.etUserEmail);
        etUserPassword = (EditText)findViewById(R.id.etUserPassword);
        tvCreateAccount = (TextView)findViewById(R.id.tvCreateAccount);


        sp = PreferenceManager.getDefaultSharedPreferences(this);
        Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        textSwitcher.setInAnimation(in);
        textSwitcher.setOutAnimation(out);

        btnSignIn.setText("Sign In");
        previousImageButton.setBackground(getDrawable(R.drawable.ic_chevron_left_blackdark_24dp));
        nextImageButton.setBackground(getDrawable(R.drawable.ic_chevron_right_black_24dp));


        if(isLoggedIn()) //replaced due to API problem
        {
            Intent i = new Intent(ActivityLogin.this,ActivityEpMain.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLogin.this,SignUpActivity.class);
                startActivity(i);
            }
        });



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEntered = etUserEmail.getText().toString();
                passwordEntered = etUserPassword.getText().toString();
                if(flagmember==0)
                    memberEntered="aiesec";
                else
                    memberEntered="ep";

                if (validate() == true) {

                btnSignIn.setText("Logging in ...");

                   // ApiInterface apiService = ApiClient.getClient(ActivityLogin.this,BaseUrl).create(ApiInterface.class);
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://139.59.62.236:8000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    ApiInterface apiService = retrofit.create(ApiInterface.class);



                Call<LoginResponse> login = apiService.updateUser(emailEntered, passwordEntered, memberEntered);

                login.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {



                        if (response.body().getStatus().equals("successful")) {


                            Toast.makeText(ActivityLogin.this, ""+response.body().getStatus(), Toast.LENGTH_SHORT).show();
                            ed = sp.edit();
                            ed.putString("name",response.body().getUser().getName());
                            ed.putString("contact",response.body().getUser().getContact());
                            ed.putString("email",response.body().getUser().getEmail());
                            ed.putString("cpf1",response.body().getUser().getCountryPreferences()[0]);
                            ed.putString("cpf2",response.body().getUser().getCountryPreferences()[1]);
                            ed.putString("cpf3",response.body().getUser().getCountryPreferences()[2]);
                            ed.putString("raisedBy",response.body().getUser().getRaisedBy());

                            ed.putInt("LoggedIn",1);
                            ed.commit();
                            Intent i = new Intent(ActivityLogin.this, ActivityEpMain.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Toast.makeText(ActivityLogin.this, "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(ActivityLogin.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }

                        // Log.d("Response status",""+response.body().getStatus());
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {

                    }
                });
            }

                else
                {
                    Toast.makeText(ActivityLogin.this, "Enter the fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation in = AnimationUtils.loadAnimation(getApplicationContext(),
                        android.R.anim.fade_in);
                Animation out = AnimationUtils.loadAnimation(getApplicationContext(),
                        android.R.anim.fade_out);
                textSwitcher.setInAnimation(in);
                textSwitcher.setOutAnimation(out);
                textSwitcher.setText(textNonAisec);
                flagmember=1;

                previousImageButton.setBackground(getDrawable(R.drawable.ic_chevron_left_black_24dp));
                nextImageButton.setBackground(getDrawable(R.drawable.ic_chevron_right_blackdark_24dp));

            }
        });

        previousImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation in = AnimationUtils.loadAnimation(getApplicationContext(),
                        android.R.anim.fade_in);
                Animation out = AnimationUtils.loadAnimation(getApplicationContext(),
                        android.R.anim.fade_out);
                textSwitcher.setInAnimation(in);
                textSwitcher.setOutAnimation(out);
                textSwitcher.setText(textAisec);
                flagmember=0;

                previousImageButton.setBackground(getDrawable(R.drawable.ic_chevron_left_blackdark_24dp));
                nextImageButton.setBackground(getDrawable(R.drawable.ic_chevron_right_black_24dp));
            }
        });


    }

    private boolean isLoggedIn() {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);

        if(sp.getInt("LoggedIn",0)==1)
        {
            return true;
        }
        else
            return false;
    }

    private boolean validate() {

        if(!emailEntered.isEmpty()&&!passwordEntered.isEmpty())
        {
            return true;
        }
        else
            return false;
    }


    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {

        @Override
        public View makeView() {

            // Create a new TextView
            TextView t = new TextView(ActivityLogin.this);
            t.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER);
            t.setTextSize(16);
            //t.setTextAppearance(ActivityLogin.this, android.R.style.TextAppearance_Large);
            t.setTextColor(getResources().getColor(R.color.white));
            return t;
        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        btnSignIn.setText("Sign In");
    }
}
