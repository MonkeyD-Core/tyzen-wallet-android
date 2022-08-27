package net.tyzen.io;



import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

public class Adaptor_history extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<cash_2> data= Collections.emptyList();
    cash_2 current;
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public Adaptor_history(Context context, List<cash_2> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_history, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        cash_2 current=data.get(position);
        myHolder.textTime.setText(current.timeStamp);
        myHolder.textType.setText(current.from);
        myHolder.textStatus.setText(current.status);
        myHolder.textAmount.setText(current.amount);
        //myHolder.textAmount.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textTime;
        TextView textType;
        TextView textStatus;
        TextView textAmount;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textTime= (TextView) itemView.findViewById(R.id.tTime);
            textType= (TextView) itemView.findViewById(R.id.tTypes);
            textAmount = (TextView) itemView.findViewById(R.id.tAmount);
            textStatus = (TextView) itemView.findViewById(R.id.tHash);
        }

    }


}


