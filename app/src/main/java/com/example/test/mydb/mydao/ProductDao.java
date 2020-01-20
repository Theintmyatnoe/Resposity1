package com.example.test.mydb.mydao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.test.mydb.myentity.Products;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Products")
    List<Products> getAll();

    @Query("SELECT * FROM products WHERE  create_by= :username and create_date=:mydate")
    List<Products> getAllbyName(String username,String mydate);

    @Query("SELECT * FROM products WHERE barcode=:mybarcode")
    List<Products> getAllByBarcode(String mybarcode);

    @Insert
    void insert(Products products);

    @Delete
    void delete(Products products);

    @Update
    void update(Products products);
}
