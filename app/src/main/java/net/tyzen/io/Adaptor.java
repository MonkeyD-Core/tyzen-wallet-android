package net.tyzen.io;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class Adaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<cash_> data= Collections.emptyList();
    cash_ current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public Adaptor(Context context, List<cash_> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_fish, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        cash_ current=data.get(position);
        myHolder.textTime.setImageResource(current.timeStamp);
        myHolder.textType.setText(current.from);
        //myHolder.textHash.setText(current.hash);
        myHolder.textAmount.setText(current.amount);

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView textTime;
        TextView textType;
        TextView textHash;
        TextView textAmount;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textTime= (ImageView) itemView.findViewById(R.id.tTime);
            textType= (TextView) itemView.findViewById(R.id.tTypes);
            textAmount = (TextView) itemView.findViewById(R.id.tAmount);
            //textHash = (TextView) itemView.findViewById(R.id.tHash);
        }
    }

    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

