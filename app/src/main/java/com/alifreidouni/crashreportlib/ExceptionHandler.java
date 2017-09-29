package com.alifreidouni.crashreportlib;

/**
 * Created by alifreidouni on 7/7/2017 AD.
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import static com.alifreidouni.crashreportlib.G.ANDROID_VERSION;
import static com.alifreidouni.crashreportlib.G.APP_PACKAGE;
import static com.alifreidouni.crashreportlib.G.APP_VERSION;
import static com.alifreidouni.crashreportlib.G.FILES_PATH;
import static com.alifreidouni.crashreportlib.G.Mobile;
import static com.alifreidouni.crashreportlib.G.PHONE_MODEL;
import static com.alifreidouni.crashreportlib.G.Title;
import static com.alifreidouni.crashreportlib.G.URL;


public class ExceptionHandler {
    private static String TAG = "com.nullwire.trace.ExceptionsHandler";
    private static String[] stackTraceFileList = null;
    private static Context cx;

    public ExceptionHandler() {
    }

    public static boolean register(Context var0) {
        PackageManager var1 = var0.getPackageManager();

        try {
            PackageInfo var2 = var1.getPackageInfo(var0.getPackageName(), 0);
            APP_VERSION = var2.versionCode + "";
            APP_PACKAGE = var2.packageName;
            FILES_PATH = var0.getFilesDir().getAbsolutePath();
            PHONE_MODEL = Build.MODEL;
            ANDROID_VERSION = Build.VERSION.RELEASE;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        boolean var4 = false;
        if (searchForStackTraces().length > 0) {
            var4 = true;
        }

        (new Thread() {
            public void run() {
                ExceptionHandler.submitStackTraces();
                UncaughtExceptionHandler var1 = Thread.getDefaultUncaughtExceptionHandler();
                if (var1 != null) {
                }

                if (!(var1 instanceof DefaultExceptionHandler)) {
                    Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(var1));
                }

            }
        }).start();
        return var4;
    }

    public static void register(Context var0, String var1, String var2, String var3) {
        URL = var1;
        Title = var2;
        Mobile = var3;
        cx = var0;
        register(var0);
    }

    @Nullable
    private static String[] searchForStackTraces() {
        if (stackTraceFileList != null) {
            return stackTraceFileList;
        } else {
            File var0 = new File(FILES_PATH + "/");
            var0.mkdir();
            FilenameFilter var1 = new FilenameFilter() {
                public boolean accept(File var1, String var2) {
                    return var2.endsWith(".stacktrace");
                }
            };
            return stackTraceFileList = var0.list(var1);
        }
    }

    private static void submitStackTraces() {
        boolean var22 = false;

        String[] var0;
        int var1;
        File var28;
        label166:
        {
            try {
                var22 = true;
                var0 = searchForStackTraces();
                if (var0 != null) {
                    if (var0.length <= 0) {
                        var22 = false;
                        break label166;
                    }

                    for (var1 = 0; var1 < var0.length; ++var1) {
                        String var2 = FILES_PATH + "/" + var0[var1];
                        final String var3 = var0[var1].split("-")[0];
                        StringBuilder var4 = new StringBuilder();
                        BufferedReader var5 = new BufferedReader(new FileReader(var2));
                        String var6 = null;
                        String var7 = null;
                        String var8 = null;

                        while ((var6 = var5.readLine()) != null) {
                            if (var7 == null) {
                                var7 = var6;
                            } else if (var8 == null) {
                                var8 = var6;
                            } else {
                                var4.append(var6);
                                var4.append(System.getProperty("line.separator"));
                            }
                        }

                        var5.close();
                        final String var9 = var4.toString();
                        final String finalVar = var8;
                        final String finalVar1 = var7;

                        StringRequest postRequest = new StringRequest(Request.Method.POST, URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        // response
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // error
                                    }
                                }
                        ) {

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("Content-Type", "application/x-www-form-urlencoded");
                                return params;
                            }

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("package_name", APP_PACKAGE);
                                params.put("package_version", APP_VERSION);
                                params.put("phone_model", finalVar);
                                params.put("android_version", finalVar1);
                                params.put("stacktrace", var9);
                                params.put("name", Title);
                                params.put("mobile", Mobile);

                                return params;
                            }
                        };

                        Volley.newRequestQueue(cx).add(postRequest);

                    }

                    var22 = false;
                    break label166;
                }

                var22 = false;
                break label166;
            } catch (Exception var26) {
                var26.printStackTrace();
                var22 = false;
            } finally {
                if (var22) {
                    try {
                        String[] var14 = searchForStackTraces();

                        for (String aVar14 : var14) {
                            File var16 = new File(FILES_PATH + "/" + aVar14);
                            var16.delete();
                        }
                    } catch (Exception var23) {
                        var23.printStackTrace();
                    }

                }
            }

            try {
                var0 = searchForStackTraces();

                for (var1 = 0; var1 < var0.length; ++var1) {
                    var28 = new File(FILES_PATH + "/" + var0[var1]);
                    var28.delete();
                }

                return;
            } catch (Exception var24) {
                var24.printStackTrace();
                return;
            }
        }

        try {
            var0 = searchForStackTraces();

            for (var1 = 0; var1 < var0.length; ++var1) {
                var28 = new File(FILES_PATH + "/" + var0[var1]);
                var28.delete();
            }
        } catch (Exception var25) {
            var25.printStackTrace();
        }

    }


}
