package com.example.chadd.burgeravenueV2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import java.util.regex.Pattern;

public class UpdateAccountActivity extends AppCompatActivity {
    public static String y;
    public static String Uname, Email, Pass, CPass;
    public EditText UN, EMAIL, PASS, CPASS;
    public Button Update;
    public Button Cancel;
    private static TextView closeCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        y = DBCon.db_UserId;
        updateInfo infoUpdate = new updateInfo();
        infoUpdate.execute();

        UN = findViewById(R.id.etun);
        EMAIL = findViewById(R.id.etemail);
        PASS = findViewById(R.id.etpass);
        CPASS = findViewById(R.id.etcpass);
        Update = findViewById(R.id.btnSignup);
        Cancel = findViewById(R.id.btnCancel);

        //        update account
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int error = 0;

                if(UN.getText().toString().isEmpty() || UN.getText().toString().length() < 4) {
                    UN.setError("Please enter username.");
                    UN.setError("Minimum of 4 characters.");
                    error = 1;
                }
                if(EMAIL.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(EMAIL.getText().toString()).matches()) {
                    EMAIL.setError("Please enter a valid email address.");
                    error = 1;
                }
                if(PASS.getText().toString().isEmpty() || PASS.getText().toString().length() < 8) {
                    PASS.setError("Please enter a valid password.");
                    PASS.setError("Minimum of 8 characters");
                    error = 1;
                }
                if(CPASS.getText().toString().isEmpty() || CPASS.getText().toString().length() < 8) {
                    CPASS.setError("Please confirm your password.");
                    error = 1;
                }
                if(!(PASS.getText().toString().matches(CPASS.getText().toString()))) {// != etcpass.getText().toString().matches(""))){
                    CPASS.setError("Not match!");
                    error = 1;
                }

                if(error == 0){
                    updateAccount accountUpdate = new updateAccount();
                    accountUpdate.execute();
                }

            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        CloseCart();
    }
    public class updateInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

            UN.setText(Uname);
            EMAIL.setText(Email);
            PASS.setText(Pass);
            CPASS.setText(CPass);
//            Toast.makeText(AccountActivity.this, DBCon.db_UserId, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT * FROM user_tbl WHERE user_id = '"+y+"'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        DBCon.db_UserId = rs.getString(1);
                        Uname = rs.getString(4);
                        Email = rs.getString(5);
                        Pass = rs.getString(6);
                        CPass = rs.getString(7);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }

    public class updateAccount extends AsyncTask<String, String, String>{
        String txtUserName = UN.getText().toString();
        String txtEmail = EMAIL.getText().toString();
        String txtPass = PASS.getText().toString();
        String txtCPass = CPASS.getText().toString();

        @Override
        protected void onPreExecute() {
            Toast.makeText(UpdateAccountActivity.this, "Updating your account...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(UpdateAccountActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String pass = convertPassMd5(txtPass);
                    String cpass = convertPassMd5(txtCPass);
                    String query = "UPDATE user_tbl SET user_name='" + txtUserName + "',user_email='"+ txtEmail+"',user_password='"+ pass + "',user_cpassword='"+ cpass +"' WHERE user_id = '"+y+"'";
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
    //close cart
    public void CloseCart(){
        closeCart = findViewById(R.id.tvx);

        closeCart.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View view){
                        Intent intent = new Intent(UpdateAccountActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }
                }
        );
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
