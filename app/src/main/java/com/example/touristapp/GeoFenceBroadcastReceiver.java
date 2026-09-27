package com.example.touristapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeoFenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeoFenceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent == null) {
            Log.e(TAG, "GeofencingEvent is null");
            return;
        }

        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Geofencing error: " + geofencingEvent.getErrorCode());
            return;
        }

        int transitionType = geofencingEvent.getGeofenceTransition();
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

        if (triggeringGeofences != null && !triggeringGeofences.isEmpty()) {
            for (Geofence geofence : triggeringGeofences) {
                Log.d(TAG, "Triggered Geofence: " + geofence.getRequestId());
            }
        }

        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Toast.makeText(context, "Entered Geofence Area 🚀", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Entered Geofence Area");
        } else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Toast.makeText(context, "Exited Geofence Area ⚠️", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Exited Geofence Area");
        }
    }
}
