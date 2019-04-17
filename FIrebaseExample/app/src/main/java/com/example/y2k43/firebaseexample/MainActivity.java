package com.example.y2k43.firebaseexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.y2k43.firebaseexample.fragment.PeopleFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction().replace(R.id.main_framelayout, new PeopleFragment()).commit();


    }
}
