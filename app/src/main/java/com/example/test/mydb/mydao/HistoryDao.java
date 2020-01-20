package com.example.test.mydb.mydao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.test.mydb.myentity.HistoryTable;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM HistoryTable")
    List<HistoryTable> getAll();

    @Query("SELECT * FROM HistoryTable where productId=:productid")
    List<HistoryTable> getAllbyProductid(int productid);
    @Query("SELECT * FROM HistoryTable where barcodeNo=:barcodeno")
    List<HistoryTable> getAllByBarcode(String barcodeno);

//    @Query("Delete FROM Stores where storeId=:storeid")
//    void deleteByid(String storeid);

    @Insert
    void insert(HistoryTable historyTable);
    @Delete
    void delete(HistoryTable historyTable);

}
