package me.walterceder.apitest;

import android.app.Application;
import android.util.Log;

/**
 * Created by iguest on 5/13/15.
 */
public class ApiTest extends Application{
    private static ApiTest instance; // singleton

    public ApiTest() {
        if (instance == null) {
            instance = this;
        } else {
            Log.e("ApiTest", "There is an error beep boop. You tried to create more than 1 the app");
        }
    }
}
