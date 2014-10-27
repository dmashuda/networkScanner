package com.unwind.networkmonitor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.ads.*;
import com.google.android.gms.drive.internal.ad;
import com.unwind.netTools.Pinger;
import com.unwind.netTools.model.Device;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


public class Scan extends ActionBarActivity {

    private AdView adView;
    private  NetDeviceAdapter adapter = new NetDeviceAdapter(new ArrayList<Device>(15), R.layout.device_fragment, this);

    private static final String AD_UNIT_ID = "ca-app-pub-5497930890633928/3668252094";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);



        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AD_UNIT_ID);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainView);
        mainLayout.addView(adView, 0);

        AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();


        adView.loadAd(request);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;

        int numrows = (int) Math.floor(dpWidth / 300);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.list);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(numrows, StaggeredGridLayoutManager.VERTICAL);
        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());




        rescan();
    }
    private void rescan(){

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected() && mWifi.isAvailable()){
            AsyncScan scan = new AsyncScan(ProgressDialog.show(this,"Scanning", "scanning your network"));
            scan.execute(adapter);

        }else {
            Toast.makeText(this, "Not connected to wifi, no scanning permitted", Toast.LENGTH_LONG).show();
        }

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

        switch (id){
            case R.id.action_refresh:
                rescan();
            default:
        }

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

    public static String intToIp(int i) {

        return ((i >> 24 ) & 0xFF ) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ( i & 0xFF) ;
    }

    private static class AsyncScan extends AsyncTask<NetDeviceAdapter, Void, List<Device>> {

        private NetDeviceAdapter adapter;
        private ProgressDialog mDialog;

        public AsyncScan(ProgressDialog dialog){
            super();
            this.mDialog = dialog;
        }
        @Override
        protected List<Device> doInBackground(NetDeviceAdapter... voids) {


            String ipString = getLocalIpv4Address();


            if (ipString == null){
                return new ArrayList<Device>(1);
            }
            int lastdot = ipString.lastIndexOf(".");
            ipString = ipString.substring(0, lastdot);

            List<Device> addresses = Pinger.getDevicesOnNetwork(ipString);
            adapter = voids[0];
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Device> inetAddresses) {
            super.onPostExecute(inetAddresses);
            adapter.setAddresses(inetAddresses);
            adapter.notifyDataSetChanged();
            mDialog.cancel();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }


        public static String getLocalIpv4Address(){
            try {
                String ipv4;
                List<NetworkInterface>  nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
                if(nilist.size() > 0){
                    for (NetworkInterface ni: nilist){
                        List<InetAddress>  ialist = Collections.list(ni.getInetAddresses());
                        if(ialist.size()>0){
                            for (InetAddress address: ialist){
                                if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4=address.getHostAddress())){
                                    return ipv4;
                                }
                            }
                        }

                    }
                }

            } catch (SocketException ex) {

            }
            return "";
        }


    }
}
