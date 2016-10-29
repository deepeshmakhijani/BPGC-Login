package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String URL1 = "https://20.20.2.11/login.html";
    private static final String URL2 = "https://10.1.0.10:8090/httpclient.html";
    public OkHttpClient client = getUnsafeOkHttpClient();
    Button login_btn;
    EditText user_data, pass_data;

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login_btn = (Button) findViewById(R.id.login_btn);

        user_data = (EditText) findViewById(R.id.user_data);
        pass_data = (EditText) findViewById(R.id.pass_data);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifi = getCurrentSsid(MainActivity.this);
                if (wifi == null) {
                    Toast.makeText(MainActivity.this, "Connect to a WIFI Network", Toast.LENGTH_SHORT).show();
                } else if (wifi.equals("\"BPGC-HOSTEL\"")) {
                    new Login1().execute();
                } else {
                    new Login2().execute();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class Login1 extends AsyncTask<String, Void, String> {

        final String username = user_data.getText().toString().trim();
        final String password = pass_data.getText().toString().trim();
        String result;

        @Override
        protected String doInBackground(String... params) {

            try {
                RequestBody body = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .add("buttonClicked", "4")
                        .build();

                Request request = new Request.Builder()
                        .url(URL1)
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
            String[] parts = result.split("title");
            if (parts[1].equals(">Web Authentication Failure</") || parts[1].equals(">Logged In</")) {
                new Login2().execute();
            } else if (parts[1].equals(">Web Authentication</")) {
                Toast.makeText(MainActivity.this, "Please check your credentials", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Wait for the issue to be solved", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Login2 extends AsyncTask<String, Void, String> {
        final String username = user_data.getText().toString().trim();
        final String password = pass_data.getText().toString().trim();
        String result;

        @Override
        protected String doInBackground(String... params) {
            try {
                RequestBody body = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .add("mode", "191")
                        .build();

                Request request = new Request.Builder()
                        .url(URL2)
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
            String[] parts = result.split("message");
            if (parts[1].equals("><![CDATA[You have successfully logged in]]></"))
                Toast.makeText(MainActivity.this, "You have successfully Logged In", Toast.LENGTH_SHORT).show();
            else if (parts[1].equals("><![CDATA[The system could not log you on. Make sure your username or password is correct]]></"))
                Toast.makeText(MainActivity.this, "Please check your credentials", Toast.LENGTH_SHORT).show();
            else if (parts[1].equals("><![CDATA[Your data transfer has been exceeded, Please contact the administrator]]></"))
                Toast.makeText(MainActivity.this, "Sorry your data is exceeded", Toast.LENGTH_SHORT).show();
            else if (parts[1].equals("><![CDATA[???]]></"))
                Toast.makeText(MainActivity.this, "Maximum Login Limit Reached", Toast.LENGTH_SHORT).show();
        }
    }
}
