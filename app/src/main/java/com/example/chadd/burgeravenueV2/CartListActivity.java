package com.example.chadd.burgeravenueV2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.PriorityQueue;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;

public class CartListActivity extends AppCompatActivity {
    private static TextView closeCart;
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mQty = new ArrayList<>();
    private ArrayList<String> mPri = new ArrayList<>();
    private ArrayList<String> mOrdID = new ArrayList<>();


    public Button CvCheckOut;
    public TextView mTOTALAMOUNT;
    public TextView txtclose;


    public static Button btnplus, btnminus;
    public static CardView btnRemove, btnUpdate;
    public TextView tvNumber;
    public String CartPopUpQty;
    public TextView mCartProdQty;
    public TextView mCartProdName;

    public static String orderID = "";
    //Popup Payment
    Dialog myDialog;
    String y = db_UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);
        CvCheckOut = findViewById(R.id.cvCheckOut);
        mTOTALAMOUNT = findViewById(R.id.totalamount);
        //Popup Payment
        myDialog = new Dialog(this);
        myDialog.setCanceledOnTouchOutside(false);

        CloseCart();

        ShowCartList showCartList = new ShowCartList();
        showCartList.execute();

        ComputeTotalAmount computeTotalAmount = new ComputeTotalAmount();
        computeTotalAmount.execute();

    }
    public class ListAdapter extends ArrayAdapter<String> {
        private int resource;
        private LayoutInflater layoutInflater;
        private ArrayList<String> week = new ArrayList<>();


        public ListAdapter (Context context, int resource, ArrayList<String> objects){
            super (context, resource, objects);
            this. resource = resource;
            this. week = objects;

            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent){

            CartListActivity.ListAdapter.ViewHolder holder;
            if(convertView == null){
                holder = new CartListActivity.ListAdapter.ViewHolder();
                convertView = layoutInflater.inflate(resource, null);

                holder.mProduct = convertView.findViewById(R.id.tvproduct);
                holder.mAmount = convertView.findViewById(R.id.tvamount);
                holder.mQuantity = convertView.findViewById(R.id.tvQty);
                holder.mPriceList = convertView.findViewById(R.id.tvPrice);

                convertView.setTag(holder);

            }
            else{
                holder = (CartListActivity.ListAdapter.ViewHolder)convertView.getTag();
            }

            holder.mProduct.setText(mName.get(position));
            holder.mAmount.setText(mPrice.get(position));
            holder.mQuantity.setText(mQty.get(position));
            holder.mPriceList.setText(mPri.get(position));

            return convertView;
        }

        class ViewHolder{
            private TextView mProduct, mAmount, mQuantity, mPriceList;
        }
    }

    //close cart
    public void CloseCart(){
        closeCart = findViewById(R.id.tvx);

        closeCart.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View view){
                        Intent intent = new Intent(CartListActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
    //Popup Payment
    public void ShowPopup(View v){
        TextView txtclose;

        myDialog.setContentView(R.layout.payment_popup);
        txtclose = myDialog.findViewById(R.id.tvx);
        txtclose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public class ShowCartList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            final ListView listView = findViewById(R.id.listCart);
            final CartListActivity.ListAdapter listAdapter = new CartListActivity.ListAdapter(CartListActivity.this, R.layout.cartactivity, mName);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                    AlertDialog.Builder adb = new AlertDialog.Builder(CartListActivity.this);
//                    adb.setTitle("CANCEL ORDER");
//                    adb.setMessage("Are you sure you want to cancel?");
//                    final int positionToRemove = position;
//                    adb.setNegativeButton("Cancel", null);
//                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener(){
//                        public void onClick(DialogInterface dialog, int which){
//                            listAdapter.remove(listAdapter.getItem(position));
//                            listAdapter.notifyDataSetChanged();
//                            orderID = mOrdID.get(position);
//                            RemoveItem removeItem = new RemoveItem();
//                            removeItem.execute();
//
//                        }
//                    });
//                    adb.show();
//                    Toast.makeText(CartListActivity.this, orderID, Toast.LENGTH_SHORT).show();
                    myDialog.setContentView(R.layout.editqty);
                    tvNumber = myDialog.findViewById(R.id.cartProdQty);
                    mCartProdName = myDialog.findViewById(R.id.cartProdName);
                    final TextView tvNumber = myDialog.findViewById(R.id.cartProdQty);
                    orderID = mOrdID.get(position);
                    ShowEdit showEdit = new ShowEdit();
                    showEdit.execute();

                    txtclose = myDialog.findViewById(R.id.cartCloseBtn);
                    txtclose.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            myDialog.dismiss();
                        }
                    });

                    btnplus = myDialog.findViewById(R.id.cartProdPlus);
                    btnplus.setOnClickListener(new View.OnClickListener() {
                        int mynum;
                        String mynum2;
                        @Override
                        public void onClick(View v) {
                            try {
                                mynum2 = tvNumber.getText().toString();
                                mynum = Integer.parseInt(mynum2);
                                mynum = mynum + 1;
                                tvNumber.setText(Integer.toString(mynum));
                            }catch(Exception exp){
                                Toast.makeText(getBaseContext(), exp.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btnminus = myDialog.findViewById(R.id.cartProdMinus);
                    btnminus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String mynum2 = tvNumber.getText().toString();
                            int mynum = Integer.parseInt(mynum2);
                            try {
                                if (mynum > 1){
                                    mynum = mynum - 1;
                                    tvNumber.setText(Integer.toString(mynum));
                                }else {
                                    tvNumber.setText(Integer.toString(1));
                                }

                            }catch(Exception exp){
                                Toast.makeText(getBaseContext(), exp.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    btnRemove = myDialog.findViewById(R.id.cartRemove);
                    btnRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listAdapter.remove(listAdapter.getItem(position));
                            listAdapter.notifyDataSetChanged();
                            orderID = mOrdID.get(position);
                            RemoveItem removeItem = new RemoveItem();
                            removeItem.execute();

                            myDialog.dismiss();
                        }
                    });

                    btnUpdate = myDialog.findViewById(R.id.cartAddBtn);
                    btnUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InsertCartEdit ice = new InsertCartEdit();
                            ice.execute();

                            ComputeQtyNPrice computeQtyNPrice = new ComputeQtyNPrice();
                            computeQtyNPrice.execute();

//                            Intent intent = new Intent(CartListActivity.this, CartListActivity.class);
//                            startActivity(intent);



                            myDialog.dismiss();


//                            Toast.makeText(CartListActivity.this, "asfa", Toast.LENGTH_SHORT).show();
                        }
                    });
                    myDialog.show();
                }
            });
        }


        @Override
        protected String doInBackground(String... strings) {
            String ordID = "";
            String name = "";
            String amount = "";
            String qty = "";
            String price = "";
            String query = null;
            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    query = "SELECT * FROM order_tbl WHERE order_isdel = 0 AND order_cust_id = '" + y + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()){
                        name = rs.getString(4);
                        amount = rs.getString(7);
                        qty = rs.getString(5);
                        price = rs.getString(6);
                        ordID = rs.getString(3);

                        mName.add(name);
                        mPrice.add(amount);
                        mQty.add(qty);
                        mPri.add(price);
                        mOrdID.add(ordID);
                    }
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return query;
        }
    }

    public class RemoveItem extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Toast.makeText(CartListActivity.this, orderID, Toast.LENGTH_SHORT).show();
            ComputeTotalAmount computeTotalAmount = new ComputeTotalAmount();
            computeTotalAmount.execute();
            Toast.makeText(CartListActivity.this, "Order deleted...", Toast.LENGTH_SHORT).show();
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
                    query = "DELETE FROM order_tbl WHERE order_prod_id = '"+orderID+"'";
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

    public class ComputeTotalAmount extends AsyncTask<String, String, String>{
        String x="", totalAmount="NULLL";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(CartListActivity.this, totalAmount, Toast.LENGTH_SHORT).show();
//            Toast.makeText(CartListActivity.this, x, Toast.LENGTH_SHORT).show();
            try {
                if (totalAmount.equalsIgnoreCase("NULL")) {
                    mTOTALAMOUNT.setText("0");
                } else {
                    mTOTALAMOUNT.setText(totalAmount);
                }
            } catch (Exception e) {
                mTOTALAMOUNT.setText("0");
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
                    query = "SELECT SUM(order_amount) FROM order_tbl WHERE order_cust_id = '" + y + "' AND order_isdel = 0";
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

    public void ShowCash(View v){
        Button showCash;
        showCash = myDialog.findViewById(R.id.btnCash);
        showCash.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(CartListActivity.this, QueueActivity.class);
                startActivity(intent);
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    public void ShowPoints(View v){
        Button showPoints;
        showPoints = myDialog.findViewById(R.id.btnRewardPoints);
        showPoints.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                CheckGuest checkGuest = new CheckGuest();
                checkGuest.execute();


            }
        });
        myDialog.show();
    }

    public class ShowEdit extends AsyncTask<String, String, String>{
        String productName = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            mCartProdName.setText(productName);
            tvNumber.setText(CartPopUpQty);

//            ShowCartList showCartList = new ShowCartList();
//            showCartList.execute();
//
//            ComputeTotalAmount computeTotalAmount = new ComputeTotalAmount();
//            computeTotalAmount.execute();


        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT * FROM product_tbl JOIN order_tbl ON prod_id = order_prod_id WHERE prod_id = '" + orderID + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        productName = rs.getString(2);
                        CartPopUpQty = rs.getString(13);
                    }
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }

    public class InsertCartEdit extends AsyncTask<String, String, String>{
        String txtUserName = tvNumber.getText().toString();
        String query = "";
        String msg = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Intent i = new Intent(CartListActivity.this, CartListActivity.class);
            startActivity(i);

            ComputeTotalAmount computeTotalAmount = new ComputeTotalAmount();
            computeTotalAmount.execute();
//            Toast.makeText(getActivity(), "Successfully added to cart...", Toast.LENGTH_SHORT).show();
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
                    query = "UPDATE order_tbl SET order_qty = '" + txtUserName + "' WHERE order_prod_id = '" + orderID + "'  ";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    msg = "Added to Cart";
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return query;
        }
    }

    public class CheckGuest extends AsyncTask<String, String, String>{
        String ab;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (ab.equalsIgnoreCase(y)){
                Toast.makeText(CartListActivity.this, "You are a guest! You can't access the reward point system", Toast.LENGTH_LONG).show();
            }else{
                Intent intent = new Intent(CartListActivity.this, PayAsYouOrderActivity.class);
                startActivity(intent);
                myDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT * FROM user_tbl WHERE user_id = '7'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        ab = rs.getString(1);
                    }
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ComputeQtyNPrice extends AsyncTask<String, String, String>{
        String QTY = "";
        String PRICE = "";
        String TOTAL = "";
        Double mqty = 0.0;
        Double mprice = 0.0;
        Double total = 0.0;

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
                    String query = "SELECT * FROM order_tbl WHERE order_cust_id = '" + y + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        QTY = rs.getString(5);
                        PRICE = rs.getString(6);
                        mqty = Double.parseDouble(QTY);
                        mprice = Double.parseDouble(PRICE);
                        total = mprice * mqty;

                    }
                    String solving = "UPDATE order_tbl SET order_amount = '"+ total +"'";
                    Statement statement = con.createStatement();
                    statement.executeUpdate(solving);
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
}

