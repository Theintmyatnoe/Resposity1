package com.example.test.my_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.mydb.myentity.HistoryTable;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.TasksViewHolder> {
    private Context mCtx;
    private List<HistoryTable> myassign;

    public HistoryAdapter(Context mCtx, List<HistoryTable> myassign) {
        this.mCtx = mCtx;
        this.myassign = myassign;
    }

    @Override
    public HistoryAdapter.TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.history_recycler, parent, false);
        return new HistoryAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.TasksViewHolder holder, int position) {
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

    class TasksViewHolder extends RecyclerView.ViewHolder {

        TextView tvhistoryTime,tvHistoryQty;

        public TasksViewHolder(View itemView) {
            super(itemView);
           tvHistoryQty=itemView.findViewById(R.id.tv_history_qty);
           tvhistoryTime=itemView.findViewById(R.id.tv_history_time);

        }
        void showData(HistoryTable myproduct){
            if(myproduct!=null){
                tvhistoryTime.setText(myproduct.getCreate_date());
                tvHistoryQty.setText(myproduct.getQty());
            }
        }
    }
}
