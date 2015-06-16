package com.jason.helper;

import android.content.Context;
import android.util.Log;

import com.jason.Cfg;
import com.jason.Debug;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by shenghao on 2015/5/14.
 */
public class HttpClientHelper {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private boolean usePaging = false;

    private int Total;
    private int TotalPage;
    private int PerPage;
    private int Page;

    static {
        client.setTimeout(60000);
    }

    /**
     * @param mayInterruptIfRunning
     */
    public void cancelAllRequest(boolean mayInterruptIfRunning) {
        client.cancelAllRequests(mayInterruptIfRunning);
    }

    /**
     * @param context
     * @param mayInterruptIfRunning
     */
    public void cancelRequest(Context context, boolean mayInterruptIfRunning) {
        client.cancelRequests(context, mayInterruptIfRunning);
    }

    public static String parseResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            String error = json.get("Error").toString();
            if (!error.trim().equals("")) {
                Log.d(error, response);
                return error;
            }
            // session id
            String sid = json.get("SID").toString();

            // now we need to do something about the code

        } catch (Exception e) {
            // the server return unexpected error
            Log.d("SERVER RESULT ERROR", response);
            return "SERVER_ERROR";
        }

        return "SUCCESS";
    }

    public void get(String url, RequestParams params,
                    JSONHttpHelper.JSONHttpResponseHandler responseHandler) {
        String u = getAbsoluteUrl(url);
        Debug.Log("URL", getAbsoluteUrl(url));
        Debug.Log("params", params.toString());
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params,
                     JSONHttpHelper.JSONHttpResponseHandler responseHandler) {

        String u = getAbsoluteUrl(url);
        Debug.Log("URL", u);
        Debug.Log("params", params.toString());
        client.post(u, params, responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        if (relativeUrl.startsWith("http://") || relativeUrl.startsWith("https://"))
            return relativeUrl;
        return Cfg.BaseUrl + relativeUrl;
    }

    public void getUrl(String url, RequestParams params,
                       JSONHttpHelper.JSONHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void download(final String urlLink, final String fileName,
                                final String dest) {
        Thread dx = new Thread() {

            public void run() {
                File dir = new File(dest);
                if (dir.exists() == false) {
                    dir.mkdirs();
                }
                // Save the path as a string value

                try {
                    URL url = new URL(urlLink);
                    Debug.Log("FILE_NAME", "File name is " + fileName);
                    Debug.Log("FILE_URLLINK", "File URL is " + url);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    connection.getLastModified();
                    // this will be useful so that you can show a typical 0-100%
                    // progress bar
                    int fileLength = connection.getContentLength();
                    Debug.Log("FILE_Size", "File Size is " + fileLength);
                    // download the file
                    InputStream input = new BufferedInputStream(
                            url.openStream());
                    OutputStream output = new FileOutputStream(dir + "/"
                            + fileName);

                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;

                        output.write(data, 0, count);
                    }

                    Debug.Log("FILE_Size", "File total is " + total);
                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Debug.Log("ERROR ON DOWNLOADING FILES", "ERROR IS" + e);
                }
            }
        };
        dx.start();
    }
}
