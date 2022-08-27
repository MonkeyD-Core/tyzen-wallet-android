package net.tyzen.io;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<List_Data> list_data;
    private Context context;

    public MyAdapter(List<List_Data> list_data, Context context) {
        this.list_data = list_data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final List_Data listData=list_data.get(position);

        holder.image.setImageResource(listData.image);
        holder.txthash.setText(listData.name);
        holder.txtamount.setText(listData.amount);

        holder.txthash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,DetailView.class);
                intent.putExtra("name",listData.name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private  ImageView image;
        private TextView txthash;
        private TextView txtamount;

        public ViewHolder(View itemView) {
            super(itemView);
            image=(ImageView) itemView.findViewById(R.id.image_view);
            txthash=(TextView)itemView.findViewById(R.id.txt_hash);
            txtamount=(TextView)itemView.findViewById(R.id.txt_amount);
        }
    }
}