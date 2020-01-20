package com.example.test.my_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.MainActivity;
import com.example.test.R;
import com.example.test.activity.UpdateRecord;
import com.example.test.mydb.myentity.Products;

import java.util.List;

public class ProductAdatper extends RecyclerView.Adapter<ProductAdatper.TasksViewHolder> {
    private Context mCtx;
    private List<Products> myassign;

    public ProductAdatper(Context mCtx, List<Products> myassign) {
        this.mCtx = mCtx;
        this.myassign = myassign;
    }

    @Override
    public ProductAdatper.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_recycler, parent, false);
        return new ProductAdatper.TasksViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ProductAdatper.TasksViewHolder holder, int position) {
        holder.showData(myassign.get(position));
    }

    @Override
    public int getItemCount() {

        //return student.size();
        if (myassign.size() > 0) {
            return myassign.size();
        }
        else return 0;
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvProductName,tvBarcoder,tvStoreName,tvQty,tvcreateBy;

        public TasksViewHolder(View itemView) {
            super(itemView);
            tvBarcoder=itemView.findViewById(R.id.tvBarcode);
            tvProductName=itemView.findViewById(R.id.tvProduct);
            tvcreateBy=itemView.findViewById(R.id.create_by);
            tvStoreName=itemView.findViewById(R.id.tvstore);
            tvQty=itemView.findViewById(R.id.tvqty);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Products assign=myassign.get(getAdapterPosition());
            Intent intent=new Intent(mCtx, UpdateRecord.class);
            intent.putExtra("update_record",assign);
            mCtx.startActivity(intent);

        }
        void showData(Products myproduct){
            if(myproduct!=null){
                tvProductName.setText("Product Name :\t"+myproduct.getProductname());
                tvStoreName.setText("Store Name :\t"+myproduct.getStorename());
                tvQty.setText("Quantity :\t"+myproduct.getQty());
                tvcreateBy.setText("Create By :\t"+myproduct.getCreate_user());
                tvBarcoder.setText("Barcode No :\t"+myproduct.getBarcode());
            }
        }
    }
}
