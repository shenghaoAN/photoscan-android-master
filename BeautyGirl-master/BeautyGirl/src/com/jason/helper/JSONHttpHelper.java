package com.jason.helper;

import android.app.Activity;
import android.content.Context;

import com.jason.Debug;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by shenghao on 2015/5/14.
 */
public class JSONHttpHelper extends Activity {

    public static class JSONHttpResponseHandler extends TextHttpResponseHandler {
        public Context context1;
        public String error = "";
        public String response;
        public JSONArray datas;
        public boolean rawResponse = false;

        public void JSONHttpResponseHandler(Context context) {
            this.context1 = context;
            this.error = "";
        }

        public void Success() {

        }

        public void Failure() {

        }

        @Override
        public void onFailure(int i, Header[] headers, String responseBody, Throwable throwable) {
            // Response failed :(
            error = throwable.getMessage();
            Debug.Log(error, response);
            Failure();
        }

        @Override
        public void onSuccess(int i, Header[] headers, String responseBody) {
            response = responseBody;

            Debug.Log("server response:", response);
            if (rawResponse) {
                Success();
                return;
            }
            try {
                JSONObject json = new JSONObject(response.trim());

                datas = (JSONArray) json.get("data");

                if (!error.trim().equals("")) {
                    Debug.Log(error, response);
                    Failure();
                    return;
                }
                Success();
                Debug.Log("JSON success", "triggered");

            } catch (Exception e) {
                e.printStackTrace();
                //the server return unexpected error
                Debug.Log("JSON Decode Error", response);
                error = "ERR_ServerError";
                Failure();
                return;
            }
        }
    }
}
