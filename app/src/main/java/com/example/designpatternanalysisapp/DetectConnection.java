package com.example.designpatternanalysisapp;

import android.content.Context;
import android.net.ConnectivityManager;


class DetectConnection {

    /**
     * Check internet connection
     *
     * @param context Context
     * @return boolean if internet connection is available
     *
     * @see https://stackoverflow.com/questions/17959561/android-how-to-prevent-webview-to-load-when-no-internet-connection
     */
    static boolean checkInternetConnection(Context context) {
        ConnectivityManager con_manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected());
    }
}