package com.zshc.frank.zshclauncher.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zshc.frank.zshclauncher.R;
import com.zshc.frank.zshclauncher.Receivers.BatteryReceiver;
import com.zshc.frank.zshclauncher.Receivers.WifiStatusReceiver;
import com.zshc.frank.zshclauncher.utils.GetDateUtil;

/**
 * 2016-5-24
 * popupwindow相关代码注释掉，改用一个TextView作为退出按钮，回到源生桌面
 * popupwindow里本来要做的内容是APP自动更新，暂时先取消
 */

public class MainActivity extends Activity implements View.OnClickListener {

    //    private TextClock mTextClock;
    private TextView mDate;//日期显示控件
    private ImageView ivBatteryStatus;
    private TextView tvBatteryLevel;
    private ImageView ivWifiStatus;
    private TextView enterGeten;
    private TextView enterUclass;
    private TextView tvVersion;
    private TextView setting;
    private TextView signOut;
    private TextView mCamera;

    private TextView mGetenUpdate;
    private TextView mLauncherUpdate;

    private BatteryReceiver mBatteryReceiver;
    private WifiStatusReceiver mWifiStatusReceiver;

    private PopupWindow pop;
    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏显示，不显示状态栏
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //状态栏透明
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); //导航栏透明
        }
        setContentView(R.layout.activity_main);

        initView();

        regReceiver();
    }

    //初始化控件
    private void initView() {
//        mTextClock = (TextClock) findViewById(R.id.tc_clock);
        mDate = (TextView) findViewById(R.id.tv_date);
        mDate.setText(GetDateUtil.getDate());

        ivBatteryStatus = (ImageView) findViewById(R.id.iv_battery_status);
        tvBatteryLevel = (TextView) findViewById(R.id.tv_battery_level);
        ivWifiStatus = (ImageView) findViewById(R.id.iv_wifi_status);
        ivWifiStatus.setImageResource(R.drawable.stat_wifi_no_connect);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        setting = (TextView) findViewById(R.id.tv_setting);
        signOut = (TextView) findViewById(R.id.tv_signout);
        mCamera = (TextView) findViewById(R.id.ic_camera);

        enterGeten = (TextView) findViewById(R.id.ic_getenclass);
        enterUclass = (TextView) findViewById(R.id.ic_uclass);
        enterGeten.setOnClickListener(this);
        enterUclass.setOnClickListener(this);
        mCamera.setOnClickListener(this);

        setting.setOnClickListener(this);
        signOut.setOnClickListener(this);

        //获取本APP的版本名称，显示到主界面
        try {
            String versionName = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //初始化popupwindow
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        final View view = inflater.inflate(R.layout.popup_menu, null);
        // 创建PopupWindow对象
        pop = new PopupWindow(view,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                false);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);

        mGetenUpdate = (TextView) view.findViewById(R.id.pop_geten);
        mLauncherUpdate = (TextView) view.findViewById(R.id.pop_launcher);
        mGetenUpdate.setOnClickListener(this);
        mLauncherUpdate.setOnClickListener(this);

        //测量未绘制的popupwindow的宽高，计算显示位置的偏移量
        width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        height = view.getMeasuredHeight();
        width = view.getMeasuredWidth();
    }

    private void regReceiver() {

        //注册BatteryReceiver
        mBatteryReceiver = new BatteryReceiver(this, ivBatteryStatus, tvBatteryLevel);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryReceiver, filter);

        //注册WifiStatusReceiver
        mWifiStatusReceiver = new WifiStatusReceiver(this, ivWifiStatus);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter2.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter2.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(mWifiStatusReceiver, filter2);
    }


    //销毁时回收资源
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBatteryReceiver != null) {
            unregisterReceiver(mBatteryReceiver);
        }
        if (mWifiStatusReceiver != null)
            unregisterReceiver(mWifiStatusReceiver);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ic_getenclass:
                try {
                    PackageManager packageManager = getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager.getLaunchIntentForPackage("com.cvte.eschbag.stu");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "请先安装集腾互动课堂学生端", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ic_uclass:
                try {
                    PackageManager packageManager = getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager.getLaunchIntentForPackage("com.mainbo.uclass");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "请先安装优课学生端", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_setting:
                if (pop.isShowing()) pop.dismiss();
                else pop.showAtLocation(v, Gravity.NO_GRAVITY, v.getLeft()-5, v.getTop() - height);
                break;
            case R.id.pop_geten:
                Toast.makeText(MainActivity.this,"youfanying",Toast.LENGTH_SHORT).show();
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.HOME");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "没有源生桌面", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_signout:
                Toast.makeText(MainActivity.this,"打开源生桌面",Toast.LENGTH_SHORT).show();
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.MAIN");
                    intent.addCategory("android.intent.category.HOME");
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "没有源生桌面", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ic_camera:
                Intent intent = new Intent();
                intent.setAction("android.media.action.IMAGE_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //重写返回键和HOME键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_NAVIGATE_PREVIOUS) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
