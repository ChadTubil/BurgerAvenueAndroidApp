package com.example.chadd.burgeravenueV2;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.example.chadd.burgeravenueV2.DBCon.db_UserId;

public class CreateActivity extends AppCompatActivity {
    public static String name1, price1, name2, name3, name4, name5, name6, name7, name8,
            price2, price3, price4, price5, price6, price7, price8;
    public TextView Name1, Price1, Name2, Name3, Name4, Name5, Name6, Name7, Name8,
            Price2, Price3, Price4, Price5, Price6, Price7, Price8;
    ImageView Patties, Cheese, Lettuce, Onions, Tomatoes, Cucumber, Egg, Bacon, Check, Reset;
    ImageView TargetPatty, TargetCheese, TargetLettuce, TargetOnions, TargetCucucmber, TargetEgg, TargetBacon, TargetTomatoes, TargetBunsTop;
    List<String> ingredientsArr = new ArrayList<String>();
    EditText NameYourBurger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Patties = findViewById(R.id.imgpatties);
        Cheese = findViewById(R.id.imgcheese);
        Lettuce = findViewById(R.id.imglettuce);
        Onions = findViewById(R.id.imgonios);
        Tomatoes = findViewById(R.id.tomatoes);
        Cucumber = findViewById(R.id.imgcucumber);
        Egg = findViewById(R.id.imgegg);
        Bacon = findViewById(R.id.imgbacon);
        Check = findViewById(R.id.check);
        Reset = findViewById(R.id.reset);


        TargetPatty = findViewById(R.id.targetpatty);
        TargetCheese = findViewById(R.id.targetcheese);
        TargetLettuce = findViewById(R.id.targetgreenletuce);
        TargetOnions = findViewById(R.id.targetonion);
        TargetCucucmber = findViewById(R.id.targetcucumber);
        TargetEgg = findViewById(R.id.targetegg);
        TargetBacon = findViewById(R.id.targetbacon);
        TargetTomatoes = findViewById(R.id.targettomato);
        TargetBunsTop = findViewById(R.id.targetbunstop);


        Patties.setOnLongClickListener(longclickListener);
        Cheese.setOnLongClickListener(longclickListener);
        Lettuce.setOnLongClickListener(longclickListener);
        Onions.setOnLongClickListener(longclickListener);
        Tomatoes.setOnLongClickListener(longclickListener);
        Cucumber.setOnLongClickListener(longclickListener);
        Egg.setOnLongClickListener(longclickListener);
        Bacon.setOnLongClickListener(longclickListener);
        TargetPatty.setOnDragListener(dragListener);
        TargetCheese.setOnDragListener(dragListener);
        TargetLettuce.setOnDragListener(dragListener);
        TargetOnions.setOnDragListener(dragListener);
        TargetTomatoes.setOnDragListener(dragListener);
        TargetCucucmber.setOnDragListener(dragListener);
        TargetEgg.setOnDragListener(dragListener);
        TargetBacon.setOnDragListener(dragListener);



        Name1 = findViewById(R.id.t1);
        Price1 = findViewById(R.id.t2);
        Name2 = findViewById(R.id.t3);
        Price2 = findViewById(R.id.t4);
        Name3 = findViewById(R.id.t5);
        Price3 = findViewById(R.id.t6);
        Name4 = findViewById(R.id.t7);
        Price4 = findViewById(R.id.t8);
        Name5 = findViewById(R.id.t11);
        Price5 = findViewById(R.id.t22);
        Name6 = findViewById(R.id.t33);
        Price6 = findViewById(R.id.t44);
        Name7 = findViewById(R.id.t55);
        Price7 = findViewById(R.id.t66);
        Name8 = findViewById(R.id.t77);
        Price8 = findViewById(R.id.t88);

        NameYourBurger = findViewById(R.id.etnameburger);
        NameYourBurger.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

//        TypedArray arrayResources = getResources().obtainTypedArray(
//                R.array.resicon);

//        for (int i = 0; i < arrayResources.length(); i++) {
//            ImageView imageView = new ImageView(this);
//            imageView.setImageDrawable(arrayResources.getDrawable(i));
//            imageView.setOnLongClickListener(longclickListener);
//            Target.addView(imageView);
//        }
//
//        arrayResources.recycle();

        ShowPatties showPatties = new ShowPatties();
        showPatties.execute();

        ShowCheese showCheese = new ShowCheese();
        showCheese.execute();

        ShowLettuce showLettuce = new ShowLettuce();
        showLettuce.execute();

        ShowOnion showOnion = new ShowOnion();
        showOnion.execute();

        ShowTomatoes showTomatoes = new ShowTomatoes();
        showTomatoes.execute();

        ShowCucumber showCucumber = new ShowCucumber();
        showCucumber.execute();

        ShowEgg showEgg = new ShowEgg();
        showEgg.execute();

        ShowBacon showBacon = new ShowBacon();
        showBacon.execute();

        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NameYourBurger.getText().toString().matches("")) {
                    NameYourBurger.setError("Please name your burger.");
                }else if (TargetLettuce.getDrawable()==null){
                    TargetLettuce.setImageResource(R.drawable.chadbunstop);
                }else if(TargetCheese.getDrawable() == null){
                    TargetCheese.setImageResource(R.drawable.chadbunstop);
                }else if(TargetPatty.getDrawable() == null){
                    TargetPatty.setImageResource(R.drawable.chadbunstop);
                }else if(TargetTomatoes.getDrawable() == null){
                    TargetTomatoes.setImageResource(R.drawable.chadbunstop);
                }else if (TargetCucucmber.getDrawable() == null){
                    TargetCucucmber.setImageResource(R.drawable.chadbunstop);
                }else if (TargetOnions.getDrawable() == null){
                    TargetOnions.setImageResource(R.drawable.chadbunstop);
                }else if (TargetEgg.getDrawable() == null){
                    TargetEgg.setImageResource(R.drawable.chadbunstop);
                }else if (TargetBacon.getDrawable() == null){
                    TargetBacon.setImageResource(R.drawable.chadbunstop);
                }else if (TargetBunsTop.getDrawable() == null) {
                    TargetBunsTop.setImageResource(R.drawable.chadbunstop);
                }else if (TargetBunsTop.getDrawable() == null){
                    TargetBunsTop.setImageResource(R.drawable.bunstop);
                }else{
                    Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                }
                if (NameYourBurger.getText().toString().matches("")) {
                    NameYourBurger.setError("Please name your burger");
                }else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateActivity.this);
                    builder1.setMessage("Are you sure of this?");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(CreateActivity.this, "Makz", Toast.LENGTH_SHORT).show();
                                    InsertIngredients insertIngredients = new InsertIngredients();
                                    insertIngredients.execute();
                                    Intent i = new Intent(CreateActivity.this, CartListActivity.class);
                                    startActivity(i);

                                }
                            });
                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(CreateActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });
                    builder1.create();
                    builder1.show();
                }
            }
        });
        
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TargetBunsTop.setImageDrawable(null);
                TargetPatty.setImageDrawable(null);
                TargetCheese.setImageDrawable(null);
                TargetLettuce.setImageDrawable(null);
                TargetOnions.setImageDrawable(null);
                TargetTomatoes.setImageDrawable(null);
                TargetCucucmber.setImageDrawable(null);
                TargetEgg.setImageDrawable(null);
                TargetBacon.setImageDrawable(null);

            }
        });
    }
    public class ShowBacon extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name8.setText(name8);
            Price8.setText(price8);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 8 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name8 = rs.getString(1);
                        price8 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowEgg extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name7.setText(name7);
            Price7.setText(price7);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 7 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name7 = rs.getString(1);
                        price7 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowCucumber extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name6.setText(name6);
            Price6.setText(price6);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 6 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name6 = rs.getString(1);
                        price6 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowPatties extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name1.setText(name1);
            Price1.setText(price1);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 1 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name1 = rs.getString(1);
                        price1 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowCheese extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name2.setText(name2);
            Price2.setText(price2);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 2 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name2 = rs.getString(1);
                        price2 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowLettuce extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name3.setText(name3);
            Price3.setText(price3);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 3 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name3 = rs.getString(1);
                        price3 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowOnion extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name4.setText(name4);
            Price4.setText(price4);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 5 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name4 = rs.getString(1);
                        price4 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }
    public class ShowTomatoes extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Name5.setText(name5);
            Price5.setText(price5);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    String query = "SELECT ingredients_name, ingredients_price FROM ingredients_tbl WHERE ingredients_id = 4 AND ingredients_isdel = 0";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()){
                        name5 = rs.getString(1);
                        price5 = rs.getString(2);
                    }

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return null;
        }
    }

    View.OnLongClickListener longclickListener = new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v){
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data,myShadowBuilder,v,0    );
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener(){
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (view.getId() == R.id.imgpatties) {
//                        Target.setImageResource(R.drawable.dndpatty1);
                    } else if (view.getId() == R.id.imgcheese) {
//                        Target.setImageResource(R.drawable.dndcheese1);
                    } else if (view.getId() == R.id.imglettuce) {
//                        Target.setImageResource(R.drawable.dndletuce);
                    } else if (view.getId() == R.id.imgonios){
//                        Target.setImageResource(R.drawable.onion);
                    }else if (view.getId() == R.id.tomatoes){

                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if (view.getId() == R.id.imgpatties) {
//                        Target.setText("Patties Out");
//                    } else if (view.getId() == R.id.text2) {
//                        txt4.setText("TextView 2 Is Exited");
//                    } else if (view.getId() == R.id.text3) {
//                        txt4.setText("TextView 3 Is Exited");
                    }
                    break;
                case DragEvent.ACTION_DROP:
//                    Toast.makeText(CreateActivity.this, String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
//                    LinearLayout oldParent = (LinearLayout)view.getParent();
//                    oldParent.removeView(view);
//                    LinearLayout newParent = (LinearLayout)v;
//                    newParent.addView(view);
                    if (view.getId() == R.id.imgpatties) {
                        ingredientsArr.add("Patties");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.patty);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.patty);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.patty);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.patty);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.patty);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.patty);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.patty);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.patty);
                        }else {
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }

                    } else if (view.getId() == R.id.imgcheese) {
                        ingredientsArr.add("Cheese");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.cheese);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.cheese);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.cheese);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.cheese);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.cheese);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.cheese);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.cheese);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.cheese);
                        }else {
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }
                    } else if (view.getId() == R.id.imglettuce) {
                        ingredientsArr.add("Lettuce");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.greenlettuce);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.greenlettuce);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.greenlettuce);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.greenlettuce);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.greenlettuce);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.greenlettuce);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.greenlettuce);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.greenlettuce);
                        }else {
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }
                    } else if (view.getId() == R.id.imgonios){
                        ingredientsArr.add("Onions");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.onions);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.onions);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.onions);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.onions);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.onions);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.onions);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.onions);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.onions);
                        }else {
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }
                    }else if (view.getId() == R.id.tomatoes){
                        ingredientsArr.add("Tomatoes");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.tomato);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.tomato);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.tomato);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.tomato);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.tomato);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.tomato);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.tomato);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.tomato);
                        }else {
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }
                    } else if (view.getId() == R.id.imgcucumber){
                        ingredientsArr.add("Cucumber");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.cucumber);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.cucumber);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.cucumber);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.cucumber);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.cucumber);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.cucumber);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.cucumber);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.cucumber);
                        }else {
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }
                    } else if (view.getId() == R.id.imgegg){
                        ingredientsArr.add("Egg");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.egg);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.egg);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.egg);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.egg);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.egg);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.egg);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.egg);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.egg);
                        }else {
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }
                    } else if (view.getId() == R.id.imgbacon){
                        ingredientsArr.add("Bacon");
                        if(TargetLettuce.getDrawable() == null){
                            TargetLettuce.setImageResource(R.drawable.bacon);
                        }else if(TargetCheese.getDrawable() == null){
                            TargetCheese.setImageResource(R.drawable.bacon);
                        }else if(TargetPatty.getDrawable() == null){
                            TargetPatty.setImageResource(R.drawable.bacon);
                        }else if(TargetTomatoes.getDrawable() == null){
                            TargetTomatoes.setImageResource(R.drawable.bacon);
                        }else if (TargetCucucmber.getDrawable() == null){
                            TargetCucucmber.setImageResource(R.drawable.bacon);
                        }else if (TargetOnions.getDrawable() == null){
                            TargetOnions.setImageResource(R.drawable.bacon);
                        }else if (TargetEgg.getDrawable() == null){
                            TargetEgg.setImageResource(R.drawable.bacon);
                        }else if (TargetBacon.getDrawable() == null){
                            TargetBacon.setImageResource(R.drawable.bacon);
                        }else if (TargetBunsTop.getDrawable() == null){
                            TargetBunsTop.setImageResource(R.drawable.bunstops);
                        }else{
                            Toast.makeText(CreateActivity.this, "You reach the limit of ingredients", Toast.LENGTH_SHORT).show();
                        }
                    }
//                    view.animate()
//                            .x(Target.getX())
//                            .y(Target.getY())
//                            .setDuration(700)
//                            .start();
                    break;
            }
            return true;
        }
    };

    public class InsertIngredients extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
//            Toast.makeText(CreateActivity.this, s, Toast.LENGTH_SHORT).show();
            ingredientsArr.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            String sqlQuery="", ingredients="", description="", sqlMixNMatch="", maxProdID="";
            Statement sqlStatement, stmtMixNMatch;
            ResultSet rsStatement;
            Double totalAmount=0.0, priceIngredient=0.0;
            int x=0;

            try {
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DBCon.DB_URL, DBCon.DB_USER, DBCon.DB_PASSWORD);
                if (con == null) {

                } else {
                    while(x < ingredientsArr.size()) {
                        ingredients = ingredientsArr.get(x);
                        sqlQuery = "SELECT * FROM ingredients_tbl WHERE ingredients_name='" + ingredients + "'";
                        sqlStatement = con.createStatement();
                        rsStatement = sqlStatement.executeQuery(sqlQuery);
                        if(rsStatement.next()) {
                            priceIngredient = Double.parseDouble(rsStatement.getString(3));
                            totalAmount = totalAmount + priceIngredient;

                            sqlMixNMatch = "INSERT INTO mixnmatch_tbl VALUES(NULL, '" + ingredients + "', '" + priceIngredient + "', CURRENT_DATE)";
                            stmtMixNMatch = con.createStatement();
                            stmtMixNMatch.executeUpdate(sqlMixNMatch);
                        }
                        x++;
                    }

                    String queryA = "SELECT mix_name, COUNT(*) c FROM mixnmatch_tbl WHERE mix_date = CURRENT_DATE GROUP BY mix_name;";
                    Statement stmtA = con.createStatement();
                    ResultSet rsA = stmtA.executeQuery(queryA);
                    while (rsA.next()) {
                        description = description + rsA.getString(2) + " " + rsA.getString(1);
                    }
                    String mixName = "MNM-" + NameYourBurger.getText().toString();
                    String queryB = "INSERT INTO product_tbl(prod_name, prod_description, prod_price, prod_isdel, prod_category) VALUES('" + mixName + "', '" + description + "', '" + totalAmount + "', '1', '0')";
                    Statement stmtB = con.createStatement();
                    stmtB.executeUpdate(queryB);

                    String queryC = "SELECT MAX(prod_id) FROM product_tbl";
                    Statement stmtC = con.createStatement();
                    ResultSet rsC = stmtC.executeQuery(queryC);
                    if(rsC.next()) {
                        maxProdID = rsC.getString(1);
                    }
                    String queryD = "INSERT INTO order_tbl() VALUES(NULL, '"+db_UserId+"', '" + maxProdID + "', '" + mixName + "', '1', '" + totalAmount + "', '" + totalAmount + "', CURRENT_DATE, CURRENT_TIME, '', '0', '')";
                    Statement stmtD = con.createStatement();
                    stmtD.executeUpdate(queryD);

                    String queryE = "DELETE FROM mixnmatch_tbl WHERE mix_date = CURRENT_DATE";
                    Statement stmtE = con.createStatement();
                    stmtE.executeUpdate(queryE);

                }
                con.close();
            }catch (Exception e){
                e.getMessage();
            }
            return String.valueOf(totalAmount);
        }
    }

}
