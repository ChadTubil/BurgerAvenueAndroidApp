package com.example.chadd.burgeravenueV2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "MAC";
    public static String user_id = "";
    public static String UserId = "";
    public static String z = "";
    String macAdd = "";

    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable()    {
            @Override
            public void run(){
                getMacAddr showMac = new getMacAddr();
                showMac.execute();

                destinationForm showDes = new destinationForm();
                showDes.execute();

//                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(i);
//                finish();
            }
        },SPLASH_TIME_OUT);
    }

    public class destinationForm extends AsyncTask<String, String, String> {
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if(z == "MainActivity"){
                Intent show = new Intent(SplashActivity.this, MainActivity.class);
                show.putExtra(UserId, user_id);
                startActivity(show);
            }
            else if (z == "LoginActivity"){
                Intent show = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(show);
            }
            else {
                Toast.makeText(SplashActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected String doInBackground(String... strings) {
            String queryDes = "";
            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if(con == null){
                    z = "NULL";
                }else{
                    queryDes = "SELECT * FROM user_tbl WHERE user_isactive = 1 AND user_macaddress = '"+macAdd+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(queryDes);
                    if (rs.next()){
                        z = "MainActivity";
                        user_id = rs.getString(1);
                        DBCon.db_UserId = rs.getString(1);
                        isSuccess = true;
                        String addusertotallogin = "UPDATE user_tbl SET user_total_login = user_total_login + 1 WHERE user_id = '"+ DBCon.db_UserId + "'";
                        Statement statement = con.createStatement();
                        statement.executeUpdate(addusertotallogin);
//                        con.close();
                    }else{
                        z = "LoginActivity";
                        isSuccess = false;
                    }
                }
            }catch (Exception e){
                z = e.getMessage();
            }
            return z;
        }
    }

    public class getMacAddr extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
//            Toast.makeText(SplashActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all){
                    if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                    byte [] macBytes = nif.getHardwareAddress();
                    if(macBytes == null){
                        macAdd = "";
                        return macAdd;
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes){
                        res1.append(Integer.toHexString( b & 0xFF) + ":");
                    }
                    if(res1.length() > 0){
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    macAdd = res1.toString();
                    DBCon.device_macAddr = macAdd;
                    return macAdd;
                }
            }catch (Exception ex){

            }
            return macAdd;
        }
    }
}
