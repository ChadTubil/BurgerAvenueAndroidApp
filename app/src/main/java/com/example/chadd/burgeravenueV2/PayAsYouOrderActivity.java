package com.example.chadd.burgeravenueV2;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class PayAsYouOrderActivity extends AppCompatActivity {
    public static String y;
    public static double remainingPoints;
    public static String RewardPoints;
    public static String totalAmount;

    public TextView rewardPoints, change;
    public TextView mTOTALAMOUNT;
    public TextView mQueueNumber;


    public Button Confirm;

    public double pointsEarned = 0;
    public double doubleTotalAmount = 0.0;
    public String strTotalAmount = "";
    public int points = 0;
    public String strPointEarnd = "";

    public double totalAddedPoints;

    public double cash = 0.0;
    public String CASH = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_as_you_order);
        y = db_UserId;
        ClearTable clearTable = new ClearTable();
        clearTable.execute();

        ShowRewardPayment showRewardPayment = new ShowRewardPayment();
        showRewardPayment.execute();

        ShowTotalAmount showTotalAmount = new ShowTotalAmount();
        showTotalAmount.execute();

        ShowQueueingNumber showQueueingNumber = new ShowQueueingNumber();
        showQueueingNumber.execute();

        InsertQueuing insertQueuing = new InsertQueuing();
        insertQueuing.execute();

        rewardPoints = findViewById(R.id.tvTotalRewardPoints);
        mTOTALAMOUNT = findViewById(R.id.tvTotalAmount);
        change = findViewById(R.id.tvChange);
        mQueueNumber = findViewById(R.id.tvqueuenumber);

        Confirm = findViewById(R.id.btnConfirm);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PayAsYouOrderActivity.this);
                builder1.setMessage("Are you sure with this transaction?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Double.parseDouble(change.getText().toString()) < Double.parseDouble(mTOTALAMOUNT.getText().toString()) ){
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(PayAsYouOrderActivity.this);
                                    builder2.setMessage("Your reward points are not enough. Do you want to use cash to pay your order?");
                                    builder2.setCancelable(true);
                                    builder2.setNegativeButton("NO",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            });
                                    builder2.setPositiveButton("YES",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    InsertToCashNReward insertToCashNReward = new InsertToCashNReward();
                                                    insertToCashNReward.execute();

                                                    Intent i = new Intent(PayAsYouOrderActivity.this, RewardNCash.class);
                                                    startActivity(i);
                                                }
                                            });
                                    AlertDialog alert2 = builder2.create();
                                    alert2.show();
                                }else{
                                    UpdateCartList updateCartList = new UpdateCartList();
                                    updateCartList.execute();

                                    InsertToPaymentRewards insertToPaymentRewards = new InsertToPaymentRewards();
                                    insertToPaymentRewards.execute();

                                    UpdateRewards updateRewards = new UpdateRewards();
                                    updateRewards.execute();

                                    AddRewards addRewards = new AddRewards();
                                    addRewards.execute();

                                }
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
    public class ShowRewardPayment extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            rewardPoints.setText(RewardPoints);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT * FROM reward_tbl WHERE reward_isdel = 0 AND reward_user_id = '" + db_UserId + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        db_UserId = rs.getString(2);
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

    public class ShowTotalAmount extends AsyncTask<String, String, String>{
        String x="";//, totalAmount="NULLL";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
//            Toast.makeText(PayAsYouOrderActivity.this, s, Toast.LENGTH_SHORT).show();
//            mTOTALAMOUNT.setText(totalAmount);
            try {
                if (totalAmount.equalsIgnoreCase("NULL")) {
                    mTOTALAMOUNT.setText("0");
                } else {
                    mTOTALAMOUNT.setText(totalAmount);
                }
            } catch (Exception e) {
                mTOTALAMOUNT.setText("0");
            }

            //Toast.makeText(PayAsYouOrderActivity.this, mTOTALAMOUNT.getText().toString(), Toast.LENGTH_SHORT).show();

            remainingPoints = Double.parseDouble(rewardPoints.getText().toString()) - Double.parseDouble(mTOTALAMOUNT.getText().toString());
            if (remainingPoints <= 0)
                change.setText("0");
            else
                change.setText(String.valueOf(((int) remainingPoints)));
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
                    query = "SELECT SUM(order_amount) FROM order_tbl WHERE order_cust_id = '" + db_UserId + "' AND order_isdel=0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
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

    public class InsertToPaymentRewards extends AsyncTask<String, String, String>{
        String msg = "";
        String query = "";
        String msgTotalPoints = rewardPoints.getText().toString();
        String msgTotalOrdered = mTOTALAMOUNT.getText().toString();
        String msgChange = change.getText().toString();
        @Override
        protected void onPreExecute() {
            //Toast.makeText(PayAsYouOrderActivity.this, "Processing...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {
                    msg = "Connection goes wrong";
                } else {
                    query = "INSERT INTO payment_rewards_tbl (payment_rewards_user_id, payment_rewards_reward_id, payment_rewards_totalpoints, payment_rewards_totalamount_ordered, payment_rewards_change, payment_rewards_isdel) VALUES('"+ y +"','"+ y +"', '"+ msgTotalPoints +"','"+ msgTotalOrdered +"','"+ msgChange +"', 0)";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return query;
        }
    }
    public class UpdateRewards extends AsyncTask<String, String, String>{
        String txtChange = change.getText().toString();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(PayAsYouOrderActivity.this, MainActivity.class);
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
                    String query = "UPDATE reward_tbl SET reward_points='" + txtChange + "',reward_isdel= '0', reward_payment_rewards_id='"+ db_UserId +"' WHERE reward_user_id = '"+y+"'";
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
    public class UpdateCartList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "UPDATE order_tbl SET order_isdel = 1 WHERE order_cust_id = '" + y + "'";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                }
                con.close();
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowQueueingNumber extends AsyncTask<String, String, String>{
        String x="", QueueNumber = "NULL";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (QueueNumber.equalsIgnoreCase("NULL")) {
                    mQueueNumber.setText("0");
                } else {
                    mQueueNumber.setText(QueueNumber);
                    queueNumber = QueueNumber;
                }
            } catch (Exception e) {
                mQueueNumber.setText("0");
            }

//            Toast.makeText(PayAsYouOrderActivity.this, queueNumber, Toast.LENGTH_SHORT).show();
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
                    query = "SELECT MAX(order_id) FROM order_tbl WHERE order_cust_id = '"+y+"' AND order_isdel = 0";
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
    public class ClearTable extends AsyncTask <String, String, String>{
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
            String query = null;
            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    query = "DELETE FROM order_tbl WHERE order_date != CURRENT_DATE";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return query;
        }
    }

    public class AddRewards extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(PayAsYouOrderActivity.this, "You earned " + s + " reward point(s).", Toast.LENGTH_LONG).show();

            InsertAddRewardPoints insertAddRewardPoints = new InsertAddRewardPoints();
            insertAddRewardPoints.execute();

            totalAddedPoints = points + Double.parseDouble(change.getText().toString());

            ComputeNInsertAddedReward computeNInsertAddedReward = new ComputeNInsertAddedReward();
            computeNInsertAddedReward.execute();

            UpdatedRewardPoints updatedRewardPoints = new UpdatedRewardPoints();
            updatedRewardPoints.execute();

        }

        @Override
        protected String doInBackground(String... strings) {
//            Toast.makeText(PayAsYouOrderActivity.this, "Wew", Toast.LENGTH_LONG).show();


            strTotalAmount = mTOTALAMOUNT.getText().toString();
            doubleTotalAmount = Double.parseDouble(strTotalAmount);
            pointsEarned = doubleTotalAmount / 50.0;
            strPointEarnd = String.valueOf(pointsEarned);
//            points = Integer.parseInt(String.valueOf(pointsEarned));

            points = (int) Math.round(pointsEarned);//Integer.parseInt(strPointEarnd);

            return String.valueOf(points);
        }
    }

    public class InsertAddRewardPoints extends AsyncTask<String, String, String>{
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
                    String query = "INSERT INTO addedrewardpoints_tbl (addedrewardpoints_user_id, addedrewardpoints_reward_id, addedrewardpoints_addedpoints, addedrewardpoints_remainingpoints, addedrewardpoints_isdel) VALUES ('"+ y + "', '"+ y + "'," +
                            "'"+ points +"', '"+ remainingPoints +"', '0')";
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

    public class ComputeNInsertAddedReward extends AsyncTask<String, String, String>{
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
                    String query = "UPDATE addedrewardpoints_tbl SET addedrewardpoints_totalpoints = '"+ totalAddedPoints +"' WHERE addedrewardpoints_user_id = '"+ y +"'";
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

    public class UpdatedRewardPoints extends AsyncTask<String, String, String>{

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
                    String query = "UPDATE reward_tbl SET reward_points = '"+ totalAddedPoints +"' WHERE reward_user_id = '"+ y +"'";
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
    public class InsertToCashNReward extends AsyncTask<String, String, String>{
        String asa = mQueueNumber.getText().toString();
        String ase = mTOTALAMOUNT.getText().toString();

        double a = 0.0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "INSERT INTO cashnreward_tbl (cashnreward_user_id, cashnreward_queue, cashnreward_amount, cashnreward_rewards, cashnreward_ordercode_id, cashnreward_isdel) " +
                            "VALUES ('"+ y + "', '"+ asa + "','"+ ase +"', '"+ remainingPoints +"', '"+ asa +"', '0')";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    a = Double.parseDouble(ase) - remainingPoints;

                    String qq = "UPDATE cashnreward_tbl SET cashnreward_cash = '"+ a + "' WHERE cashnreward_user_id = '" + y + "' AND cashnreward_user_id = ''";
                    Statement statement = con.createStatement();
                    statement.executeUpdate(qq);

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }

    public class InsertQueuing extends AsyncTask<String, String, String>{
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

                    String qq = "UPDATE order_tbl SET order_code = '"+ queueNumber + "' WHERE order_cust_id = '" + y + "' AND order_isdel = 0";
                    Statement statement = con.createStatement();
                    statement.executeUpdate(qq);

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
}
