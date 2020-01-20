package com.example.test.mydb.myentity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Products implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="product_name")
    private String productname;
    @ColumnInfo(name="barcode")
    private String barcode;
    @ColumnInfo(name="store_name")
    private String storename;
    @ColumnInfo(name="qty")
    private String qty;
    @ColumnInfo(name="create_by")
    private String create_user;
    @ColumnInfo(name="create_date")
    private String create_date;
    @ColumnInfo(name="update_date")
    private String update_date;

    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProductname(){
        return productname;
    }
    public void setProductname(String productname){
        this.productname=productname;
    }
    public String getBarcode(){
        return barcode;
    }
    public void setBarcode(String barcode){
        this.barcode=barcode;
    }
    public String getStorename(){
        return storename;
    }
    public void setStorename(String storename){
        this.storename=storename;
    }
    public String getQty(){
        return qty;
    }
    public void setQty(String qty){
        this.qty=qty;
    }
    public String getCreate_user(){
        return create_user;
    }
    public void setCreate_user(String create_user){
        this.create_user=create_user;
    }
    public String getCreate_date(){
        return create_date;
    }
    public void setCreate_date(String create_date){
        this.create_date=create_date;
    }
    public String getUpdate_date(){
        return update_date;
    }
    public void setUpdate_date(String update_date){
        this.update_date=update_date;
    }
}
