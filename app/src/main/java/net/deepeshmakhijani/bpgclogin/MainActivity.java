package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String URL1 = "https://20.20.2.11/login.html";
    public static final String URL2 = "https://10.1.0.10:8090/httpclient.html";
    static EditText user_data, pass_data;
    Button login_btn, logout_btn;
    CheckBox rc, sd;
    private SharedPreferences sharedPreferences, shared;
    private SharedPreferences.Editor editor, editor1;

    public static String getCurrentSsid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            ssid = connectionInfo.getSSID();
        }
        return ssid;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting the sharedPreferences
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = sharedPreferences.edit();
        shared = getApplicationContext().getSharedPreferences("MyPref1", 0); // 0 - for private mode
        editor1 = sharedPreferences.edit();
        login_btn = (Button) findViewById(R.id.login_btn);
        logout_btn = (Button) findViewById(R.id.logout_btn);
        user_data = (EditText) findViewById(R.id.user_data);
        pass_data = (EditText) findViewById(R.id.pass_data);
        rc = (CheckBox) findViewById(R.id.rc);
        sd = (CheckBox) findViewById(R.id.sd);
        rc.setChecked(true);
        String user1, pass1;
        user1 = shared.getString("Default", null);
        user_data.clearFocus();
        user_data.setText(user1);
        pass1 = sharedPreferences.getString(user1, null);
        pass_data.setText(pass1);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifi = getCurrentSsid(MainActivity.this);
                if (wifi == null) {
                    Toast.makeText(MainActivity.this, "Connect to a WIFI Network", Toast.LENGTH_SHORT).show();
//                } else if (wifi.equals("\"BPGC-HOSTEL\"")) {
//                    new Login1(MainActivity.this).execute();
//                } else {
//                    new Login2(MainActivity.this).execute();
//                }
                } else {
                    new Login2(MainActivity.this).execute();
                }
                if (rc.isChecked()) {
                    final String username = user_data.getText().toString().trim();
                    final String password = pass_data.getText().toString().trim();
                    editor.putString(username, password);
                    editor.commit();
                }
            }
        });

        rc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    sd.setChecked(false);
                    sd.setEnabled(false);
                } else {
                    sd.setEnabled(true);
                }
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifi = getCurrentSsid(MainActivity.this);
                if (wifi == null) {
                    Toast.makeText(MainActivity.this, "Connect to a WIFI Network", Toast.LENGTH_SHORT).show();
                } else {
                    new Logout(MainActivity.this).execute();
                }
            }
        });
        pass_data.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                final String username = user_data.getText().toString().trim();
                String pass = sharedPreferences.getString(username, null);
                pass_data.setText(pass);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        shared = getApplicationContext().getSharedPreferences("MyPref1", 0); // 0 - for private mode
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        user_data = (EditText) findViewById(R.id.user_data);
        pass_data = (EditText) findViewById(R.id.pass_data);
        String user1, pass1;
        user1 = shared.getString("Default", null);
        pass1 = sharedPreferences.getString(user1, null);
        user_data.setText(user1);
        pass_data.setText(pass1);
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
            Intent intent = new Intent(MainActivity.this, SettingsMain.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


