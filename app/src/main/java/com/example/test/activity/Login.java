package com.example.test.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.mydb.DatabaseClient;
import com.example.test.mydb.myentity.Users;

import java.util.List;

public class Login extends AppCompatActivity {
    private Button btnlogin;
    private EditText edtusername,edtpass;
    private String username,password;
    private SharedPreferences preferences;
    private int mysize;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setUp();
        getSupportActionBar().hide();
        preferences= getSharedPreferences("user_details",MODE_PRIVATE);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=edtusername.getText().toString();
                password=edtpass.getText().toString();
                if(!username.equals("") && !password.equals("")){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username",username);
                    editor.putString("password",password);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Login.this,MyMainActivity.class);
                    startActivity(intent);
                    getUserList();
                    if (mysize<0){
                        saveTask();
                    }
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login fail", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void setUp(){
        btnlogin=findViewById(R.id.btn_login);
        edtusername=findViewById(R.id.edt_username);
        edtpass=findViewById(R.id.edt_pass);

    }
    private void saveTask() {
        username=edtusername.getText().toString().trim();
        password=edtpass.getText().toString().trim();

        class SaveClass extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Users users = new Users();
                users.setName(username);
                users.setPassword(password);

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao()
                        .insert(users);
                List<Users> myusers= (List<Users>) DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().userDao().getAll();
                Log.d("UserList","UserList"+myusers);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username",username);
                editor.putString("password",password);
                editor.commit();
            }
        }
        SaveClass st = new SaveClass();
        st.execute();
    }
    private void getUserList(){
        username=edtusername.getText().toString().trim();
        password=edtpass.getText().toString().trim();
        class GetStudentList extends AsyncTask<Void, Void, List<Users>> {

            @Override
            protected List<Users> doInBackground(Void... voids) {


                List<Users> users= DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao()
                        .getAllbyName(username,password);


                return users;
            }

            @Override
            protected void onPostExecute(List<Users> students) {
                super.onPostExecute(students);
                mysize=students.size();

            }
        }
        GetStudentList gtl=new GetStudentList();
        gtl.execute();
    }
}
