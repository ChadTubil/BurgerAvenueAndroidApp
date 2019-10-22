package com.example.chadd.burgeravenueV2;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;
import static com.example.chadd.burgeravenueV2.DBCon.queueNumber;

public class RewardNCashPayment extends AppCompatActivity {
    public String tempCode = "";
    ImageView imageView;
    Button button;
    public final  static int QRcodeWidth = 500;
    Bitmap bitmap;
    public TextView mQueueNumber;
    public static String y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_ncash_payment);
        imageView = findViewById(R.id.imageView);

        mQueueNumber = findViewById(R.id.tvQueueingNumber);
        y = db_UserId;

        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i=0; i<7; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }

        tempCode = randomStringBuilder.toString();
        ComputeAmount computeAmount = new ComputeAmount();
        computeAmount.execute();

        ShowQueueingNumber showQueueingNumber = new ShowQueueingNumber();
        showQueueingNumber.execute();


    }
    Bitmap TextToImageEncode (String Value) throws WriterException {
        BitMatrix bitMatrix;
        try{
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );
        }catch (IllegalArgumentException e){
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int [] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for(int y = 0; y < bitMatrixHeight; y++){
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++){

                pixels[offset + x] = bitMatrix.get(x, y) ? getResources().getColor(R.color.QRCodeBlackColor):
                        getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public class ShowQueueingNumber extends AsyncTask<String, String, String> {
        String x="", QueueNumber = "NULL";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
//            mQueueNumber.setText(QueueNumber);

            try {
                mQueueNumber.setText(queueNumber);
//                if (QueueNumber.equalsIgnoreCase("")) {
//                    mQueueNumber.setText("0");
//                } else {
//                    mQueueNumber.setText(QueueNumber);
//                }
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
                    query = "SELECT MAX(order_id) FROM order_tbl WHERE order_cust_id = '" + y + "' AND order_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        QueueNumber = rs.getString(1);
                    }

//                    String queryA = "UPDATE cashnreward_tbl SET cashnreward_ordercode_id='" + tempCode + "' WHERE cashnreward_ordercode_id=''";
//                    Statement stmtA = con.createStatement();
//                    stmtA.executeUpdate(queryA);
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
                x = "Error";
            }
            return query;
        }
    }

    public class ComputeAmount extends AsyncTask<String, String, String> {
        String x="", totalAmount="NULLL";
        String SUM = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (totalAmount.equalsIgnoreCase("")) {
                    totalAmount = String.valueOf(0);
                    try{
                        bitmap = TextToImageEncode(queueNumber);

                        imageView.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                } else {
                    try{
                        bitmap = TextToImageEncode(queueNumber);

                        imageView.setImageBitmap(bitmap);
                    }catch (WriterException e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {

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
                    query = "SELECT order_code FROM order_tbl WHERE order_cust_id = '" + y + "' AND order_isdel = 0";
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
}
