package com.example.designpatternanalysisapp;

import android.util.Log;
import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

class RestClient {
    /**
     * Send data to tracking server
     *
     * @param data data formatted as key value in a two dimensional array
     * @throws Exception Errors from Rest call
     */
    static void post(String[][] data) {
        try {
            URL obj = new URL(MainActivity.TRACK_URL);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add specific user agent, specify method
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", MainActivity.USER_AGENT + ":" + Hash.md5(MainActivity.DEVICE_UNIQUE_ID));

            // prepare key value of parameters
            String urlParameters = prepareDataArray(data);

            // Send post request
            con.setDoOutput(true);
            DataOutputStream os = new DataOutputStream(con.getOutputStream());
            os.writeBytes(urlParameters);
            os.flush();
            os.close();

            con.getResponseCode();
        } catch (Exception e) {
            Log.d("RestCall", e.toString());
        }
    }

    /**
     * Format array of data to key/value pattern for server request
     * DO NOT CHANGE if the change wasn't done on the server beforehand
     *
     * @param data data formatted as array
     *
     * @return String
     */
    private static String prepareDataArray(String[][] data) {
        StringBuilder parameters = new StringBuilder();

        for (String[] paramValueCombination:data) {
            // key value combination
            if (paramValueCombination.length == 2) {
                parameters.append(paramValueCombination[0]);
                parameters.append("=");
                parameters.append(paramValueCombination[1]);
                parameters.append("&");
            }
        }

        return parameters.toString();
    }
}

