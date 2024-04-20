package com.example.myapplication.apdater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.ProcessUser;

import java.util.List;


public class ListProcessUserAdapter extends RecyclerView.Adapter<ListProcessUserAdapter.ViewHolder> {
    private List<ProcessUser> processUsers;
    private Context context;
    private OnItemListProcessUserClickListener listener;
    public interface OnItemListProcessUserClickListener {
        void onItemClick(ProcessUser processUser,int potition);
    }
    public void setOnItemClickListener(OnItemListProcessUserClickListener listener) {
        this.listener = listener;
    }
    public ListProcessUserAdapter(Context context, List<ProcessUser> processUsers) {
        this.context = context;
        this.processUsers = processUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_process_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nameUser.setText(processUsers.get(position).getUsername().toString());
        holder.process.setText(processUsers.get(position).getProcess().toString());
        holder.status.setText(processUsers.get(position).getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return processUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameUser,process,status;
        public ViewHolder(View itemView) {
            super(itemView);
            nameUser = itemView.findViewById(R.id.nameUser);
            process =itemView.findViewById(R.id.process);
            status = itemView.findViewById(R.id.status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(processUsers.get(position),position);
                        }
                    }
                }
            });
        }
    }
}
