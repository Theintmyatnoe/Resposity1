package com.example.test.mydb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.test.mydb.mydao.HistoryDao;
import com.example.test.mydb.mydao.ProductDao;
import com.example.test.mydb.mydao.StoreDao;
import com.example.test.mydb.mydao.UserDao;
import com.example.test.mydb.myentity.HistoryTable;
import com.example.test.mydb.myentity.Products;
import com.example.test.mydb.myentity.Stores;
import com.example.test.mydb.myentity.Users;

@Database(entities ={Users.class, Products.class, Stores.class, HistoryTable.class},version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
    public abstract StoreDao storeDao();
    public abstract HistoryDao historyDao();
}
