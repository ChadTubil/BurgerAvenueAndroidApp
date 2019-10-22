package com.example.chadd.burgeravenueV2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment {
    String y;
    public View rootView;

    private static final String TAG = "tab1";

    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mImageMenus = new ArrayList<>();
    private ArrayList<String> mProdID = new ArrayList<>();
    private ArrayList<String> mQty = new ArrayList<>();

    private static String prodID = "";

    public ImageView mCartProdImg;
    public TextView mCartProdName;
    public TextView mCartProdQty;
    public TextView mCartProdPrice;

    //Variable for inserting the cartpopup
    public String CartPopUpName = "";
    public Double CartPopUpAmount;
    public String CartPopUpPrice = "";
    public String CartPopUpQty = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab1listview, container, false);

        showTab1 showtab1 = new showTab1();
        showtab1.execute();

        return rootView;
    }
    public class TabAdapter extends ArrayAdapter<String> {
        private int resource;
        private LayoutInflater layoutInflater;
        private ArrayList<String> week = new ArrayList<>();


        public TabAdapter (Context context, int resource, ArrayList<String> objects){
            super (context, resource, objects);
            this. resource = resource;
            this. week = objects;

            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent){

            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resource, null);
                holder.image1 = convertView.findViewById(R.id.image1);
                holder.tvProductname = convertView.findViewById(R.id.tvProductname);
                holder.tvPrice = convertView.findViewById(R.id.tvPrice);
                holder.tvDescription = convertView.findViewById(R.id.tvDescription);
                holder.imagecart = convertView.findViewById(R.id.imagecart);
                convertView.setTag(holder);

            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }

            Glide.with(getContext()).load(mImageMenus.get(position)).into(holder.image1);

            holder.tvProductname.setText(mName.get(position));
            holder.tvPrice.setText(mPrice.get(position));
            holder.tvDescription.setText(mDescription.get(position));

            return convertView;
        }

        class ViewHolder{
            private ImageView image1, imagecart;
            private TextView tvProductname, tvPrice, tvDescription;
        }
    }
    public class showTab1 extends AsyncTask<String, String, String> {
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            ListView listView = rootView.findViewById(R.id.lvTablist);
            TabAdapter listAdapter = new TabAdapter(getActivity(), R.layout.fragment_tab1, mName);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = getLayoutInflater().inflate(R.layout.cartpopup, null);

                    prodID = mProdID.get(position);
                    y = db_UserId;
                    mCartProdImg = mView.findViewById(R.id.cartProdImg);
                    mCartProdName = mView.findViewById(R.id.cartProdName);
                    mCartProdQty = mView.findViewById(R.id.cartProdQty);
                    mCartProdPrice = mView.findViewById(R.id.cartProdPrice);

                    TextView mCartCloseBtn = mView.findViewById(R.id.cartCloseBtn);
                    Button btnminus = mView.findViewById(R.id.cartProdMinus);
                    Button btnplus = mView.findViewById(R.id.cartProdPlus);
                    CardView btnAddToCart = mView.findViewById(R.id.cartAddBtn);

                    final TextView tvNumber = mView.findViewById(R.id.cartProdQty);

                    ShowProdDetails showProdDetails = new ShowProdDetails();
                    showProdDetails.execute();

                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.setCanceledOnTouchOutside(false);

                    mCartCloseBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
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
                                Toast.makeText(getContext(), exp.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
                                Toast.makeText(getContext(), exp.toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    btnAddToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CartPopUpName = mCartProdName.getText().toString();
                            CartPopUpPrice = mCartProdPrice.getText().toString();
                            CartPopUpQty = mCartProdQty.getText().toString();
                            InsertCartPopUp icpu = new InsertCartPopUp();
                            icpu.execute();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {
            String a = "";
            String b = "";
            String c = "";
            String d = "";
            String f = "";
            String query = null;

            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    query = "SELECT * FROM product_tbl WHERE prod_isdel = 0 AND prod_category = 1";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()){
                        a = rs.getString(6);
                        b = rs.getString(2);
                        c = rs.getString(5);
                        d = rs.getString(3);
                        f = rs.getString(1);

                        mName.add(b);
                        mDescription.add(d);
                        mPrice.add(c);
                        mImageMenus.add(DBCon.HOST_URL + "/burgeravenue/live-preview/admin/html/burgerimg/" + a);
                        mProdID.add(f);
                    }
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return query;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class ShowProdDetails extends AsyncTask<String, String, String> {
        String productName = "";
        String productPrice = "";
        String productImage = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            mCartProdName.setText(productName);
            mCartProdPrice.setText(productPrice);
            Glide.with(getContext()).load(DBCon.HOST_URL + "/burgeravenue/live-preview/admin/html/burgerimg/" + productImage).into(mCartProdImg);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT * FROM product_tbl WHERE prod_id = '" + prodID + "'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        productName = rs.getString(2);
                        productPrice = rs.getString(5);
                        productImage = rs.getString(6);
                    }
                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }

    public class InsertCartPopUp extends AsyncTask<String, String, String>{
        String query = "";
        String msg = "";
        @Override
        protected void onPreExecute() {
//            Toast.makeText(getActivity(), "Adding Item To Cart", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getActivity(), "Successfully added to cart...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            CartPopUpAmount = Double.parseDouble(CartPopUpPrice) * Double.parseDouble(CartPopUpQty);
            try{
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {
                    msg = "Connection goes wrong";
                } else {
                    query = "INSERT INTO order_tbl (order_cust_id, order_prod_id, order_name, order_qty, order_price, order_amount, order_date, order_time, order_isdel) " +
                            "VALUES('"+y+"','"+prodID+"','"+CartPopUpName+"', '" +CartPopUpQty+"', '" +CartPopUpPrice+"','"+CartPopUpAmount+"'," +
                            "CURRENT_DATE, CURRENT_TIME, 0)";
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
}
