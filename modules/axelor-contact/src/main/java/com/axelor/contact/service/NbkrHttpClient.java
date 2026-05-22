package com.axelor.contact.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NbkrHttpClient {

    private static final String NBKR_URL = "https://www.nbkr.kg/XML/daily.xml";
    private static final int TIMEOUT_MS   = 10_000;

    public InputStream fetchRatesStream() throws Exception {
        URL url = new URL(NBKR_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(TIMEOUT_MS);
        conn.setReadTimeout(TIMEOUT_MS);

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("НБКР вернул статус: " + status);
        }

        return conn.getInputStream();
    }
}