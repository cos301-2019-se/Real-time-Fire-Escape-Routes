package com.example.wifisignalapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private WifiManager manager;
    private TextView view;
    private Button btn_search;
    private BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.list_wifi);
        manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!manager.isWifiEnabled()){
            Toast.makeText(this, "Wifi off", Toast.LENGTH_LONG).show();
            manager.setWifiEnabled(true);
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean resp = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);

                if (resp){
                       for (ScanResult result: manager.getScanResults()){
                           view.append(result.SSID);
                       }
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(receiver, filter);
    }
}
