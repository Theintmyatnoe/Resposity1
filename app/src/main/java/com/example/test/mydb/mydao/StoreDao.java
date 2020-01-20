package com.example.test.mydb.mydao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.test.mydb.myentity.Stores;

import java.util.List;

@Dao
public interface StoreDao {
    @Query("SELECT * FROM Stores")
    List<Stores> getAll();

    @Query("SELECT * FROM Stores where barcodeNo=:barcodeno")
    List<Stores> getAllBybarcode(String barcodeno);

    @Query("Delete FROM Stores where storeId=:storeid")
    void deleteByid(String storeid);

    @Insert
    void insert(Stores mystore);
    @Delete
    void delete(Stores mystore);
}
