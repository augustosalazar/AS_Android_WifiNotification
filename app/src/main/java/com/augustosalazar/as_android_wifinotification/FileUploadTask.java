package com.augustosalazar.as_android_wifinotification;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUploadTask {

    private Context mContext;
    private static final String TAG = "WifiNotification";

    public FileUploadTask(Context context) {
        mContext = context;
    }

}
