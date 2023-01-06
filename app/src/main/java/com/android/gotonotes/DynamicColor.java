package com.android.gotonotes;

import android.app.Application;
import com.google.android.material.color.DynamicColors;

public class DynamicColor  extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
