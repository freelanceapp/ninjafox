package com.mojodigi.ninjafox.View;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.mojodigi.ninjafox.Activity.MainActivity;
import com.mojodigi.ninjafox.Database.Record;
import com.mojodigi.ninjafox.R;
import com.mojodigi.ninjafox.CustomControl.RelativeTimeTextView;
import com.mojodigi.ninjafox.Unit.BrowserUtility;
import com.mojodigi.ninjafox.Unit.IntentUtility;


import java.util.List;



public class RecordAdapter extends ArrayAdapter<Record> {
    private Context context;
    private int layoutResId;
    private List<Record> list;
    int flag;
    public RecordAdapter(Context context, int layoutResId, List<Record> list,int flag) {
        super(context, layoutResId, list);
        this.context = context;
        this.layoutResId = layoutResId;
        this.list = list;
        this.flag=flag;
    }

    private static class Holder {
        TextView title;
        RelativeTimeTextView time;
        TextView url;
        ImageView menuIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            holder = new Holder();
            holder.title = (TextView) view.findViewById(R.id.record_item_title);
            holder.time = (RelativeTimeTextView) view.findViewById(R.id.record_item_time);
            holder.url = (TextView) view.findViewById(R.id.record_item_url);
            holder.menuIcon = view.findViewById(R.id.menuIcon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        final Record record = list.get(position);
        holder.title.setText(record.getTitle());
        holder.time.setReferenceTime(record.getTime());
        holder.url.setText(record.getURL());


        holder.menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popUpmenu(view,record);
            }
        });

        return view;
    }

    private void popUpmenu(View view , final Record record) {
        final PopupMenu popup = new PopupMenu(context, view);
        //inflating menu from xml resource
        popup.inflate(R.menu.options_menu_home);

popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menu_new_atb:
                //((MainActivity)context).addAlbum(context.getString(R.string.album_untitled), record.getURL(), false, null);

                ((MainActivity)context).hideomnibox(false);
                ((MainActivity)context).updateAlbum(record.getURL());
                ((MainActivity)context).increaseTabCount();
                ((MainActivity)context). setTabValue();
                jmmToast.show(context, R.string.toast_new_tab_successful);
                break;
            case R.id.menu_copy_link:
                BrowserUtility.copyURL(context, record.getURL());
                break;

            case R.id.menu_share_link:
                IntentUtility.share(context, record.getTitle(), record.getURL());
                break;
            case R.id.menu_share_delete:
                list.remove(record);
                ((MainActivity)context).deleteHB(record,flag);
                notifyDataSetChanged();
                break;


        }
        return false;

    }
});


        popup.show();

    }
}