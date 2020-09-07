package com.example.designpatternanalysisapp;

class Tracking {
    /**
     * Formats tracking and sends post request
     * DO NOT CHANGE if the change wasn't done on the server beforehand
     *
     * @param trackingKey Tracking-Key
     * @param version     Current active modification version
     */
    static void track(String trackingKey, String version) {
        String[][] data = {
                {"action", trackingKey},
                {"userId", Hash.md5(MainActivity.DEVICE_UNIQUE_ID)},
                {"version", version},
        };

        RestClient.post(data);
    }
}
