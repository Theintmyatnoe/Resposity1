package com.example.test.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.test.my_adapter.HistoryAdapter;
import com.example.test.mydb.DatabaseClient;
import com.example.test.mydb.myentity.HistoryTable;
import com.example.test.mydb.myentity.Products;
import com.example.test.mydb.myentity.Stores;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNewProduct extends AppCompatActivity implements View.OnClickListener {
    private EditText edtBarcode,edtProductName,edtQty;
    private String barcode,productname,storename,qty,create_user,mydate,update_date,historyDate;
    private TextView tvhistoryTime,tvhistoryQty,tvSelectDate;
    private SharedPreferences pref;
    private Button btnSave;
    private List<Products> prod;
    private List<HistoryTable> myHistory;
    private Spinner spinner;
    private List<SpinnerList> storeList=new ArrayList<>();
    private ArrayList<String> mystoreList=new ArrayList<>();
    private List<Stores> stores;
    private DatePickerDialog datePickerDialog;
    private String mystoreid;
    private String product_create_date;
    private int historyProductId;
    private String historyqty;

    private RecyclerView historyRecycler;
    private TextView btnProductBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_product);
        setUp();
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.add_product_menu);
        View view = getSupportActionBar().getCustomView();
        btnProductBack=view.findViewById(R.id.btn_product_back);
        tvSelectDate=view.findViewById(R.id.product_create_date);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        historyRecycler.setLayoutManager(linearLayoutManager);

        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        create_user=pref.getString("username",null);
        update_date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        product_create_date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        historyDate= new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        tvSelectDate.setText(update_date);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
        myAsync myAsync=new myAsync();
        myAsync.execute();
        getTask();
        edtProductName.setFocusable(false);
        edtBarcode.setOnClickListener(this);
        tvSelectDate.setOnClickListener(this);
        btnProductBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddNewProduct.this,MyMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setUp(){
        edtBarcode=findViewById(R.id.edt_barcode);
        edtProductName=findViewById(R.id.edt_product_name);
        spinner=findViewById(R.id.store_spinner);
        edtQty=findViewById(R.id.edt_qty);
        btnSave=findViewById(R.id.btn_save_product);
//        edtdate=findViewById(R.id.edt_date);
        tvhistoryQty=findViewById(R.id.tv_history_qty);
        tvhistoryTime=findViewById(R.id.tv_history_time);
        historyRecycler=findViewById(R.id.history_recycler_list);
    }
    private void addProduct(){
        barcode=edtBarcode.getText().toString();
        productname=edtProductName.getText().toString();
        storename=spinner.getSelectedItem().toString();
        qty=edtQty.getText().toString();
        create_user=pref.getString("username",null);
        mydate=tvSelectDate.getText().toString();
        if (barcode.isEmpty()) {
            edtBarcode.setError("Barcode required");
            edtBarcode.requestFocus();
            return;
        }
        if (qty.isEmpty()) {
            edtQty.setError("Qty required");
            edtQty.requestFocus();
            return;
        }

        class SaveTask extends AsyncTask<Void, Void, List<Products>> {

            @Override
            protected List<Products> doInBackground(Void... voids) {

                //creating a task
                Products products=new Products();
                products.setBarcode(barcode);
                products.setProductname(productname);
                products.setCreate_user(create_user);
                products.setQty(qty);
                products.setStorename(storename);
                products.setCreate_date(mydate);
                products.setUpdate_date(update_date);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .productDao()
                        .insert(products);
                prod= (List<Products>) DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().productDao().getAll();
                Log.d("ProductAddNewList","ProductList"+prod);
                return prod;
            }

            @Override
            protected void onPostExecute(List<Products> aVoid) {
                super.onPostExecute(aVoid);
                for (int i=0;i<prod.size();i++){
                    historyProductId=prod.get(i).getId();
                }
                historyqty=qty;
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                addHistory();

            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }
    private void addHistory(){
        historyDate= new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        class SaveTask extends AsyncTask<Void, Void, List<HistoryTable>> {

            @Override
            protected List<HistoryTable> doInBackground(Void... voids) {

                //creating a task
                HistoryTable historyTable=new HistoryTable();
                historyTable.setProductId(historyProductId);
                historyTable.setBarcodeNo(barcode);
                historyTable.setQty(historyqty);
                historyTable.setCreate_date(product_create_date);
                historyTable.setUpdate_date(historyDate);
                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .historyDao()
                        .insert(historyTable);
                myHistory= (List<HistoryTable>) DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().historyDao().getAllbyProductid(historyProductId);
                Log.d("HistoryList","HistoryList"+myHistory);
                return myHistory;
            }

            @Override
            protected void onPostExecute(List<HistoryTable> aVoid) {
                super.onPostExecute(aVoid);
                HistoryAdapter adapter = new HistoryAdapter(AddNewProduct.this, myHistory);
                historyRecycler.setAdapter(adapter);
//                tvhistoryTime.setText(update_date);
//                tvhistoryQty.setText(historyqty);
//                Toast.makeText(getApplicationContext(), "load data", Toast.LENGTH_LONG).show();
            }
        }
        SaveTask st = new SaveTask();
        st.execute();
    }

    private void getDate(){
        // calender class's instance and get current date , month and year from calender
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialog = new DatePickerDialog(AddNewProduct.this,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    // set day of month , month and year value in the edit text
                    tvSelectDate.setText(year + "-"
                            + (monthOfYear + 1) + "-0" +dayOfMonth );

                }
            }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_barcode:
                mystoreid=edtBarcode.getText().toString();
                if (!mystoreid.equals("")){
                    getProduct();
                    getAddHistory();
                }

                break;
            case R.id.product_create_date:
                getDate();
                break;
        }

    }

    private void getProduct() {

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
    private void getAddHistory() {

        class GetTasks extends AsyncTask<Void, Void, List<HistoryTable>> {

            @Override
            protected List<HistoryTable> doInBackground(Void... voids) {
                List<HistoryTable> stores = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .historyDao()
                        .getAllByBarcode(mystoreid);

                return stores;
            }

            @Override
            protected void onPostExecute(List<HistoryTable> mystore) {
                super.onPostExecute(mystore);
                HistoryAdapter adapter = new HistoryAdapter(AddNewProduct.this, mystore);
                historyRecycler.setAdapter(adapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private class myAsync extends AsyncTask<Void, Void, List<Stores>> {
        private static final String SOAP_ACTION = "http://tempuri.org/GetAllStockInJSON";
        private static final String METHOD_NAME = "GetAllStockInJSON";
        private static final String NAMESPACE = "http://tempuri.org/";

        @Override
        protected List<Stores> doInBackground(Void... params) {
            String url="http://192.168.1.64:8020/Webservice/WebService_NPTDC.asmx";
            SoapObject request=new SoapObject(NAMESPACE,METHOD_NAME);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.implicitTypes = true;
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;

            HttpTransportSE httpTransport = new HttpTransportSE(url);
            httpTransport.debug = true;
            try {
                httpTransport.call(SOAP_ACTION, envelope);
            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                Log.e("HTTPLOG", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("IOLOG", e.getMessage());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                Log.e("XMLLOG", e.getMessage());
                e.printStackTrace();
            } //send request

            try {
                SoapPrimitive result = (SoapPrimitive)envelope.getResponse();
                JSONObject jsonObject= new JSONObject(result.toString());
                String responseData=jsonObject.getString("ResponseData");
                String responseInfo=jsonObject.getString("ResponseInfo");
                List<Stores> mystoreList=DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().storeDao().getAll();

                if(!responseData.equals("")){
                    JSONArray jsonArray=new JSONArray(responseData);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject1=jsonArray.getJSONObject(i);
                        Stores mystore=new Stores();
                        String mStoreId=jObject1.getString("StockInID");
                        Log.i("RESPONSE_NAME",String.valueOf(mStoreId));
                        mystore.setStoreId(mStoreId);

                        for (int j=0;j<mystoreList.size();j++){
                            String myId=mystoreList.get(j).getStoreId();
                            if(mStoreId.equals(myId)){
                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().storeDao().deleteByid(mStoreId);
                            }
                        }

                        String mBarcode=jObject1.getString("BarCodeNo");
                        Log.i("RESPONSE_NAME",String.valueOf(mBarcode));
                        mystore.setBarcodeNo(mBarcode);

                        String mStoreName=jObject1.getString("StoreName");
                        Log.i("RESPONSE_NAME",String.valueOf(mStoreName));
                        mystore.setStoreName(mStoreName);

                        String mProductName=jObject1.getString("ModelName");
                        Log.i("RESPONSE_NAME",String.valueOf(mProductName));
                        mystore.setProductname(mProductName);


                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().storeDao().insert(mystore);
                        stores= (List<Stores>) DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().storeDao().getAll();
                        Log.d("MyStoreList","StoreList"+stores);
                    }
                }

            }
            catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return stores;
        }

        @Override
        protected void onPostExecute(List<Stores> mystores) {
            super.onPostExecute(mystores);
        }
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

                storeList.clear();
                for (int i=0;i<mystores.size();i++){
                    SpinnerList myspinner=new SpinnerList();
                    myspinner.setMylist(mystores.get(i).getStoreName());

                    storeList.add(myspinner);
                }
                for (int i=0;i<storeList.size();i++){
                    mystoreList.add(storeList.get(i).getMylist());
                }
                return mystoreList;
            }

            @Override
            protected void onPostExecute(ArrayList<String> myspin) {
                super.onPostExecute(myspin);

                ArrayAdapter<String> adp1 = new ArrayAdapter<String>(AddNewProduct.this,
                        android.R.layout.simple_list_item_1, myspin);
                adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adp1);
            }


        }
        GetTeacherList gt=new GetTeacherList();
        gt.execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AddNewProduct.this,MyMainActivity.class);
        startActivity(intent);
        finish();
    }
}
