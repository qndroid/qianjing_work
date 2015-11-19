
package com.qianjing.finance.util;

import com.qianjing.finance.net.connection.AnsynHttpRequest;
import com.qianjing.finance.net.connection.HttpCallBack;
import com.qianjing.finance.ui.QApplication;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TimeZone;

/**
 * 自定义系统的Crash捕捉类，用Toast替换系统的对话框 将软件版本信息，设备信息，出错信息保存在sd卡中，你可以上传到服务器
 * 
 * @author xiaanming
 */
public class CustomCrashHandler implements UncaughtExceptionHandler {
    private Context mContext;
    private static final String SDCARD_ROOT = Environment
            .getExternalStorageDirectory().toString();
    private static CustomCrashHandler mInstance = new CustomCrashHandler();
    private QApplication mApplication;

    private CustomCrashHandler() {
    }

    /**
     * 单例模式，保证只有一个CustomCrashHandler实例存在
     * 
     * @return
     */
    public static CustomCrashHandler getInstance() {
        return mInstance;
    }

    /**
     * 异常发生时，系统回调的函数，我们在这里处理一些操
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // 将一些信息保存到SDcard
        savaInfoToSD(mContext, ex);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 为我们的应用程序设置自定义Crash处理
     */
    public void setCustomCrashHanler(Context context) {
        mContext = context;
        mApplication = (QApplication) mContext.getApplicationContext();

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap
     * 
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            map.put("versionName", mPackageInfo.versionName);
            map.put("versionCode", "_" + mPackageInfo.versionCode);
            map.put("Build_version", "_" + Build.VERSION.RELEASE);
            map.put("CPU ABI", "_" + Build.CPU_ABI);
            map.put("Vendor", "_" + Build.MANUFACTURER);
            map.put("MODEL", "_" + Build.MODEL);
            map.put("SDK_INT", "_" + Build.VERSION.SDK_INT);
            map.put("PRODUCT", "_" + Build.PRODUCT);
            map.put("Activity_Window", "_" + "LoginActivity");
            map.put("Log", "发送登录请求");
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取系统未捕捉的错误信息
     * 
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();
        return mStringWriter.toString();
    }

    /**
     * 保存获取软件信息，设备信息和出错信息保存在SDcard
     * 
     * @param context
     * @param ex
     * @return
     */
    private String savaInfoToSD(Context context, Throwable ex) {
        StringBuffer buffer = new StringBuffer();
        String fileName = null;
        for (Map.Entry<String, String> entry : obtainSimpleInfo(context)
                .entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            buffer.append(key).append(" = ").append(value).append("\n");
        }

        buffer.append(obtainExceptionInfo(ex));

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(SDCARD_ROOT + File.separator + "QjFinaLog"
                    + File.separator);
            if (!dir.exists()) {
                dir.mkdir();
            }

            try {
                fileName = dir.toString() + File.separator
                        + paserTime(System.currentTimeMillis()) + ".log";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(buffer.toString().getBytes());
                fos.flush();
                fos.close();

                sendErrorLog(buffer.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格
     * 
     * @param milliseconds
     * @return
     */
    private String paserTime(long milliseconds) {
        System.setProperty("user.timezone", "qj/private Finacial");
        TimeZone tz = TimeZone.getTimeZone("qj/private Finacial");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date(milliseconds));
    }

    private void sendErrorLog(String strLog) {
        Hashtable<String, Object> upload = new Hashtable<String, Object>();
        upload.put("log", strLog);
        String url = "tool/other/log.php";
        AnsynHttpRequest.requestByPost(mContext, url, new HttpCallBack() {
            @Override
            public void back(String data, int url) {
                if (data == null) {
                    return;
                }
            }
        }, upload);
    }

}
