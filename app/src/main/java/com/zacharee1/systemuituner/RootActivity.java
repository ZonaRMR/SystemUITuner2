package com.zacharee1.systemuituner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class RootActivity extends AppCompatActivity {
    private SetThings setThings;

    public  AppCompatActivity activity;
    private BroadcastReceiver finish_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThings = new SetThings(this);
        activity = this;

        setContentView(R.layout.activity_root);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView title = (TextView) findViewById(R.id.title_root);
        title.setTextColor(setThings.titleText); //set proper title text color

        Button getPerms = (Button) findViewById(R.id.get_perms);
        setThings.buttons(getPerms, "setupDoneRoot"); //button listener

        finish_activity = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_activity")) {
                    finish();
                }
            }
        };
        registerReceiver(finish_activity, new IntentFilter("finish_activity"));
    }

    @Override
    protected void onStop()
    {
        try
        {
            unregisterReceiver(finish_activity);
        }

        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        super.onStop();
    }
}
