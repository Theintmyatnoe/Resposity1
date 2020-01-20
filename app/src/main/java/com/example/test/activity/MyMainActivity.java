package com.example.test.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.my_adapter.ProductAdatper;
import com.example.test.mydb.DatabaseClient;
import com.example.test.mydb.myentity.Products;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyMainActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton btnAddProductNew;
    private String userName;
    private SharedPreferences prf;
    private String myDate;
    private RecyclerView productRecycler;
    private TextView tvdate;
    private Button btnprevious,btnnext,btnLogout;
    private SimpleDateFormat df;
    private Calendar c;
    private int listsize;
    private Button btnSearch;
    private EditText edtBarcode;
    private String barcodeno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymain);
        setUp();
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_menu);
        View view = getSupportActionBar().getCustomView();
        tvdate=view.findViewById(R.id.mydate);
        btnprevious=view.findViewById(R.id.btn_decrease);
        btnnext=view.findViewById(R.id.btn_increase);
        btnLogout=view.findViewById(R.id.btn_logout);

        c = Calendar.getInstance();
        df = new SimpleDateFormat("yyyy-MM-dd");
        myDate = df.format(c.getTime());
//        myDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        tvdate.setText(myDate);

        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        userName=prf.getString("username",null);
        getProductList();
        List<Products> myproductList= (List<Products>) getIntent().getSerializableExtra("Product_List");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        productRecycler.setLayoutManager(linearLayoutManager);
        if (myproductList!=null){
            ProductAdatper adapter = new ProductAdatper(MyMainActivity.this, myproductList);
            productRecycler.setAdapter(adapter);
        }
        btnAddProductNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myuser=userName;
                Intent intent=new Intent(MyMainActivity.this, AddNewProduct.class);
                intent.putExtra("myuser",myuser);
                startActivity(intent);
                finish();
            }
        });
        btnprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, -1);
                myDate = df.format(c.getTime());

                Log.v("PREVIOUS DATE : ", myDate);
                tvdate.setText(myDate);
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, +1);
                myDate = df.format(c.getTime());

                Log.v("PREVIOUS DATE : ", myDate);
                tvdate.setText(myDate);
            }
        });
        btnLogout.setOnClickListener(this);
        tvdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                myDate=tvdate.getText().toString();
                getProductList();

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeno=edtBarcode.getText().toString().trim();
                if (!barcodeno.equals("")){
                    getProductByBarcode();
                }
            }
        });
    }

    private void getProductByBarcode() {
        class GetTasks extends AsyncTask<Void, Void, List<Products>> {

            @Override
            protected List<Products> doInBackground(Void... voids) {
                List<Products> products = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .getAllByBarcode(barcodeno);

                return products;
            }

            @Override
            protected void onPostExecute(List<Products> products) {
                super.onPostExecute(products);
                listsize=products.size();
                if (listsize==0){
                    Toast.makeText(getApplicationContext(),"No Data to show",Toast.LENGTH_SHORT).show();
                }
                ProductAdatper adapter = new ProductAdatper(MyMainActivity.this, products);
                productRecycler.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void setUp(){
        btnAddProductNew=findViewById(R.id.btn_add_product_new);
        productRecycler=findViewById(R.id.product_list_recycler);
        btnSearch=findViewById(R.id.btn_search);
        edtBarcode=findViewById(R.id.search_barcode);
    }
    private void getProductList() {
        class GetTasks extends AsyncTask<Void, Void, List<Products>> {

            @Override
            protected List<Products> doInBackground(Void... voids) {
                List<Products> products = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .getAllbyName(userName,myDate);

                return products;
            }

            @Override
            protected void onPostExecute(List<Products> products) {
                super.onPostExecute(products);
                listsize=products.size();
                if (listsize==0){
                    Toast.makeText(getApplicationContext(),"No Date:"+myDate,Toast.LENGTH_SHORT).show();
                }
                ProductAdatper adapter = new ProductAdatper(MyMainActivity.this, products);
                productRecycler.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                Intent intent=new Intent(MyMainActivity.this,Login.class);
                startActivity(intent);
                finish();
        }
    }
}
