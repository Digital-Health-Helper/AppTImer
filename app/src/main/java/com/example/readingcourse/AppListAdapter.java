package com.example.readingcourse;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ListViewHolder> {
    private ArrayList<HashMap> topAppsList;
    private Context context;
    DataUtils dataUtils;
    private int AD_TYPE = 0;

    AppListAdapter(ArrayList<HashMap> al,DataUtils dataUtils) {
        this.topAppsList = al;
        this.dataUtils = dataUtils;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView appName, usageTime,timeLimit;
        ImageView appLogo;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.appName);
            usageTime = itemView.findViewById(R.id.appUsageTime);
            appLogo = itemView.findViewById(R.id.appLogo);
            timeLimit = itemView.findViewById(R.id.tv_time_limit);
        }
    }

    @Override
    public int getItemViewType(int position) {
        HashMap h = this.topAppsList.get(position);
        if (h.get("adview") != null) return AD_TYPE;
        return 1;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        this.context = viewGroup.getContext();

            itemView = LayoutInflater.from(this.context)
                    .inflate(R.layout.top_app_list_item, viewGroup, false);


        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {
        HashMap<String, Long> hm = this.topAppsList.get(i);
        HashMap.Entry<String, Long> entry = hm.entrySet().iterator().next();
        long usageTime = entry.getValue();
        final String packageName = entry.getKey();
        Log.e("HELP",packageName);
        final Long timeLimit = AppTimer.appTimerPreferences.getLong(packageName,-1L);
        if(listViewHolder.timeLimit!=null&& timeLimit!=null) {

                long hours = TimeUnit.MILLISECONDS.toHours(timeLimit);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLimit - TimeUnit.HOURS.toMillis(hours));
                if (hours != 0) {
                    listViewHolder.timeLimit.setText(String.format(Locale.getDefault(), "%dh %02dmin", (int) hours, (int) minutes));
                } else if (minutes != 0) {
                    listViewHolder.timeLimit.setText(String.format(Locale.getDefault(), "%dmin", (int) minutes));
                } else {
                    listViewHolder.timeLimit.setText(String.format(Locale.getDefault(), "No Timer Set", 1));
                }

        }
        listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.settings_dialog, null);
                final Spinner hourSpinner = dialogView.findViewById(R.id.hourSpinner);
                final Spinner minuteSpinner = dialogView.findViewById(R.id.minuteSpinner);
                final Switch nagSwitch = dialogView.findViewById(R.id.nagSwitch);
                final DataUtils dataUtils = new DataUtils(context);
                if (timeLimit != -1) {
                    long hours = TimeUnit.MILLISECONDS.toHours(timeLimit);
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLimit - TimeUnit.HOURS.toMillis(hours));
                    hourSpinner.setSelection((int)hours);
                    minuteSpinner.setSelection((int)minutes/10);
                }
                builder.setView(dialogView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hours = Integer.parseInt((String) hourSpinner.getSelectedItem());
                                int minutes = Integer.parseInt((String) minuteSpinner.getSelectedItem());
                                boolean nag = nagSwitch.isChecked();
                                long timeLimit = TimeUnit.HOURS.toMillis(hours)
                                        + TimeUnit.MINUTES.toMillis(minutes);
                                SharedPreferences.Editor editor = AppTimer.appTimerPreferences.edit();
                                Log.e("HELP",timeLimit+"");
                                editor.putLong(packageName, timeLimit);
                                editor.commit();
                                editor.apply();
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });
        PackageManager pm = this.context.getPackageManager();
        ApplicationInfo applicationInfo;

        try {
            applicationInfo = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }

        String applicationName = (String) (applicationInfo != null ? pm.getApplicationLabel(applicationInfo) : packageName);
        Drawable appIcon = applicationInfo != null ? pm.getApplicationIcon(applicationInfo) : null;
        if (appIcon != null && listViewHolder.appLogo != null) {
            listViewHolder.appLogo.setImageDrawable(appIcon);
        }

        if (listViewHolder.usageTime != null) {
            long hours = TimeUnit.MILLISECONDS.toHours(usageTime);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(usageTime - TimeUnit.HOURS.toMillis(hours));
            if (hours != 0) {
                listViewHolder.usageTime.setText(String.format(Locale.getDefault(), "%dh %02dmin", (int) hours, (int) minutes));
            } else if (minutes != 0) {
                listViewHolder.usageTime.setText(String.format(Locale.getDefault(), "%dmin", (int) minutes));
            } else {
                listViewHolder.usageTime.setText(String.format(Locale.getDefault(), "Less than %dmin", 1));
            }
        }

        if (listViewHolder.usageTime != null) {
            listViewHolder.appName.setText(applicationName);
        }
    }

    @Override
    public int getItemCount() {
        return topAppsList.size();
    }
}
