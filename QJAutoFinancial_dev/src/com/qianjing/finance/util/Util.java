
package com.qianjing.finance.util;

import com.qianjing.finance.model.common.BrachBank;
import com.qianjing.finance.model.common.UserDevice;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Util
{
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle)
        {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try
        {
            output.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    /*
     * 获取渠道号
     */
    public static String getChannelCode(Context context)
    {
        String channelCode = getMetaData(context, "UMENG_CHANNEL");
        if (channelCode != null)
        {
            return channelCode;
        }
        return "C_000";
    }

    private static String getMetaData(Context context, String key)
    {
        try
        {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null)
            {
                return value.toString();
            }
        } catch (Exception e)
        {
            return "";
        }
        return "";
    }

    /*
     * 判断设备的版本号是否低于最低支持版本(4.0)
     */
    public static boolean checkSDKLevel(Context context)
    {
        UserDevice.initInfo(context);
        String strLevel = UserDevice.getSDKLevel();
        try
        {
            // 是否是5.0
            if (TextUtils.equals("L", strLevel))
                return true;
            // 是否低于4.0
            if (Float.valueOf(strLevel) >= 14)
                return true;
            else
                return false;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    /*
     * 获取当前应用的版本号
     */
    public static String getVersionName(Context mContext)
    {
        String versionName = "";
        try
        {
            versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 1).versionName;
        } catch (Exception e)
        {

        }

        return versionName;
    }

    /**
     * 隐藏系统软件盘
     */
    public static void hideSoftInputMethod(Context context, View v)
    {
        /* 隐藏软键盘 */
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isActive())
        {
            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 6-18位非纯数字判断
    public static boolean isMacth(String value)
    {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
        return value.matches(regex);

    }

    /**
     * 将字符串转成MD5值
     * 
     * @param string
     * @return
     */
    public static String stringToMD5(String string)
    {
        byte[] hash;
        try
        {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash)
        {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * 获取设备唯一标示
     */
    public static String getMac()
    {
        String macSerial = null;
        String str = "";
        try
        {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str;)
            {
                str = input.readLine();
                if (str != null)
                {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static void hideInputView(Activity context)
    {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && ((Activity) context).getCurrentFocus() != null)
        {
            if (context.getCurrentFocus().getWindowToken() != null)
            {
                imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private static ArrayList<Integer> getNowVersion(String version)
    {
        ArrayList<Integer> values = new ArrayList<Integer>();
        String[] versionStrings = version.split("\\.");
        for (int i = 0; i < versionStrings.length; i++)
        {
            values.add(Integer.parseInt(versionStrings[i]));
        }

        if (values.size() < 3)
        {
            for (int i = values.size(); i < 3; i++)
            {
                values.add(0);
            }
        }
        return values;
    }

    /**
     * 版本号比较 如果最新版本号大于当前版本号，则返回1
     */
    public static int checkVersion(String strLatestVersion, String strCurrentVersion)
    {
        ArrayList<Integer> arrayLatest = getNowVersion(strLatestVersion);
        ArrayList<Integer> arrayCurrent = getNowVersion(strCurrentVersion);
        if (arrayLatest.size() != arrayCurrent.size())
        {
            return 0;
        }
        for (int i = 0; i < arrayLatest.size(); i++)
        {
            // 只判断大于，其他略过
            int latest = arrayLatest.get(i);
            int current = arrayCurrent.get(i);

            if (latest == current)
                continue;
            if (latest < current)
                break;
            if (latest > current)
                return 1;
        }
        return 0;
    }

    public static ArrayList<BrachBank> searchBank(ArrayList<BrachBank> list, String strSub) {

        // 如果参数异常，直接返回原数据
        if (list == null || strSub == null || TextUtils.equals("", strSub))
            return list;

        ArrayList<BrachBank> resultList = new ArrayList<BrachBank>();
        int index = 0;
        while (index < list.size())
        {
            BrachBank bank = list.get(index);
            String strBankName = bank.getName();
            if (strBankName.contains(strSub)) {
                resultList.add(bank);
            }
            index++;
        }
        return resultList;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
