package eu.iums.fitwork;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, i , PendingIntent.FLAG_IMMUTABLE);
        PendingIntent pendingIntentDrink = PendingIntent.getActivity(context, 2, i, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder_break = new NotificationCompat.Builder(context, "break_alert")
                .setSmallIcon(R.drawable.ic_streching_sport_and_notification_icon)
                .setContentTitle("Fit@Work")
                .setContentText("Ihre Mittagspause beginnt jetzt")
                .setStyle(new NotificationCompat.BigTextStyle()
                       .bigText("Ihre Mittagspause beginnt jetzt"))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationCompat.Builder builder_drink = new NotificationCompat.Builder(context, "drink_alert")
                .setSmallIcon(R.drawable.ic_streching_sport_and_notification_icon)
                .setContentTitle("Fit@Work")
                .setContentText("Zeit zu Trinken")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Zeit zu Trinken"))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntentDrink);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder_break.build());
        notificationManagerCompat.notify(2, builder_drink.build());
    }
}
