package com.unwind.networkmonitor;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.ads.*;
import com.unwind.netTools.Pinger;

import java.net.InetAddress;
import java.util.List;


public class Scan extends ActionBarActivity {

    private AdView adView;

    private static final String AD_UNIT_ID = "ca-app-pub-5497930890633928/3668252094";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainView);
        mainLayout.addView(adView, 0);

        AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();

        adView.loadAd(request);


        Button button = (Button) findViewById(R.id.testBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                List<InetAddress> addresses = Pinger.getDevicesOnNetwork("192.254.0");
                String build = new String();
                for(InetAddress i: addresses){
                    build+=i.getHostName()+"\n";
                }

                Toast.makeText(getApplicationContext(), build, Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }
    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
