package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.Vector;

public class SetDefault extends AppCompatActivity {
    public static Context contextOfApplication;
    SetDefaultAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    TextView text;
    private Vector<SettingsItemFormat> settingsItemFormats = new Vector<>();

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_default);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.settings_rv_default);
        text = (TextView) findViewById(R.id.text);
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
        contextOfApplication = getApplicationContext();


        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        settingsItemFormats = data();
        if (settingsItemFormats.size() == 0) {
            text.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.INVISIBLE);
        }
        adapter = new SetDefaultAdapter(this, settingsItemFormats);
        recyclerView.setAdapter(adapter);
    }


    public Vector<SettingsItemFormat> data() {
        Vector<SettingsItemFormat> settingsItemFormats1 = new Vector<>();
        Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : sharedPreferencesAll.entrySet()) {
            SettingsItemFormat settingsItemFormat = new SettingsItemFormat();
            settingsItemFormat.setUsername(entry.getKey());
            settingsItemFormat.setPassword(entry.getValue().toString());
            settingsItemFormats1.add(settingsItemFormat);
        }

        return settingsItemFormats1;
    }
}