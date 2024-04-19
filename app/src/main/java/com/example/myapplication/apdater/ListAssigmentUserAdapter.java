package com.example.myapplication.apdater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.AssigmentUser;
import com.example.myapplication.entity.UserRcm;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListAssigmentUserAdapter extends RecyclerView.Adapter<ListAssigmentUserAdapter.ViewHolder> {
    private List<AssigmentUser> assigmentUsers;
    private Context context;
    private OnItemListAssigmentUserClickListener listener;
    public interface OnItemListAssigmentUserClickListener {
        void onItemClick(AssigmentUser assigmentUser,int potition);
    }
    public void setOnItemClickListener(OnItemListAssigmentUserClickListener listener) {
        this.listener = listener;
    }
    public ListAssigmentUserAdapter(Context context, List<AssigmentUser> assigmentUsers) {
        this.context = context;
        this.assigmentUsers = assigmentUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_assigment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       holder.nameAssigment.setText(assigmentUsers.get(position).getNameAssignment().toString());
       holder.process.setText(assigmentUsers.get(position).getProcess().toString());
       holder.status.setText(assigmentUsers.get(position).getStatus().toString());
    }

    @Override
    public int getItemCount() {
        return assigmentUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameAssigment,process,status;
        public ViewHolder(View itemView) {
            super(itemView);
            nameAssigment = itemView.findViewById(R.id.nameAssigment);
            process =itemView.findViewById(R.id.processAssigment);
            status = itemView.findViewById(R.id.statusProcess);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(assigmentUsers.get(position),position);
                        }
                    }
                }
            });
        }
    }
}
