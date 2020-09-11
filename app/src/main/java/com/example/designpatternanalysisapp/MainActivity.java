package com.example.designpatternanalysisapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint({"SetJavaScriptEnabled", "HardwareIds"})
public class MainActivity extends AppCompatActivity {

    WebView mWebview;

    // Group information - Example
    // Group 1: Trial group
    // Group 2: Control group
    // Group 3: Own group for testing modifications (can be filtered afterwards)
    static final String GROUP = "3";

    // Modification server
    static final String MODIFICATIONS_BASE_URL = "https://www.your-url.com/modification/" + GROUP + "/";

    // Indicates which character of the JS file is the currently used version
    static final int MODIFICATION_VERSION_POSITION = 12;

    // Tracking Server information
    static final String TRACK_URL = "https://www.your-url.com/trackaction";

    // Adaptable user agent
    static final String USER_AGENT = "Design-Pattern-Analysis-App";

    // Information about the platform to be tracked
    static final String PLATFORM_URL = "www.platform.com";
    static final String PLATFORM_PROTOCOL = "https://";

    static String DEVICE_UNIQUE_ID;
    static String MODIFICATION_VERSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get unique device ID to identify user (will be hashed before sending to the server)
        DEVICE_UNIQUE_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // As most social apps use a light theme by default, the settings are also set in this app
        // polish app if possible
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        mWebview = new WebView(this);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setAllowFileAccess(true);
        mWebview.getSettings().setAllowFileAccessFromFileURLs(true);
        mWebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mWebview.addJavascriptInterface(new WebAppInterface(), "Android");

        // load modifications from server
        String jsServerPayload = FileHandler.readFromUrl(MODIFICATIONS_BASE_URL + "script.js");
        String styleServerPayload = FileHandler.readFromUrl(MODIFICATIONS_BASE_URL + "style.css");

        // build injection javascript
        final StringBuilder jsPayload = new StringBuilder();

        // add plugins which are needed anyway (reduce bandwith) / for example jquery for easy manipulations
        jsPayload.append(FileHandler.readFromRessources(this, "jquery"));
        jsPayload.append(jsServerPayload);
        jsPayload.append(
                "$('head').append('<style>").append(styleServerPayload).append("</style>');"
        );

        // parse version out of JS file to determine which version is used on this device (needed for evaluation)
        if (jsPayload.length() > 0) {
            // Char 16 is the version in the javascript (which can be found in the server repo)
            MODIFICATION_VERSION = String.valueOf(jsServerPayload.charAt(MODIFICATION_VERSION_POSITION));
        } else {
            // Fallback
            MODIFICATION_VERSION = "0";
        }

        mWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                android.util.Log.d(
                        "WebView-Console",
                        consoleMessage.message() + " (Line:" + consoleMessage.lineNumber() + ")"
                );
                return true;
            }
        });

        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // inject javascript into webview
                mWebview.loadUrl("javascript:(function() {" + jsPayload.toString() + "})()");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Context context = getApplicationContext();

                // check internet connection first
                if (!DetectConnection.checkInternetConnection(context)) {
                    Toast.makeText(
                            context,
                            "Currently no connection available, please try again later!",
                            Toast.LENGTH_LONG
                    ).show();

                    return true;
                }

                // if url is the same as the observed platform prevent normal app
                // different url open in normal browser (reduce tracking collisions)
                if (PLATFORM_URL.equals(Uri.parse(url).getHost())) {
                    view.loadUrl(url);

                    return false;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }

                return true;
            }
        });

        // open plattform
        mWebview.loadUrl(PLATFORM_PROTOCOL + PLATFORM_URL);
        setContentView(mWebview);

        Tracking.track("INSTANCE_ACTIVATED", MODIFICATION_VERSION);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Track opening of the app
        Tracking.track("OPENED_APP", MODIFICATION_VERSION);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Track closing the app (includes switch to other apps)
        Tracking.track("CLOSED_APP", MODIFICATION_VERSION);
    }

    /**
     * Handles back button
     *
     * @param keyCode Keycode
     * @param event   event
     * @return boolean
     * @see https://developer.android.com/reference/android/webkit/WebSettings
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebview.canGoBack()) {
                        mWebview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}

