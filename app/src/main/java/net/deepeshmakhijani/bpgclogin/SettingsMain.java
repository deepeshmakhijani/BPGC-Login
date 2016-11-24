package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsMain extends AppCompatActivity {
    CardView card1, card2, card3;
    CheckBox checkBox;
    Context context;
    TextView textView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        textView=(TextView)findViewById(R.id.text_empty_message);
        card3 = (CardView) findViewById(R.id.card3);
        card1 = (CardView) findViewById(R.id.card1);
        card2 = (CardView) findViewById(R.id.card2);
        checkBox = (CheckBox) findViewById(R.id.check_auto_login);
        context = getApplicationContext();
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref1", 0);
        editor = sharedPreferences.edit();
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsMain.this, Settings.class);
                startActivity(intent);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/deepeshmakhijani/BPGC-Login";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(Intent.createChooser(i, "Choose Browser"));
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://cc.bits-goa.ac.in/ldap/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(Intent.createChooser(i, "Choose Browser"));
            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsMain.this, SetDefault.class);
                startActivity(intent);
            }
        });
        Boolean b1 = sharedPreferences.getBoolean("Auto", false);
        if (b1) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(context, "Auto Login Enabled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("Auto", true);
                    editor.commit();
                    startService();
                } else {
                    Toast.makeText(context, "Auto Login Disabled", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("Auto", false);
                    editor.commit();
                    stopService();
                }
            }
        });
    }

    public void startService() {
        startService(new Intent(getBaseContext(), MyService.class));

    }

    //    Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), MyService.class));
    }
}
