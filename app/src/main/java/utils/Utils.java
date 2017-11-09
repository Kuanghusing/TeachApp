package utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tian on 2017/10/31.
 */

public class Utils {
    /** 首先默认个文件保存路径 */
    private static final String SAVE_PIC_PATH= Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH+"/img";//保存的确切位置

    //保存图片
    public static void saveFile(Bitmap bm, String path) throws IOException {
        String subForder = SAVE_REAL_PATH;
        Log.v("lqnda",subForder);
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder+"/01.jpg");
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
     }

 //修改tablayout下划线的宽度
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {

        Class tabLayout = tabs.getClass();

        Field tabStrip =null;

        try{

            tabStrip = tabLayout.getDeclaredField("mTabStrip");

        }catch(NoSuchFieldException e) {

            e.printStackTrace();

        }

        tabStrip.setAccessible(true);

        LinearLayout llTab =null;

        try{

            llTab = (LinearLayout) tabStrip.get(tabs);

        }catch(IllegalAccessException e) {

            e.printStackTrace();

        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,leftDip,Resources.getSystem().getDisplayMetrics());

        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,rightDip,Resources.getSystem().getDisplayMetrics());

        for(int i =0;i < llTab.getChildCount();i++) {

            View child = llTab.getChildAt(i);

            child.setPadding(0,0,0,0);

            LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1);

            params.leftMargin= left;

            params.rightMargin= right;

            child.setLayoutParams(params);

            child.invalidate();

        }

    }

     /*返回当前的格式：yyyyMMddHHmmss*/
     public static String getCurrentTime(){
         SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
         return sdf.format(System.currentTimeMillis());
     }

    //毫秒转秒
    public static String long2String(long time) {
        //毫秒转秒
        int sec = (int) time / 1000;
        int min = sec / 60;    //分钟
        sec = sec % 60;        //秒
        if (min < 10) {    //分钟补0
            if (sec < 10) {    //秒补0
                return "0" + min + ":0" + sec;
            } else {
                return "0" + min + ":" + sec;
            }
        } else {
            if (sec < 10) {    //秒补0
                return min + ":0" + sec;
            } else {
                return min + ":" + sec;
            }
        }
    }

    /**
     * 文本中的emojb字符处理为表情图片
     * SpannableString字符串类型
     *setSpan(Object what, int start, int end, int flags)方法需要用户输入四个参数，
     * what表示设置的格式是什么，可以是前景色、背景色也可以是可点击的文本等等，start表示需要设置格式的子字符串的起始下标，同理end表示终了下标，
     * flags属性就有意思了，共有四种属性：
     Spanned.SPAN_INCLUSIVE_EXCLUSIVE 从起始下标到终了下标，包括起始下标
     Spanned.SPAN_INCLUSIVE_INCLUSIVE 从起始下标到终了下标，同时包括起始下标和终了下标
     Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 从起始下标到终了下标，但都不包括起始下标和终了下标
     Spanned.SPAN_EXCLUSIVE_INCLUSIVE 从起始下标到终了下标，包括终了下标
     * @param context
     * @param tv
     * @param source
     * @return
     */
    public static SpannableString getEmotionContent(final Context context, final TextView tv, String source) {
        SpannableString spannableString = new SpannableString(source);
        Resources res = context.getResources();
        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";

        /*正则表达式*/
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            Integer imgRes = EmotionUtils.EMOTION_STATIC_MAP.get(key);
            if (imgRes != null) {
                // 压缩表情图片
                int size = (int) tv.getTextSize() * 13 / 8;
                Bitmap bitmap = BitmapFactory.decodeResource(res, imgRes);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                 /*在Android中，TextView只用于显示图文混排效果，而EditText不仅可显示，也可混合输入文字和图像。这其中必不可少的一个类便是ImageSpan。*/
                ImageSpan span = new ImageSpan(context, scaleBitmap);
                spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }
    /**
     * dp转dip
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5F);
    }


    /**
     * 毫秒转化时分秒毫秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "d");
        }
        if (hour > 0) {
            sb.append(hour + "h");
        }
        if (minute > 0) {
            sb.append(minute + "′");
        }
        if (second > 0) {
            sb.append(second + "″");
        }
        return sb.toString();
    }
}
