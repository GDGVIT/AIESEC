package com.android.gdgvit.aiesec.activity.Aiesecer;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.android.gdgvit.aiesec.R;
import com.android.gdgvit.aiesec.activity.EP.ActivityEpMain;
import com.android.gdgvit.aiesec.activity.Main.StartActivity;
import com.android.gdgvit.aiesec.fragment.AIESECER.AiescerProfileFragment;
import com.android.gdgvit.aiesec.model.LogoutResponse;
import com.android.gdgvit.aiesec.rest.ApiInterface;

import android.support.v4.app.Fragment;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AiesecMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    Toolbar toolbar;
    SharedPreferences sps;
    SharedPreferences.Editor ed;
    String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6Im5pc2hhbnQubmlqYWd1bmE4QGFpZXNlYy5uZXQiLCJ0aW1lIjoiMjMtMDMtMjAxNyAwNTozOCBQTSJ9.D3_yki5HlFdwzOcB2IBqaT65SA5mg2GlXFQpZ_MncxE";
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aiesec_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       // fab.setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View view) {
             //   Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //    .setAction("Action", null).show();
            //}
       // });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.aiesec_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        System.out.println("Hi");
        if (id == R.id.aiescer_profile2) {
            //toolbar.setVisibility(View.GONE);
            AiescerProfileFragment df = new AiescerProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer_aiesecer,df).commit();

        }
        else if (id == R.id.nav_logout) {


            Dialog progressDialog = new Dialog(AiesecMainActivity.this);
            progressDialog.setContentView(R.layout.dialog_view);
            progressDialog.show();

            TextView tvDialog = (TextView)progressDialog.findViewById(R.id.tvDialogContent);
            tvDialog.setText("Logging out..");

            ed = sps.edit();
            ed.putInt("LoggedIn",0);
            ed.commit();



            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://139.59.62.236:8000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface apiService = retrofit.create(ApiInterface.class);


            Call<LogoutResponse> logoutResponseCall = apiService.logoutUser(token);
            logoutResponseCall.enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {

                    Log.d("Logout Response",response.body().getStatus());
                    Intent i = new Intent(getApplicationContext(), StartActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<LogoutResponse> call, Throwable t) {

                }
            });



        }


        /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
