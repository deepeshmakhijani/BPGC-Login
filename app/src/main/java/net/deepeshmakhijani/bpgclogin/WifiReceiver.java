package net.deepeshmakhijani.bpgclogin;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.NotificationCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by deepesh on 11/18/16.
 */

//Broadcast receiver to detect changes in wifi
public class WifiReceiver extends BroadcastReceiver {
    public String message;
    private SharedPreferences sharedPreferences, shared;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Context c = context;

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

//        Detect changes in wifi
        if (info != null && info.isConnected()) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//            If wifi is not previously connected
            if (!mWifi.isConnected()) {

//                Get default username, password
                sharedPreferences = context.getApplicationContext().getSharedPreferences("MyPref", 0);
                shared = context.getApplicationContext().getSharedPreferences("MyPref1", 0);
                final String user1 = shared.getString("Default", null);
                final String pass1 = sharedPreferences.getString(user1, null);

//                Get wifi ssid
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ssid = wifiInfo.getSSID();

                if (((ssid.equals("\"BPGC-HOSTEL\"") || ssid.equals("\"BPGC-WIFI\"")) && (user1 != null) && (pass1 != null))) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            new Login2(c).execute(user1, pass1);
                        }
                    }, 100);
                }
            }
        }


    }

    //    Same Login2 function as used in MainActivity except the toast

    class Login2 extends AsyncTask<String, Void, String> {
        private OkHttpClient client = getOkHttpClient.getOkHttpClient();
        private String result;

        private Context c;

        public Login2(Context context) {
            c = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody body = new FormBody.Builder()
                        .add("username", params[0])
                        .add("password", params[1])
                        .add("mode", "191")
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
                    message = m.group(1);
                } else {
                    message = "Kindly Reconnect the WiFi";
                }
            } else {
                message = "Kindly Reconnect the WiFi";
            }


            //                    Send notification


            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c);
            mBuilder.setSmallIcon(R.drawable.logo_bits);
            mBuilder.setContentTitle(message);
            mBuilder.setContentText("Enjoy Bro |m|");
            NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

            int notificationID = 001;
            mNotificationManager.notify(notificationID, mBuilder.build());


        }
    }


}
