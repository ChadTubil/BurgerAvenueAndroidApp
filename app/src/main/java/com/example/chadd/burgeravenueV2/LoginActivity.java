package com.example.chadd.burgeravenueV2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    public static String UserId = "";
    public static String IdUser = "";

    public EditText etusername;
    public EditText etpassword;
    public CardView login;
    public CardView login2;

    //show account
    private static TextView create;
    //forget pass
    Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ShowCreateAccount();
        myDialog = new Dialog(this);
        myDialog.setCanceledOnTouchOutside(false);
        etusername = findViewById(R.id.etusername);
        etpassword = findViewById(R.id.etpassword);
        login = findViewById(R.id.cv);
        login2 = findViewById(R.id.cv1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int error = 0;
//                if (etusername.getText().toString().isEmpty()){
//                    etusername.setError("Please enter a valid username");
//                    error = 1;
//                }
//                if (etpassword.getText().toString().isEmpty() || etpassword.getText().toString().length() < 8){
//                    etpassword.setError("Please enter a valid user");
//                }
                CheckLogin checkLogin = new CheckLogin();
                checkLogin.execute();
            }
        });

        login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("If you use this, you can not able to access the reward points system.");
                builder.setCancelable(true);

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GuestLogin guestLogin = new GuestLogin();
                        guestLogin.execute();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }
    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String...params){
            String username = etusername.getText().toString();
            String password = etpassword.getText().toString();

            if(username.trim().equals("")|| password.trim().equals("")){
                z="Please enter UserName and Password";
            }else{
                try{

                    Connection con = null;
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection(DBCon.DB_URL,DBCon.DB_USER,DBCon.DB_PASSWORD);
                    if(con == null){
                        z = "Check your internet connection";
                    }else{
                        String pass = convertPassMd5(password);
                        String query = "SELECT * FROM user_tbl WHERE user_name='" + username + "' AND user_password='" + pass + "' AND user_isactive = 0";
                        Statement stmt = con.createStatement();
                        ResultSet resultSet = stmt.executeQuery(query);
                        if(resultSet.next()){
                            z = "Log in successful";
                            UserId = resultSet.getString(1);
                            DBCon.db_UserId = resultSet.getString(1);
                            isSuccess = true;
                        }else{
                            z = "Invalid credentials";
                            isSuccess = false;
                        }
                        String addusertotallogin = "UPDATE user_tbl SET user_total_login = user_total_login + 1 WHERE user_id = '"+ DBCon.db_UserId + "'";
                        Statement statement = con.createStatement();
                        statement.executeUpdate(addusertotallogin);
                    }
                }
                catch (Exception e){
                    z = e.getMessage();
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(LoginActivity.this, String.valueOf(x[0]), Toast.LENGTH_SHORT).show();
            if(s.equalsIgnoreCase("Log in successful")){
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(IdUser, UserId);
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
    //show create account
    public void ShowCreateAccount(){
        create = findViewById(R.id.tvcreate);

        create.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View view){
                        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
    //forgot password
    public void ShowPopup(View v){
        TextView txtclose;

        myDialog.setContentView(R.layout.popup);
        txtclose = myDialog.findViewById(R.id.tvx);
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public class GuestLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected void onPostExecute(String s) {
            if(s == "Log in successful"){
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra(IdUser, UserId);
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String...params){

            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL,DBCon.DB_USER,DBCon.DB_PASSWORD);
                if(con == null){
                    z = "Check your internet connection";
                }else{
                    String query = "SELECT * FROM user_tbl WHERE user_id = '1'";
                    Statement stmt = con.createStatement();
                    ResultSet resultSet = stmt.executeQuery(query);
                    if(resultSet.next()){
                        z = "Log in successful";
                        UserId = resultSet.getString(1);
                        DBCon.db_UserId = resultSet.getString(1);
                        isSuccess = true;
                        con.close();
                    }else{
                        z = "Invalid credentials";
                        isSuccess = false;
                    }
                }
            }
            catch (Exception e){
                z = e.getMessage();

            }
            return z;
        }

    }
    public static String convertPassMd5(String pass){
        String password = null;
        MessageDigest mdEnc;
        try{
            mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(pass.getBytes(), 0, pass.length());
            pass = new BigInteger(1,mdEnc.digest()).toString(16);
            while (pass.length() < 32){
                pass = "0" + pass;
            }
            password = pass;
        }catch (NoSuchAlgorithmException e1){
            e1.printStackTrace();
        }
        return password;
    }
}
