package net.deepeshmakhijani.bpgclogin;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;
import android.support.v4.app.NotificationCompat;


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

    //    Broadcast Receiver
    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        //        Get shared preferences
        public SharedPreferences sharedPreferences, shared;

        //        On broadcast receive
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
                    if (user1 != null) {
                        final String pass1 = sharedPreferences.getString(user1, null);

//                   Get wifi ssid

//                    Execute Login2 after 1 sec delay
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new Login(c).execute(user1, pass1);

                            }
                        }, 5000);
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //    This is called when the service gets started initially
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        return START_STICKY;

    }

    //    This is the main function which is executed for the service
    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");

        registerReceiver(receiver, filter);

    }

    //    On Destroy - When the sevice is stopped from MainActivity
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //    Same Login2 function as used in MainActivity except the toast

    class Login extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();
        private String result, message;

        private Context c;

        Login(Context context) {
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
                        .url("https://campnet.bits-goa.ac.in:8090/login.xml")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Auto Login";
                String description = "Automatic Login";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(description, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        protected void onPostExecute(String result) {
            //   Notification Builder
            createNotificationChannel();

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(c, "Automatic Login");

            mBuilder.setContentTitle("BPGC LOGIN");


            if (result != null) {

                String pattern = Pattern.quote("<message><![CDATA[") + "(.*?)" + Pattern.quote("]]></message>");
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(result);
                if (m.find()) {

                    message = m.group(1);

                    if (message.equals("You have successfully logged in")) {
                        mBuilder.setSmallIcon(R.drawable.wifi);
                    } else {
                        mBuilder.setSmallIcon(R.drawable.wifi_off);
                    }

                    mBuilder.setContentText(message);
                    NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(001, mBuilder.build());
                }
            }
        }
    }
}
