package com.alifreidouni.crashreportlib;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import static com.alifreidouni.crashreportlib.G.ANDROID_VERSION;
import static com.alifreidouni.crashreportlib.G.APP_VERSION;
import static com.alifreidouni.crashreportlib.G.FILES_PATH;
import static com.alifreidouni.crashreportlib.G.PHONE_MODEL;


public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "UNHANDLED_EXCEPTION";
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;

    public DefaultExceptionHandler(Thread.UncaughtExceptionHandler var1) {
        this.defaultExceptionHandler = var1;
    }

    public void uncaughtException(Thread var1, Throwable var2) {
        StringWriter var3 = new StringWriter();
        PrintWriter var4 = new PrintWriter(var3);
        var2.printStackTrace(var4);

        try {
            Random var5 = new Random();
            int var6 = var5.nextInt(99999);
            String var7 = APP_VERSION + "-" + Integer.toString(var6);
            Log.d("UNHANDLED_EXCEPTION", "Writing unhandled exception to: " + FILES_PATH + "/" + var7 + ".stacktrace");
            BufferedWriter var8 = new BufferedWriter(new FileWriter(FILES_PATH + "/" + var7 + ".stacktrace"));
            var8.write(ANDROID_VERSION + "\n");
            var8.write(PHONE_MODEL + "\n");
            var8.write(var3.toString());
            var8.flush();
            var8.close();
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        Log.d("UNHANDLED_EXCEPTION", var3.toString());
        this.defaultExceptionHandler.uncaughtException(var1, var2);
    }
}
