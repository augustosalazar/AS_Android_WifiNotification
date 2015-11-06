package com.augustosalazar.as_android_wifinotification;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UploadService extends Service {

    private static final String TAG = "WifiNotification";
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "UploadService onCreate");
        mContext = this;
        new doUpload().execute();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class doUpload extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            startUpload();
            return null;
        }

    }

    public void startUpload() {
        Log.d(TAG, "Upload ");
        final String serverUrl = "http://www.tripquality.co/";

        uploadFile(serverUrl + "UploadToserver.php", Environment
                .getExternalStorageDirectory().getPath()+"/foto.jpg", true);
    }

    public int uploadFile(String upLoadServerUri, final String sourceFileUri,
                          final boolean debugMode) {

        String fileName = sourceFileUri;
        int serverResponseCode = 0;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);
        Log.d(TAG, "Upload : " + sourceFileUri + " to "+upLoadServerUri);


        if (!sourceFile.isFile()) {

            Log.d(TAG, "Source File not exist :" + sourceFileUri);

            return 0;

        } else {


            Log.d(TAG, "Upload file found");
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                Log.d(TAG, "Upload buffer bytesAvailable " + bytesAvailable + " maxBufferSize " + maxBufferSize);
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                Log.d(TAG, "Upload buffer bytesRead " + bytesRead);

                while (bytesRead > 0) {
                    Log.d(TAG, "Upload buffer sending " + bytesRead);
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.d(TAG, "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    if (debugMode == false) {
                        File file = new File(sourceFileUri);
                        file.delete();
                    }

                }

                // close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();

                Log.e(TAG, "error: " + ex.getMessage(), ex);

                Toast.makeText(mContext,
                        "File Upload error .", Toast.LENGTH_SHORT)
                        .show();

            } catch (Exception e) {

                Log.e(TAG, "error: " + e.getMessage(), e);
                e.printStackTrace();

            }
            return serverResponseCode;

        } // End else block
    }
}
