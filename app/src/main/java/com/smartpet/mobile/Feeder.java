package com.smartpet.mobile;

import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Failed extends Exception {
    public Failed(String message) {
        super(message);
    }
}

public class Feeder {
    String ip;
    String uri;
    public Feeder(String ip) throws Failed {
        try {
            HttpURLConnection conn = null;
            URL url = new URL("http://" + ip + ":8000");
            this.uri = "http://" + ip + ":8000";
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                this.ip = ip;
            } else {
                throw (new Failed("Connect failed. "));
            }
        } catch (IOException e) {
            e.printStackTrace();
//            throw (new IOException());
            throw new Failed("Connect failed. ");
        }
    }

    public void addFeedingPlan(int hour, int minute, int weight) throws Failed {
        try {
            StringBuffer sbf = new StringBuffer();
            String strRead = null;
            HttpURLConnection conn = null;
            URL url = new URL(uri + "/api/plan");

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("{\"time_h\":");
            writer.write(String.valueOf(hour));
            writer.write(",");
            writer.write("\"time_m\":");
            writer.write(String.valueOf(minute));
            writer.write(",");
            writer.write("\"weight\":");
            writer.write(String.valueOf(weight));
            writer.write("}");
            writer.flush();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            conn.disconnect();
            String results = sbf.toString();
            Log.println(Log.WARN, "FEed", results);
            if (!results.equals("{\"result\":0}")) {
                throw new Failed("Server returned failed result.");
            } else {
                Log.println(Log.WARN, "Feeder", "Add feeding plan request sent.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Failed("");
        }
    }

    public void feed(int weight) throws Failed {
        try {
            StringBuffer sbf = new StringBuffer();
            String strRead = null;
            HttpURLConnection conn = null;
            URL url = new URL(uri + "/api/food");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("{\"weight\": " + String.valueOf(weight) + "}");
            writer.flush();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            conn.disconnect();
            String results = sbf.toString();
            Log.println(Log.WARN, "FEed", results);
            if (!results.equals("{\"result\":0}")) {
                throw new Failed("Server returned failed result.");
            } else {
                Log.println(Log.WARN, "Feeder", "Feeding request sent.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Failed("Internal error.");
        }
    }
    public void delFeedingPlan(int hour, int minute) throws Failed {
        try {
            StringBuffer sbf = new StringBuffer();
            String strRead = null;
            HttpURLConnection conn = null;
            URL url = new URL(uri + "/api/plan");

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("{\"time_h\":");
            writer.write(String.valueOf(hour));
            writer.write(",");
            writer.write("\"time_m\":");
            writer.write(String.valueOf(minute));
            writer.write("}");
            writer.flush();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            conn.disconnect();
            String results = sbf.toString();
            Log.println(Log.WARN, "FEed", results);
            if (!results.equals("{\"result\":0}")) {
                throw new Failed("Server returned failed result.");
            } else {
                Log.println(Log.WARN, "Feeder", "Add feeding plan request sent.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new Failed("");
        }
    }
    public int getFeededToday() {
        try {
            StringBuffer sbf = new StringBuffer();
            String strRead = null;
            HttpURLConnection conn = null;
            System.out.println(uri + "/api/food");
            URL url = new URL(uri + "/api/food");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");

            conn.connect();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8);
            writer.write("{\"range\": \"today\"}");
            writer.flush();
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
            conn.disconnect();
            String results = sbf.toString();
            Log.println(Log.WARN, "FEed", results);
            Log.println(Log.WARN, "Feeder", "Feeding request sent.");
            int result;
            result = Integer.parseInt((results.split(",")[1]).split(":")[1].replaceAll("\\}", ""));
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}