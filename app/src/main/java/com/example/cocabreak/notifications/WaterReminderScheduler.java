package com.example.cocabreak.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

public class WaterReminderScheduler {

    private static final String PREFS = "notificaciones_agua";
    private static final String KEY_ACTIVO = "recordatorio_activo";
    private static final String KEY_INTERVALO = "intervalo_minutos";
    public static final int INTERVALO_DEFAULT = 60; // 60 minutos por defecto

    /**
     * Activa los recordatorios de agua con el intervalo indicado (en minutos).
     */
    public static void activar(Context context, int intervaloMinutos) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        PendingIntent pendingIntent = crearPendingIntent(context);

        long intervaloMs = (long) intervaloMinutos * 60 * 1000;
        long primerDisparo = System.currentTimeMillis() + intervaloMs;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, primerDisparo, pendingIntent);
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, primerDisparo, intervaloMs, pendingIntent);
        }

        // Guardar preferencias
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean(KEY_ACTIVO, true)
                .putInt(KEY_INTERVALO, intervaloMinutos)
                .apply();
    }

    /**
     * Cancela los recordatorios de agua.
     */
    public static void cancelar(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(crearPendingIntent(context));
        }

        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_ACTIVO, false).apply();
    }

    /**
     * Reprograma la siguiente alarma (llamado desde WaterReminderReceiver
     * para encadenar alarmas en Android 6+).
     */
    public static void reprogramarSiguiente(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        boolean activo = prefs.getBoolean(KEY_ACTIVO, false);
        int intervalo = prefs.getInt(KEY_INTERVALO, INTERVALO_DEFAULT);

        if (activo) {
            activar(context, intervalo);
        }
    }

    public static boolean estaActivo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_ACTIVO, false);
    }

    public static int getIntervalo(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_INTERVALO, INTERVALO_DEFAULT);
    }

    private static PendingIntent crearPendingIntent(Context context) {
        Intent intent = new Intent(context, WaterReminderReceiver.class);
        return PendingIntent.getBroadcast(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}