package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.Vector;

public class Settings extends AppCompatActivity {
    public static Context contextOfApplication;
    SettingsRV adapter;
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
        setContentView(R.layout.activity_settings);
        recyclerView =(RecyclerView)findViewById(R.id.settings_rv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        text = (TextView) findViewById(R.id.textdefault);
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
        //  recyclerView.setItemAnimator(new DefaultItemAnimator());
        settingsItemFormats = data();
        if (settingsItemFormats.size() == 0) {
            text.setVisibility(View.VISIBLE);
        } else {
            text.setVisibility(View.INVISIBLE);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteAll) {
            SimpleAlertDialog alert = new SimpleAlertDialog();
            alert.showDialog(this, "Alert", "Are you sure you want to delete all saved credientials ?", "YES", "NO", "HI", true, true, false);
            alert.setClickListener(new SimpleAlertDialog.ClickListener() {
                @Override
                public void onPosButtonClick() {
                    editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    settingsItemFormats.clear();
                    adapter.notifyDataSetChanged();
                    text.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNegButtonClick() {

                }

                @Override
                public void onNeuButtonClick() {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

}



