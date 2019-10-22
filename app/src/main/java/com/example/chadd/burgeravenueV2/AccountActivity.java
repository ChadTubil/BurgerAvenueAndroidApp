package com.example.chadd.burgeravenueV2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;

public class AccountActivity extends AppCompatActivity {
    public static String y;
    public static String Fname, Lname, Uname, Email, Points;
    public TextView Fullname, username, email, points;
    //edit account
    private static ImageView editAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        EditAccount();

        y = DBCon.db_UserId;

        userInfo showInfo = new userInfo();
        showInfo.execute();

        Fullname = findViewById(R.id.tvFullname);
        username = findViewById(R.id.tvUsername);
        email = findViewById(R.id.tvemail);
        points = findViewById(R.id.tvRewardPoints);
    }
    //edit account
    public void EditAccount(){
        editAccount = findViewById(R.id.tveditAccount);

        editAccount.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View view){
                        Intent intent = new Intent(AccountActivity.this, UpdateAccountActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    public class userInfo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Fullname.setText(Fname+" "+Lname);
            username.setText(Uname);
            email.setText(Email);
            points.setText(Points);
//            Toast.makeText(AccountActivity.this, y, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT user_fname, user_lname, user_name, user_email, reward_points FROM user_tbl JOIN reward_tbl ON user_id = reward_user_id WHERE user_id = '"+y+"' AND user_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        y = rs.getString(1);
                        Fname = rs.getString(1);
                        Lname = rs.getString(2);
                        Uname = rs.getString(3);
                        Email = rs.getString(4);
                        Points = rs.getString(5);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
}
