package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by shubhamk on 30/10/16.
 */

public class SettingsRV extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    Vector <SettingsItemFormat> settingsItemFormat;

    public SettingsRV(Context context, Vector<SettingsItemFormat> settingsItemFormat) {
        this.context = context;
        this.settingsItemFormat = settingsItemFormat;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.settings_item_format, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user_name.setText(settingsItemFormat.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return settingsItemFormat.size();
    }

}


class ViewHolder extends RecyclerView.ViewHolder {
    TextView username;
    TextView user_name;
    public ViewHolder(View itemView) {
        super(itemView);

        username = (TextView) itemView.findViewById(R.id.username_textview);
        user_name=(TextView)itemView.findViewById(R.id.username1);

    }
}
