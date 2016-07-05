package com.zshc.frank.zshclauncher.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.zshc.frank.zshclauncher.R;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class BatteryReceiver extends BroadcastReceiver {

    private ImageView ivBattery;
    private TextView tvBattery;
    private Context context;

    public BatteryReceiver(Context context, ImageView imageView, TextView textView) {

        this.context = context;
        this.ivBattery = imageView;
        this.tvBattery = textView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);//当前电量
        int max = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);//最大电量
        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);//电池健康状态
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);//电池充放电状态

        int level = -1;
        if (rawlevel >= 0 && max > 0) {
            level = (rawlevel * 100) / max;
        }
        tvBattery.setText(level + "%");

//        tvBattery.setText("电量"+rawlevel + " max:" + max + " heath:" + health + " status:" + status);
        if (BatteryManager.BATTERY_HEALTH_GOOD == health) {//如果电池状态良好
            switch (status) {

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    break;
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    if (level<=20)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_charge_anim1);
                    if (level>20&&level<=40)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_charge_anim2);
                    if (level>40&&level<=60)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_charge_anim3);
                    if (level>60&&level<=80)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_charge_anim4);
                    if (level>80&&level<=100)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_charge_anim5);
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    if (level <= 10)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_10);
                    else if (level >= 10 && level <= 20)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_20);
                    else if (level >= 20 && level <= 40)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_40);
                    else if (level >= 40 && level <= 60)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_60);
                    else if (level >= 60 && level <= 90)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_90);
                    else if (level >= 90 && level <= 100)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_100);
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING :
                    if (level <= 10)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_10);
                    else if (level >= 10 && level <= 20)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_20);
                    else if (level >= 20 && level <= 40)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_40);
                    else if (level >= 40 && level <= 60)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_60);
                    else if (level >= 60 && level <= 90)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_90);
                    else if (level >= 90 && level <= 100)
                        ivBattery.setImageResource(R.drawable.stat_sys_battery_100);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    ivBattery.setImageResource(R.drawable.stat_sys_battery_100);
                    break;
                default:
                    break;
            }
        }
    }
}
