package com.example.cocabreak.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.cocabreak.R;
import com.example.cocabreak.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class WaterReminderReceiver extends BroadcastReceiver {

    public static final String CHANNEL_ID = "agua_recordatorio";
    public static final int NOTIFICATION_ID = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Crear el canal de notificación (necesario en Android 8+)
        crearCanal(context);

        // Intent para abrir la app al tocar la notificación
        Intent abrirApp = new Intent(context, MainActivity.class);
        abrirApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, abrirApp,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("¡Hora de tomar agua!")
                .setContentText("Recuerda hidratarte. Tu cuerpo te lo agradecerá.")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Llevas un rato sin tomar agua. Un vaso ahora te ayuda a reducir el consumo de Coca-Cola."))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, builder.build());
        }

        // Guardar la notificación en Firebase como historial
        guardarEnHistorial(context);

        // Reprogramar la siguiente alarma (en Android 6+ setExactAndAllowWhileIdle no repite solo)
        WaterReminderScheduler.reprogramarSiguiente(context);
    }

    private void crearCanal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID,
                    "Recordatorio de agua",
                    NotificationManager.IMPORTANCE_HIGH
            );
            canal.setDescription("Te recuerda tomar agua cada cierto tiempo");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(canal);
            }
        }
    }

    private void guardarEnHistorial(Context context) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        Map<String, Object> notif = new HashMap<>();
        notif.put("titulo", "¡Hora de tomar agua!");
        notif.put("mensaje", "Recordatorio para hidratarte");
        notif.put("fecha", System.currentTimeMillis());
        notif.put("tipo", "agua");

        FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(uid)
                .child("historialNotificaciones")
                .push()
                .setValue(notif);
    }
}