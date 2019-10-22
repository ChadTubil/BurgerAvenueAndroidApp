package com.example.chadd.burgeravenueV2;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;
import static com.example.chadd.burgeravenueV2.DBCon.queueNumber;

public class RewardNCash extends AppCompatActivity {
    public static String y;
    public static Double remainingPoints = 0.0;
    public static String RewardPoints = " ";
    public TextView mQueueNumber;
    public TextView TotalAmountToBePaid, Cash;
    public TextView rewardPoints;
    public Button Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_ncash);
        y = db_UserId;

        mQueueNumber = findViewById(R.id.tvqueuenumber);
        TotalAmountToBePaid = findViewById(R.id.tvTotalAmountToBePaid);
        Cash = findViewById(R.id.tvCashToBeAdded);
        rewardPoints = findViewById(R.id.tvRemainingRewardPoints);
        Confirm = findViewById(R.id.btnConfirm);

        ShowQueueingNumber showQueueingNumber = new ShowQueueingNumber();
        showQueueingNumber.execute();

        ShowTotalAmount showTotalAmount = new ShowTotalAmount();
        showTotalAmount.execute();

        ShowRemainingRewardPoints showRemainingRewardPoints = new ShowRemainingRewardPoints();
        showRemainingRewardPoints.execute();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertRewardNCash insertRewardNCash = new InsertRewardNCash();
                insertRewardNCash.execute();


            }
        });
    }

    public class ShowQueueingNumber extends AsyncTask<String, String, String> {
        String x="", QueueNumber = "NULL";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
//                mQueueNumber.setText(queueNumber);
                if (QueueNumber.equalsIgnoreCase("NULL")) {
                    mQueueNumber.setText("0");
                } else {
                    mQueueNumber.setText(QueueNumber);
                    queueNumber = QueueNumber;
                }
            } catch (Exception e) {
                mQueueNumber.setText("0");
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String query = null;
            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {
                    x = "ConnectionError";
                } else {
                    query = "SELECT MAX(order_id) FROM order_tbl WHERE order_cust_id = '"+ y +"' AND order_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        QueueNumber = rs.getString(1);
                    }
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
                x = "Error";
            }
            return query;
        }
    }

    public class ShowTotalAmount extends AsyncTask<String, String, String>{
        String x="", totalAmount="NULLL";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
//            mTOTALAMOUNT.setText(totalAmount);
            try {
                if (totalAmount.equalsIgnoreCase("")) {
                    TotalAmountToBePaid.setText("0");
                } else {
                    TotalAmountToBePaid.setText(totalAmount);
                }
            } catch (Exception e) {
                TotalAmountToBePaid.setText("0");
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            String query = null;
            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {
                    x = "ConnectionError";
                } else {
                    query = "SELECT SUM(order_amount)FROM order_tbl WHERE order_cust_id = '" + y + "' AND order_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        totalAmount = rs.getString(1);

                    }
                }

                con.close();
            }catch (Exception e){
                e.getMessage();
                x = "Error";
            }
            return query;
        }
    }

    public class ShowRemainingRewardPoints extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            rewardPoints.setText(RewardPoints);
            remainingPoints = Double.parseDouble(TotalAmountToBePaid.getText().toString()) - Double.parseDouble(rewardPoints.getText().toString());
            Cash.setText(String.valueOf(remainingPoints));
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT * FROM reward_tbl WHERE reward_isdel = 0 AND reward_user_id = '" + y + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        db_UserId = rs.getString(1);
                        RewardPoints = rs.getString(3);
                    }
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }

    public class InsertRewardNCash extends AsyncTask<String, String, String>{
        String msg = "";
        String msgQueue = mQueueNumber.getText().toString();
        String msgAmount = TotalAmountToBePaid.getText().toString();
        String msgRemainingReward = rewardPoints.getText().toString();
        String msgCash = Cash.getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(RewardNCash.this, "Please pay in the cashier to complete you transaction", Toast.LENGTH_LONG).show();
//            Intent i = new Intent(RewardNCash.this, MainActivity.class);
//            startActivity(i);

            Intent intent = new Intent(RewardNCash.this, RewardNCashPayment.class);
            startActivity(intent);
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
                    String query = "INSERT INTO cashnreward_tbl (cashnreward_user_id, cashnreward_queue, cashnreward_amount, " +
                            "cashnreward_rewards, cashnreward_cash, cashnreward_isdel) VALUES " +
                            "('" + y + "', '" + msgQueue + "', '" + msgAmount + "', '" + msgRemainingReward + "', '" + msgCash + "', '0')";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);


                    msg = "Successful";
                }
                con.close();
            }catch (Exception e){
                msg = e.getMessage();
            }
            return msg;
        }
    }
}
