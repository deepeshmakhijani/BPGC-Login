package net.deepeshmakhijani.bpgclogin;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by deepesh on 20/11/16.
 */

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");

        registerReceiver(receiver, filter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

        unregisterReceiver(receiver);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

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
                        }, 1000);
                    }
                }
            }


        }

        //    Same Login2 function as used in MainActivity except the toast

        class Login2 extends AsyncTask<String, Void, String> {
            private OkHttpClient client = getOkHttpClient.getOkHttpClient();
            private String result, message;

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
//            Notification
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c);
                mBuilder.setSmallIcon(R.drawable.logo_bits);
                mBuilder.setContentTitle("MOTHERFUCKAS");

                if (result != null) {
                    String pattern = Pattern.quote("<message><![CDATA[") + "(.*?)" + Pattern.quote("]]></message>");
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(result);
                    if (m.find()) {
                        message = m.group(1);
                        mBuilder.setContentText(message);
                        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

                        int notificationID = 001;
                        mNotificationManager.notify(notificationID, mBuilder.build());
                    }
                }
            }
        }
    };
}
