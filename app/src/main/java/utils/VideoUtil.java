package utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.view.WindowManager;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by Tian on 2017/11/11.
 * 视频工具类
 */

public class VideoUtil {

    public static Activity scanForActivity(Context context){
        if (context==null){
            return null;
        }
        if (context instanceof Activity){
            return (Activity) context;
        }else if (context instanceof ContextWrapper){
            return (Activity) scanForActivity(context).getBaseContext();
        }
        return null;
    }

    public static AppCompatActivity getAppCompatActivity(Context context){
        if (context==null){
            return null;
        }
        if (context instanceof  AppCompatActivity){
            return (AppCompatActivity) context;
        }else if (context instanceof ContextThemeWrapper){
            return (AppCompatActivity) getAppCompatActivity(context).getBaseContext();
        }
        return null;
    }

    public static void showActionBar(Context context){
        ActionBar bar=getAppCompatActivity(context).getSupportActionBar();
        if (bar!=null){
            bar.setShowHideAnimationEnabled(false);
            bar.show();
        }
        scanForActivity(context)
                .getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public static void hideActionBar(Context context){
        ActionBar bar=getAppCompatActivity(context).getSupportActionBar();
        if (bar!=null){
            bar.setShowHideAnimationEnabled(false);
            bar.hide();
        }
        scanForActivity(context)
                .getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                ,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /*获取屏幕宽度*/
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }
    /*获取屏幕高度*/
    public static int getScreenHeigth(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /*将毫秒格式化 ##:##的时间*/
    public static String formatTime(long milliseconds){
        if (milliseconds<=0||milliseconds>=24*60*60*1000){
            return "00:00";
        }
        long totalSeconds=milliseconds/1000;
        long seconds=totalSeconds%60;
        long minutes=(totalSeconds/60)%60;
        long hours=totalSeconds/3600;

        StringBuilder builder=new StringBuilder();
        Formatter formatter=new Formatter(builder, Locale.getDefault());
        if (hours>0){
            return formatter.format("%d:%02d:%02d",hours,minutes,seconds).toString();
        }else{
            return formatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }

    /*保存播放位置，以便下次播放时接着上次的位置继续播放*/
    public static void savePlayPosition(Context context,String url,long position){
        context.getSharedPreferences("position",
                Context.MODE_PRIVATE)
                .edit()
                .putLong(url,position)
                .apply();
    }
    /*取出上次保存播放位置*/
    public static long getSavedPlayPosition(Context context, String url){
      return context.getSharedPreferences("position",
                Context.MODE_PRIVATE)
              .getLong(url,0);
    }


}
