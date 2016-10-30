package net.deepeshmakhijani.bpgclogin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.Map;
import java.util.Vector;

public class Settings extends AppCompatActivity {
    SettingsRV adapter;
    private Vector<SettingsItemFormat> settingsItemFormats = new Vector<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        recyclerView =(RecyclerView)findViewById(R.id.settings_rv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //  recyclerView.setItemAnimator(new DefaultItemAnimator());
       settingsItemFormats =data();
        adapter = new SettingsRV(this,settingsItemFormats);
        recyclerView.setAdapter(adapter);
    }

public Vector<SettingsItemFormat> data(){
Vector<SettingsItemFormat> settingsItemFormats1 = new Vector<>();
    Map<String, ?> sharedPreferencesAll = sharedPreferences.getAll();
    for (Map.Entry<String, ?> entry: sharedPreferencesAll.entrySet()) {
      SettingsItemFormat settingsItemFormat = new SettingsItemFormat();
        settingsItemFormat.setUsername(entry.getKey());
        settingsItemFormat.setPassword(entry.getValue().toString());
        settingsItemFormats1.add(settingsItemFormat);
    }

return settingsItemFormats1;
}


}



