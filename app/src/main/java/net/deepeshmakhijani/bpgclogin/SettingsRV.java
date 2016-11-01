package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by shubhamk on 30/10/16.
 */

public class SettingsRV extends RecyclerView.Adapter<SettingsRV.ViewHolder> {

    Context applicationContext = Settings.getContextOfApplication();
    SharedPreferences shared = applicationContext.getSharedPreferences("MyPref1", 0);
    SharedPreferences.Editor editor = shared.edit();
    SharedPreferences shared1 = applicationContext.getSharedPreferences("MyPref", 0);
    SharedPreferences.Editor editor1 = shared1.edit();
    private Context context;
    private Vector<SettingsItemFormat> settingsItemFormat;
    private int lastCheckedPosition = -1;
    public SettingsRV(Context context, Vector<SettingsItemFormat> settingsItemFormat) {
        this.context = context;
        this.settingsItemFormat = settingsItemFormat;
    }

    @Override
    public SettingsRV.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.settings_item_format, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(SettingsRV.ViewHolder holder, int position) {
        holder.user_name.setText(settingsItemFormat.get(position).getUsername());
        String star = "";
        for (int i = 0; i < settingsItemFormat.get(position).getPassword().length(); i++) {
            star += "*";
        }
        holder.pass_word.setText(star);
        int pos = shared.getInt("DefaultPos", -5);
        if (position == pos) {
            holder.radiobutton.setChecked(true);
        } else {
            holder.radiobutton.setChecked(position == lastCheckedPosition);
        }

    }

    @Override
    public int getItemCount() {
        return settingsItemFormat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, password;
        TextView user_name, pass_word, colon, colon1;
        CheckBox checkBox;
        RadioButton radiobutton;
        ImageButton delete;
        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            username = (TextView) itemView.findViewById(R.id.username_textview);
            user_name = (TextView) itemView.findViewById(R.id.username1);
            password = (TextView) itemView.findViewById(R.id.password_textview);
            pass_word = (TextView) itemView.findViewById(R.id.password1);
            colon = (TextView) itemView.findViewById(R.id.colon1);
            colon1 = (TextView) itemView.findViewById(R.id.colon2);
            delete = (ImageButton) itemView.findViewById(R.id.deletebutton);
            radiobutton = (RadioButton) itemView.findViewById(R.id.radioButton2);
            radiobutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemRangeChanged(0, settingsItemFormat.size());
                    final String username = user_name.getText().toString().trim();
                    editor.putString("Default", username);
                    editor.putInt("DefaultPos", getAdapterPosition());
                    editor.commit();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String username = user_name.getText().toString().trim();
                    String check;
                    check = shared.getString("Default", null);
                    if (username.equals(check)) {
                        editor.remove("Default");
                        editor.commit();
                    }
                    editor1.remove(username);
                    editor1.commit();
                    settingsItemFormat.remove(getAdapterPosition());
                    // Log.e("TAG",getItemCount()+"");
                    notifyItemRemoved(getAdapterPosition());
                    //Log.e("TAG",getAdapterPosition()+" pos");

                }
            });
        }
    }

}


