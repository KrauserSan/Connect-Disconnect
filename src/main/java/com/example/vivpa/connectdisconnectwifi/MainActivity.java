package com.example.vivpa.connectdisconnectwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    TextView text;WifiConfiguration wfc;
    String ssid = "WifiForTheAges";
    String password = "Bits2ncsu";
    WifiManager wfMgr;
    Button button;
    int networkID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wfMgr = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        setConf();

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        this.registerReceiver(this.myWifiReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private BroadcastReceiver myWifiReceiver
            = new BroadcastReceiver(){

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            NetworkInfo networkInfo = (NetworkInfo) arg1.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                button.setText("Connected");
            }
            else
                button.setText("Disconnected");
        }};

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void setConf(){
        wfc = new WifiConfiguration();

        wfc.SSID = "\"".concat(ssid).concat("\"");
        wfc.status = WifiConfiguration.Status.DISABLED;
        wfc.priority = 40;
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wfc.preSharedKey = "\"".concat(password).concat("\"");
        networkID = wfMgr.addNetwork(wfc);
    }
    public void wifiConnect(){
        wfMgr.disconnect();
        wfMgr.enableNetwork(this.networkID, true);
        wfMgr.reconnect();
    }
    public void wifiDisconnect(){
        wfMgr.disconnect();
    }

    @Override
    public void onClick(View v) {
        if(button.getText()=="Connected"){
            wifiDisconnect();
        }
        else
            wifiConnect();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(myWifiReceiver);
        super.onPause();
    }
}

