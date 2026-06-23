package com.example.cocabreak.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Reactiva los recordatorios de agua después de que el teléfono se reinicia,
 * ya que las AlarmManager se borran al apagar el dispositivo.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())
                || "android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction())) {

            if (WaterReminderScheduler.estaActivo(context)) {
                int intervalo = WaterReminderScheduler.getIntervalo(context);
                WaterReminderScheduler.activar(context, intervalo);
            }
        }
    }
}
