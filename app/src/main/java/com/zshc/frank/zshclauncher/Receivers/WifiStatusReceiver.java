package com.zshc.frank.zshclauncher.Receivers;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.zshc.frank.zshclauncher.R;

/**
 * Created by Frank on 2016/5/5.
 */
public class WifiStatusReceiver extends BroadcastReceiver {

    Context context;
    private ImageView ivWifiStatus;

    public WifiStatusReceiver(Context context, ImageView imageView) {

        this.ivWifiStatus = imageView;
        this.context = context;
        int strength = getStrength(context);
        setWifiStatus(strength);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
            int strength = getStrength(context);
            setWifiStatus(strength);
        } else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                setWifiStatus(0);
            }
        } else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            //WIFI开关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifiState == WifiManager.WIFI_STATE_DISABLED) {//如果关闭
                setWifiStatus(0);
            }
        }


        setWifiStatus(getStrength(context));
//        Toast.makeText(context,"changed",Toast.LENGTH_LONG).show();
    }

    public int getStrength(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        if (info.getBSSID() != null) {
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            // 链接速度
//			int speed = info.getLinkSpeed();
//			// 链接速度单位
//			String units = WifiInfo.LINK_SPEED_UNITS;
//			// Wifi源名称
//			String ssid = info.getSSID();
            return strength;

        }
        return 0;
    }

    private void setWifiStatus(int status) {

//        Toast.makeText(context,""+status,Toast.LENGTH_SHORT).show();
        switch (status) {
            case 0:
                ivWifiStatus.setImageResource(R.drawable.stat_wifi_no_connect);
                break;
            case 1:
                ivWifiStatus.setImageResource(R.drawable.stat_wifi_bad);
                break;
            case 2:
                ivWifiStatus.setImageResource(R.drawable.stat_wifi_normal);
                break;
            case 3:
                ivWifiStatus.setImageResource(R.drawable.stat_wifi_good);
                break;
            case 4:
                ivWifiStatus.setImageResource(R.drawable.stat_wifi_nice);
                break;
            default:
                break;
        }
    }
}
