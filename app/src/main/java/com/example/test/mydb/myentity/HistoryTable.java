package com.example.test.mydb.myentity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class HistoryTable implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="productId")
    private int productId;
    @ColumnInfo(name="barcodeNo")
    private String barcodeNo;
    @ColumnInfo(name="qty")
    private String qty;
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
    public String getBarcodeNo(){
        return barcodeNo;
    }
    public void setBarcodeNo(String barcodeNo){
        this.barcodeNo=barcodeNo;
    }

    public int getProductId(){
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public String getQty(){
        return qty;
    }
    public void setQty(String qty){
        this.qty=qty;
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
