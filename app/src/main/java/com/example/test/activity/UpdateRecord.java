package com.example.test.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.myObj.SpinnerList;
import com.example.test.my_adapter.UpdateHistoryAdapter;
import com.example.test.mydb.DatabaseClient;
import com.example.test.mydb.myentity.HistoryTable;
import com.example.test.mydb.myentity.Products;
import com.example.test.mydb.myentity.Stores;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateRecord extends AppCompatActivity implements View.OnClickListener {
    private EditText edtbarcode,edtProductName,edtQty;
    private Spinner spinnerStore;
    private String selectStore,username,barcodeno,productname,qty,mystore1,historyDate,createDate;
    private int productid;
    private Button btnUpdate;
    private Products myproducts;
    private List<Products> productsList;
    private ArrayList<String> mystorelist=new ArrayList<>();
    private List<SpinnerList> myspinlist=new ArrayList<>();
    private RecyclerView historyRecycler;
    private TextView btnProductBack,tvSelectDate;
    private List<HistoryTable> myHistoryList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_product);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.add_product_menu);
        View view = getSupportActionBar().getCustomView();
        btnProductBack=view.findViewById(R.id.btn_product_back);
        tvSelectDate=view.findViewById(R.id.product_create_date);
        setUp();

        btnUpdate.setText("Update");
        myproducts= (Products) getIntent().getSerializableExtra("update_record");
        loadData();
        getTask();
        getHistory();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAssign(myproducts);
                addHistory();
            }
        });
        edtbarcode.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        historyRecycler.setLayoutManager(linearLayoutManager);
        btnProductBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateRecord.this,MyMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setUp(){
        edtbarcode=findViewById(R.id.edt_barcode);
        edtProductName=findViewById(R.id.edt_product_name);
        edtQty=findViewById(R.id.edt_qty);
//        edtDate=findViewById(R.id.edt_date);
        spinnerStore=findViewById(R.id.store_spinner);
        btnUpdate=findViewById(R.id.btn_save_product);
        historyRecycler=findViewById(R.id.history_recycler_list);

    }
    private void loadData(){
        edtProductName.setText(myproducts.getProductname());
        edtbarcode.setText(myproducts.getBarcode());
        edtQty.setText(myproducts.getQty());
        selectStore=myproducts.getStorename();
        tvSelectDate.setText(myproducts.getCreate_date());
        username=myproducts.getCreate_user();
        productid=myproducts.getId();
    }
    private void getTask(){
        class GetTeacherList extends AsyncTask<Void, Void, ArrayList<String>> {

            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                List<Stores> mystores = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .storeDao()
                        .getAll();

                myspinlist.clear();
                for (int i=0;i<mystores.size();i++){
                    SpinnerList myspinner=new SpinnerList();
                    myspinner.setMylist(mystores.get(i).getStoreName());

                    myspinlist.add(myspinner);
                }
                for (int i=0;i<myspinlist.size();i++){
                    mystorelist.add(myspinlist.get(i).getMylist());
                }
                return mystorelist;
            }

            @Override
            protected void onPostExecute(ArrayList<String> myspin) {
                super.onPostExecute(myspin);

                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(UpdateRecord.this,
                        android.R.layout.simple_list_item_1, myspin);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerStore.setAdapter(adp1);
                for (int i = 0; i < spinnerStore.getCount(); i++) {
                    if (spinnerStore.getItemAtPosition(i).toString().equals(selectStore)) {
                        spinnerStore.setSelection(i);
                    }
                }
            }

        }
        GetTeacherList gt=new GetTeacherList();
        gt.execute();
    }

    private void getProduct() {
        final String mystoreid=edtbarcode.getText().toString();
        class GetTasks extends AsyncTask<Void, Void, List<Stores>> {

            @Override
            protected List<Stores> doInBackground(Void... voids) {
                List<Stores> stores = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .storeDao()
                        .getAllBybarcode(mystoreid);

                return stores;
            }

            @Override
            protected void onPostExecute(List<Stores> mystore) {
                super.onPostExecute(mystore);
                edtProductName.setText(mystore.get(0).getProductname());
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void updateAssign(final Products products) {
        barcodeno=edtbarcode.getText().toString();
        productname=edtProductName.getText().toString();
        qty=edtQty.getText().toString();
        mystore1=spinnerStore.getSelectedItem().toString();
        createDate=tvSelectDate.getText().toString();
        if (barcodeno.isEmpty()) {
            edtbarcode.setError("Barcode required");
            edtbarcode.requestFocus();
            return;
        }
        if (qty.isEmpty()) {
            edtQty.setError("Qty required");
            edtQty.requestFocus();
            return;
        }

        class UpdateClass extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                products.setProductname(productname);
                products.setBarcode(barcodeno);
                products.setQty(qty);
                products.setStorename(mystore1);
                products.setCreate_user(username);
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .productDao()
                        .update(products);
                productsList=DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().productDao().getAll();
                Log.d("AssignRegisterList","AssignList"+productsList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
           }
        }
        UpdateClass ut = new UpdateClass();
        ut.execute();
    }

    @Override
    public void onClick(View v) {
        getProduct();
    }

    private void getHistory(){

        class GetTasks extends AsyncTask<Void, Void, List<HistoryTable>> {

            @Override
            protected List<HistoryTable> doInBackground(Void... voids) {
                List<HistoryTable> stores = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .historyDao()
                        .getAllbyProductid(productid);

                return stores;
            }

            @Override
            protected void onPostExecute(List<HistoryTable> mystore) {
                super.onPostExecute(mystore);
                UpdateHistoryAdapter adapter = new UpdateHistoryAdapter(UpdateRecord.this, mystore);
                historyRecycler.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();

    }
    private void addHistory(){
        historyDate= new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        class SaveTask extends AsyncTask<Void, Void, List<HistoryTable>> {

            @Override
            protected List<HistoryTable> doInBackground(Void... voids) {

                //creating a task
                HistoryTable historyTable=new HistoryTable();
                historyTable.setProductId(productid);
                historyTable.setQty(qty);
                historyTable.setCreate_date(createDate);
                historyTable.setUpdate_date(historyDate);
                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .historyDao()
                        .insert(historyTable);
                myHistoryList= (List<HistoryTable>) DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().historyDao().getAllbyProductid(productid);
                Log.d("HistoryList","HistoryList"+myHistoryList);
                return myHistoryList;
            }

            @Override
            protected void onPostExecute(List<HistoryTable> aVoid) {
                super.onPostExecute(aVoid);
                UpdateHistoryAdapter adapter = new UpdateHistoryAdapter(UpdateRecord.this, aVoid);
                historyRecycler.setAdapter(adapter);
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(UpdateRecord.this, MyMainActivity.class);
        intent.putExtra("Product_List", (Serializable) productsList);
        startActivity(intent);
        finish();
    }
}
