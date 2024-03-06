package com.app.doit.alarm;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.doit.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ((Intent.ACTION_BOOT_COMPLETED).equals(intent.getAction())) {
            // TODO: Work on this later
            // reset all alarms
        } else {
            String todoTitle = intent.getStringExtra("todoTitle");
            if (todoTitle != null) {
                createNotification(context, todoTitle);
            }
        }
    }

    private void createNotification(Context context, String todoTitle) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "do-it-channel-id")
                .setSmallIcon(R.drawable.todo_list_icon)
                .setContentTitle(todoTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify((int) System.currentTimeMillis(), builder.build());
    }
}
