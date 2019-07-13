package com.example.connectit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.connectit.Fragment.HomeFragment;
import com.example.connectit.Fragment.NotificationFragment;
import com.example.connectit.Fragment.ProfileFragment;
import com.example.connectit.Fragment.SearchFragment;
import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,NotificationFragment.OnFragmentInteractionListener,ProfileFragment.OnFragmentInteractionListener,SearchFragment.OnFragmentInteractionListener{

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        bottomNavigationView=findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_home:
                        selectedFragment=new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectedFragment= new SearchFragment();
                        break;
                    case R.id.nav_add:
                        selectedFragment=null;
                        startActivity(new Intent(MainActivity.this,PostActivity.class));
                        break;
                    case R.id.nav_heart:
                        selectedFragment=new NotificationFragment();
                        break;
                    case R.id.nav_profile:
                        SharedPreferences.Editor editor=getSharedPreferences("PREFS",MODE_PRIVATE).edit();
                        editor.putString("profileid", FirebaseAuth.getInstance().getUid());
                        editor.apply();
                        selectedFragment=new ProfileFragment();
                        break;
                }
                if(selectedFragment!=null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
