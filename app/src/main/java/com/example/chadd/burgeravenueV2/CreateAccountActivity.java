package com.example.chadd.burgeravenueV2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.result.EmailAddressParsedResult;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;

public class CreateAccountActivity extends AppCompatActivity {
    public EditText etFname;
    public EditText etLname;
    public EditText etUname;
    public EditText etEmail;
    public EditText etpass;
    public EditText etcpass;
    public Button btnSignup;

    public String newUserID;

    private static TextView closeCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etFname = findViewById(R.id.etfn);
        etLname = findViewById(R.id.etln);
        etUname = findViewById(R.id.etun);
        etEmail = findViewById(R.id.etemail);
        etpass = findViewById(R.id.etpass);
        etcpass = findViewById(R.id.etcpass);
        btnSignup = findViewById(R.id.btnSignup);

        etFname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        etLname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int error = 0;

                if (etFname.getText().toString().isEmpty()) {
                    etFname.setError("Please enter first name.");
                    error = 1;
                }
                if (etLname.getText().toString().isEmpty()) {
                    etLname.setError("Please enter last name.");
                    error = 1;
                }
                if(etUname.getText().toString().isEmpty() || etUname.getText().toString().length() < 4) {
                    etUname.setError("Please enter a valid username.");
                    etUname.setError("Minimum of 4 characters");
                    error = 1;
                }
                if(etEmail.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                    etEmail.setError("Please enter a valid email address.");
                    error = 1;
                }
                if(etpass.getText().toString().isEmpty() || etpass.getText().toString().length() < 8) {
                    etpass.setError("Please enter a valid password.");
                    etpass.setError("Minimum of 8 characters");
                    error = 1;
                }
                if(etcpass.getText().toString().isEmpty() || etcpass.getText().toString().length() < 8) {
                    etcpass.setError("Please confirm your password.");
                    etcpass.setError("Minimum of 8 characters");
                    error = 1;
                }
                if(!(etpass.getText().toString().matches(etcpass.getText().toString()))) {
                    etcpass.setError("Not match!");
                    error = 1;
                }

                if(error == 0){
                    SignUp signUp = new SignUp();
                    signUp.execute();
                }

            }
        });
        CloseCart();
    }
    public class SignUp extends AsyncTask<String, String, String> {
        String msg = "";
        String msgFname = etFname.getText().toString();
        String msgLname = etLname.getText().toString();
        String msgUname = etUname.getText().toString();
        String msgEmail = etEmail.getText().toString();
        String msgPass = etpass.getText().toString();
        String msgCPass = etcpass.getText().toString();

        @Override
        protected void onPreExecute() {
//            Toast.makeText(CreateAccountActivity.this, "Creating your account...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {

            if(msg == "Successful"){
                etFname.getText().clear();
                etLname.getText().clear();
                etUname.getText().clear();
                etEmail.getText().clear();
                etpass.getText().clear();
                etcpass.getText().clear();

                InsertPoints insertPoints = new InsertPoints();
                insertPoints.execute();

                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);

            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {
                    msg = "Connection goes wrong";
                } else {

                    String pass = convertPassMd5(msgPass);
                    String cpass = convertPassMd5(msgCPass);
                    String query = "INSERT INTO user_tbl (user_fname, user_lname, user_name, user_email, user_password, user_cpassword, user_isdel, user_emp_id, user_last_login, user_isactive) VALUES " +
                            "('" + msgFname + "', '" + msgLname + "', '" + msgUname + "', '" + msgEmail + "', '" + pass + "', '" + cpass + "', 0, 0, CURRENT_TIMESTAMP, 0)";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    String lastInsertedID = "SELECT MAX(user_id) FROM user_tbl WHERE user_emp_id=0";
                    Statement stmtLastID = con.createStatement();
                    ResultSet rsLastID = stmtLastID.executeQuery(lastInsertedID);
                    if(rsLastID.next()) {
                        newUserID = rsLastID.getString(1);
                    }

                    msg = "Successful";
                }
                con.close();
            }catch (Exception e){
                msg = e.getMessage();
            }
            return msg;
        }
    }
    //close cart
    public void CloseCart(){
        closeCart = findViewById(R.id.tvx);

        closeCart.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View view){
                        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );
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
                    String query = "INSERT INTO reward_tbl (reward_points, reward_user_id) VALUES (5.00, " + newUserID + ")";
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
