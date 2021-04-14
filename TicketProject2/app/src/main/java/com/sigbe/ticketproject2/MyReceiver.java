package com.sigbe.ticketproject2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends android.content.BroadcastReceiver {

    private static final String TAG = "error";

    @Override
    public void onReceive(android.content.Context context, android.content.Intent intent) {
        //Tu lógica de negocio irá aquí. En caso de requerir más de unos milisegundos, deberías usar un servicio
        creaNotificacion(System.currentTimeMillis(), "NOTIFICACION", "Notificación aqui", context);
    }

    public static void creaNotificacion(long when, String title, String content, Context context) {
        try {
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.checked);

            Intent notificationIntent = new Intent(context, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setWhen(when)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.checked)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);

            notificationManager.notify((int) when, notificationBuilder.build());

        } catch (Exception e) {
            Log.e(TAG, "crearNotificaciones:" + e.getMessage());
        }
    }
}