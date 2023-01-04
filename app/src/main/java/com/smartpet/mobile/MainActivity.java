package com.smartpet.mobile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.smartpet.mobile.databinding.ActivityMainBinding;
import com.smartpet.mobile.ui.home.HomeFragment;
import com.smartpet.mobile.Feeder;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public Feeder feeder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
//        try {
//            feeder = new Feeder("192.168.1.6:8000");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    public void feed(View view) {
        int weight;
        Object test;
        EditText input;
        input = (EditText) findViewById(R.id.inputFeedWeight);
        String weight_string = input.getText().toString();
        try {
            weight = Integer.parseInt(weight_string);
            Log.println(Log.WARN, "FEED", String.valueOf(weight));
            feeder.feed(weight);
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("Completed");
            normalDialog.setMessage("Successful sent feeding request to feeder. ");
            normalDialog.show();
        } catch (NumberFormatException | Failed | NullPointerException e) {
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("Error");
            normalDialog.setMessage("Something went wrong... ");
            normalDialog.show();
            e.printStackTrace();
        }
    }
    public void addPlan(View view) {
        int hour, minute, weight;
        EditText input_time, input_weight;
        input_time = (EditText) findViewById(R.id.setTime);
        String time_string = input_time.getText().toString();
        try {
            hour = Integer.parseInt(time_string.split(":")[0]);
            minute = Integer.parseInt(time_string.split(":")[1]);
        } catch (Exception ignored){
            return;
        }
        input_weight = (EditText) findViewById(R.id.planWeight);
        String weight_string = input_weight.getText().toString();
//        weight = Integer.parseInt(weight_string);
        try {
            weight = Integer.parseInt(weight_string);
            feeder.addFeedingPlan(hour, minute, weight);
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("Completed");
            normalDialog.setMessage("Successful sent add plan request to feeder. ");
            normalDialog.show();

        } catch (Failed | NumberFormatException | NullPointerException e){
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("Error");
            normalDialog.setMessage("Something went wrong... ");
            normalDialog.show();
        }
    }
    @SuppressLint("SetTextI18n")
    public void connect(View view) {
        String ip;
        EditText ip_edit;
        TextView status;
        ip_edit = (EditText) findViewById(R.id.ip);
        status = (TextView) findViewById(R.id.status);
        ip = ip_edit.getText().toString();
        try {
            this.feeder = new Feeder(ip);
            status.setText("Connected!");
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("Completed");
            normalDialog.setMessage("Successful connected. ");
            normalDialog.show();
        } catch (Failed ignored) {}
    }
    public void delPlan(View view) {
        int hour, minute;
        EditText input_time;
        input_time = (EditText) findViewById(R.id.setTime);
        String time_string = input_time.getText().toString();
        try {
            hour = Integer.parseInt(time_string.split(":")[0]);
            minute = Integer.parseInt(time_string.split(":")[1]);
        } catch (Exception ignored){
            return;
        }
//        input_weight = (EditText) findViewById(R.id.planWeight);
//        String weight_string = input_weight.getText().toString();
//        weight = Integer.parseInt(weight_string);
        try {
//            weight = Integer.parseInt(weight_string);
            feeder.delFeedingPlan(hour, minute);
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("Completed");
            normalDialog.setMessage("Successful sent a delete plan request to feeder. ");
            normalDialog.show();

        } catch (Failed | NumberFormatException | NullPointerException e){
            final AlertDialog.Builder normalDialog = new AlertDialog.Builder(MainActivity.this);
            normalDialog.setTitle("Error");
            normalDialog.setMessage("Something went wrong... ");
            normalDialog.show();
        }
    }
    public void flushTotal(View view) {
        int total = feeder.getFeededToday();
        TextView totalFeeded = (TextView) findViewById(R.id.todayTotalFeed);
        totalFeeded.setText(String.valueOf(total));
    }
}