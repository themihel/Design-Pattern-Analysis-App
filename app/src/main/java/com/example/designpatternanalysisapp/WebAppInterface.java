package com.example.designpatternanalysisapp;

import android.webkit.JavascriptInterface;

public class WebAppInterface {

    /**
     * Used in javascript -> ability to track events from platform out to another server
     * Prevents problems with Content-Security-Policy (CSP)
     *
     * @see https://developer.android.com/reference/android/webkit/JavascriptInterface
     * @see https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP
     *
     * @param trackingKey Tracking-Key
     * @param version     Version of modification
     */
    @JavascriptInterface
    public void track(String trackingKey, String version) {
        Tracking.track(trackingKey, version);
    }
}
