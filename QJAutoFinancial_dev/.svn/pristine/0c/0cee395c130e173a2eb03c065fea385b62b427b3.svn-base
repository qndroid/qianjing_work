package com.qianjing.finance.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.os.Environment;


public class WriteLog {
	
	private static final String SDCARD_ROOT = Environment
			.getExternalStorageDirectory().toString();

	public WriteLog(Context context) {
	}

	public static void recordLog(String LogDataStr) {

		boolean saveTypeStr = true;
		try {
			boolean saveType = saveTypeStr;

			File saveFile = new File(savaInfoToSD());
			if (!saveType && saveFile.exists()) {
				saveFile.delete();
				saveFile.createNewFile();
				// 保存结果到文
				OutputStreamWriter write = new OutputStreamWriter(
						new FileOutputStream(saveFile), "gbk");
				BufferedWriter writer = new BufferedWriter(write);
				writer.write(LogDataStr);
				writer.close();
			} else if (saveType && saveFile.exists()) {
				OutputStreamWriter write = new OutputStreamWriter(
						new FileOutputStream(saveFile), "gbk");
				BufferedWriter writer = new BufferedWriter(write);
				writer.write(LogDataStr);
				writer.close();
			} else if (saveType && !saveFile.exists()) {
				saveFile.createNewFile();
				OutputStreamWriter write = new OutputStreamWriter(
						new FileOutputStream(saveFile), "gbk");
				BufferedWriter writer = new BufferedWriter(write);
				writer.write(LogDataStr);
				writer.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String savaInfoToSD() {
		String fileName = null;

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File dir = new File(SDCARD_ROOT + File.separator + "QjFinaLog"
					+ File.separator);
			if (!dir.exists()) {
				dir.mkdir();
			}

			fileName = dir.toString() + File.separator + "CurrWrite"
					+ paserTime(System.currentTimeMillis()) + ".log";
		}
		return fileName;

	}

	private static String paserTime(long milliseconds) {
		System.setProperty("user.timezone", "qj/private Finacial");
		TimeZone tz = TimeZone.getTimeZone("qj/private Finacial");
		TimeZone.setDefault(tz);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String times = format.format(new Date(milliseconds));

		return times;
	}
}