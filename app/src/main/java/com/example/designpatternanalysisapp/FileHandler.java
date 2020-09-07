package com.example.designpatternanalysisapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class FileHandler {

    /**
     * Read file from server
     *
     * @param resourceUrl URL to pull
     *
     * @return String
     */
    static String readFromUrl(String resourceUrl) {
        String content = "";

        try {
            URL url = new URL(resourceUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60000);
            InputStream in = conn.getInputStream();
            content = convertStreamToString(in);
            conn.disconnect();
        } catch (Exception e) {
            Log.d("LoadPayload", e.toString());
        }

        return content;
    }

    /**
     * Reads file from ressources
     *
     * @param context  App activity context
     * @param filename Filename
     *
     * @return String
     */
    static String readFromRessources(Context context, String filename) {
        String content = "";

        try {
            InputStream is = context.getResources().openRawResource(context.getResources().getIdentifier(filename, "raw", context.getPackageName()));
            content = convertStreamToString(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return content;
    }

    /**
     * Convert inputStream to string
     *
     * @param is Inputstream
     * @return String
     */
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
