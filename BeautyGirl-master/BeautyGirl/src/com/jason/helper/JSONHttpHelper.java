package com.jason.helper;

import android.app.Activity;
import android.content.Context;

import com.jason.Debug;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by shenghao on 2015/5/14.
 */
public class JSONHttpHelper extends Activity {

    public static class JSONHttpResponseHandler extends TextHttpResponseHandler {
        public Context context1;
        public String error = "";
        public String response;
        public JSONArray datas;
        public int totalNum;
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

            // 获取文件响应类型
            String contentType_value = null;

            // 遍历头部信息
            for (Header header : headers) {
                // 获取contentType_value的头部信息
                if (header.getName().equals("Content-Type")) {
                    // 获取他的value值
                    contentType_value = header.getValue();
                }
            }
            Debug.Log("contentType_value", contentType_value);
            // 定义服务器端缺省的编码方式
            String default_charset = "UTF-8";
            // 处理contentType_value来获取编码方式
            // 判断是否为null
            if (contentType_value != null) {
                // 判断是否有=字符
                if (contentType_value.contains("=")) {
                    // 获取=字符位置
                    int index = contentType_value.indexOf("=");
                    // 从=所在位置的下一个字符开始截取，返回服务器端的编码
                    default_charset = contentType_value.substring(
                            index + 1, contentType_value.length());
                } else {
                    String result = new String(responseBody);
                    default_charset = getCharSet(result);
                }
            } else {
                String result = new String(responseBody);
                default_charset = getCharSet(result);
            }
            Debug.Log("编码是：", default_charset + "");


            try {
                response = new String(responseBody.getBytes(), "UTF-8");
                Debug.Log("server response:", response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                response = responseBody;
            }

            if (rawResponse) {
                Success();
                return;
            }
            try {
                JSONObject json = new JSONObject(response.trim());

                if (json.has("totalNum")) {
                    totalNum = json.getInt("totalNum");
                } else if (json.has("displayNum")) {
                    totalNum = json.getInt("displayNum");
                }
                Debug.Log("totalNum is ", totalNum + "");

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

        /**
         * 获取网页源代码中默认的编码
         *
         * @param result
         * @return
         */
        public String getCharSet(String result) {
            String defaultCharset = null;
            // <mate http-equiv="Content-Type"
            // content="text/html; charset=GBK" /> //html4
            // <mate charset="UTF-8">
            if (result != null) {
                if (result
                        .contains("content=\"text/html; charset=GBK\"")) {
                    defaultCharset = "GBK";
                } else if (result
                        .contains("content=\"text/html; charset=UTF-8\"")) {
                    defaultCharset = "UTF-8";
                } else if (result
                        .contains("content=\"text/html; charset=GB2312\"")) {
                    defaultCharset = "GB2312";
                } else if (result.contains("charset=\"UTF-8\"")) {
                    defaultCharset = "UTF-8";
                } else if (result.contains("charset=\"UTF-8\"")) {
                    defaultCharset = "GBK";
                } else if (result.contains("charset=\"gb18030\"")) {
                    defaultCharset = "UTF-8";
                }
            }
            return defaultCharset;
        }

    }
}
