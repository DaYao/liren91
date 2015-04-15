package com.liren91.liren91;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by smallQ on 15-3-24.
 */
public class Liren91Application extends Application
{
    //判断网络连接
    public boolean hasConnected(Activity activity)
    {
        ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            Log.i("ddd", "toast");

            activity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    Toast toast = new Toast(getApplicationContext());
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setImageResource(R.drawable.net_cannot_use);
                    toast.setView(imageView);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.show();

                }
            });
            return false;
        }
    }

    //获取登录状态
    public synchronized boolean isLogin()
    {
        SharedPreferences setting = getSharedPreferences("LirenCookie", 0);
        boolean isLoging = setting.getBoolean("isLogin", false);
        return isLoging;
    }
}
