package com.example.chadd.burgeravenueV2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;

public class MainActivity extends AppCompatActivity {
    public static CardView showAcc;
    public static CardView showLog;
    public static CardView showMenu;
    public static CardView showCreate;

    public static String y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateStatus update = new updateStatus();
        update.execute();
        ShowMenu();
        ShowAccount();
        ShowCreate();

        y = db_UserId;


        showLog = findViewById(R.id.cvlogout);
        showLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Do you want to Log out?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateLogout logout = new updateLogout();
                                logout.execute();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert1 = builder1.create();
                alert1.show();
            }
        });
    }
    public class updateLogout extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(MainActivity.this, DBCon.db_UserId, Toast.LENGTH_SHORT).show();
            Intent goToLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLogin);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "UPDATE user_tbl SET user_isactive=0,user_macaddress='"+ DBCon.device_macAddr+"' WHERE user_id = '"+db_UserId+"'";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public class updateStatus extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String msg = "";
            Boolean isSuccess = false;

            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL,DBCon.DB_USER,DBCon.DB_PASSWORD);
                if(con == null){
                    msg = "Check your internet connection";
                }else{
                    String query = "UPDATE user_tbl SET user_isactive = 1, user_macaddress='"+DBCon.device_macAddr+"'" + "WHERE user_id = '" + db_UserId + "'";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    msg = "Updated Successfully!";
                }
                con.close();
            }
            catch (Exception e){
                msg = e.getMessage();
            }
            return msg;
        }
    }

    public void ShowAccount(){
        showAcc = findViewById(R.id.cvAccount);

        showAcc.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                InsertPoints insertPoints = new InsertPoints();
                insertPoints.execute();

                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
    public void ShowMenu(){
        showMenu = findViewById(R.id.cvMenu);

        showMenu.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View view){
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                    }
                }
        );

    }
    public void ShowCreate(){
        showCreate = findViewById(R.id.cvCreate);

        showCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

    }
    public class InsertPoints extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "INSERT INTO reward_tbl (reward_points, reward_user_id) VALUES (5.00, '"+y+"'')";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
}
