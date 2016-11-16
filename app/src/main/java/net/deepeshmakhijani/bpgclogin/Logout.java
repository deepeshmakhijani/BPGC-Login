package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by deepesh on 11/7/16.
 */

public class Logout extends AsyncTask<String, Void, String> {

    private final String username = MainActivity.user_data.getText().toString().trim();
    private final String password = MainActivity.pass_data.getText().toString().trim();
    private final Context context;
    public OkHttpClient client = getOkHttpClient.getOkHttpClient();
    private String result;

    Logout(Context c) {
        context = c;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            RequestBody body = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("mode", "193")
                    .build();

            Request request = new Request.Builder()
                    .url(MainActivity.URL2)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            result = response.body().string();
            return result;
        } catch (Exception e) {
            result = "ERR";
            return null;
        }
    }

    protected void onPostExecute(String result) {
        if (result != null) {
            String pattern = Pattern.quote("<message><![CDATA[") + "(.*?)" + Pattern.quote("]]></message>");
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(result);
            if (m.find()) {
                Toast.makeText(this.context, m.group(1), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.context, "Kindly Reconnect the WiFi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}