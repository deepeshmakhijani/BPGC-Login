package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by shubhamk on 22/11/16.
 */

public class SetDefaultAdapter extends RecyclerView.Adapter<SetDefaultAdapter.ViewHolder> {
    Context applicationContext = SetDefault.getContextOfApplication();
    SharedPreferences shared = applicationContext.getSharedPreferences("MyPref1", 0);
    SharedPreferences.Editor editor = shared.edit();
    private Context context;
    private Vector<SettingsItemFormat> settingsItemFormat;
    private int lastCheckedPosition = -1;

    public SetDefaultAdapter(Context context, Vector<SettingsItemFormat> settingsItemFormat) {
        this.context = context;
        this.settingsItemFormat = settingsItemFormat;
    }

    @Override
    public SetDefaultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.set_default_item_format, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(SetDefaultAdapter.ViewHolder holder, int position) {
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
        String user = shared.getString("Default", null);
        if (user != null) {
            if (user.equals(holder.user_name)) {
                holder.radiobutton.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return settingsItemFormat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, password;
        TextView user_name, pass_word, colon, colon1;
        RadioButton radiobutton;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username_textview_new);
            user_name = (TextView) itemView.findViewById(R.id.username1_new);
            password = (TextView) itemView.findViewById(R.id.password_textview_new);
            pass_word = (TextView) itemView.findViewById(R.id.password1_new);
            colon = (TextView) itemView.findViewById(R.id.colon1_new);
            colon1 = (TextView) itemView.findViewById(R.id.colon2_new);
            radiobutton = (RadioButton) itemView.findViewById(R.id.radioButton2_new);

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
        }
    }


}