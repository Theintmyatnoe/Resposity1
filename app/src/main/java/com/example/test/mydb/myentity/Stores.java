package com.example.test.mydb.myentity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Stores implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="storeId")
    private String storeId;
    @ColumnInfo(name="storeName")
    private String storeName;
    @ColumnInfo(name="barcodeNo")
    private String barcodeNo;
    @ColumnInfo(name="productname")
    private String productname;

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    public String getStoreId(){
        return storeId;
    }
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public String getStoreName(){
        return storeName;
    }
    public void setStoreName(String storeName){
        this.storeName=storeName;
    }
    public String getBarcodeNo(){
        return barcodeNo;
    }
    public void setBarcodeNo(String barcodeNo){
        this.barcodeNo=barcodeNo;
    }
    public String getProductname(){
        return productname;
    }
    public void setProductname(String productname){
        this.productname=productname;
    }
}
