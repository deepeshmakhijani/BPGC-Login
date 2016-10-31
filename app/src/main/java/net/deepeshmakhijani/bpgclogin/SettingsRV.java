package net.deepeshmakhijani.bpgclogin;

import android.content.Context;
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
    //    private static CheckBox lastChecked = null;
//    private static int lastCheckedPos = 0;
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
        String star = "";
        for (int i = 0; i < settingsItemFormat.get(position).getPassword().length(); i++) {
            star += "*";
        }
        holder.pass_word.setText(star);


////        Deepesh ---- Only One Check box to be selected  --- http://stackoverflow.com/questions/28972049/single-selection-in-recyclerview
//        holder.checkBox.setChecked(fonts.get(position).isSelected());
//        holder.checkBox.setTag(new Integer(position));
//
//        //for default check in first item
//        if(position == 0 && fonts.get(0).isSelected() && holder.checkBox.isChecked())
//        {
//            lastChecked = holder.checkBox;
//            lastCheckedPos = 0;
//        }
//
//        holder.checkBox.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                CheckBox cb = (CheckBox)v;
//                int clickedPos = ((Integer)cb.getTag()).intValue();
//
//                if(cb.isChecked)
//                {
//                    if(lastChecked != null)
//                    {
//                        lastChecked.setChecked(false);
//                        fonts.get(lastCheckedPos).setSelected(false);
//                    }
//
//                    lastChecked = cb;
//                    lastCheckedPos = clickedPos;
//                }
//                else
//                    lastChecked = null;
//
//                fonts.get(clickedPos).setSelected(cb.isChecked);
//            }
//        });
////    Deepesh
    }

    @Override
    public int getItemCount() {
        return settingsItemFormat.size();
    }

}


class ViewHolder extends RecyclerView.ViewHolder {
    TextView username, password;
    TextView user_name, pass_word;
    public ViewHolder(View itemView) {
        super(itemView);

        username = (TextView) itemView.findViewById(R.id.username_textview);
        user_name=(TextView)itemView.findViewById(R.id.username1);
        password = (TextView) itemView.findViewById(R.id.password_textview);
        pass_word = (TextView) itemView.findViewById(R.id.password1);

    }
}
