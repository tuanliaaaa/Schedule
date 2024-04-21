package com.example.myapplication.apdater;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entity.UserRcm;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecommendUserAdapter extends RecyclerView.Adapter<RecommendUserAdapter.ViewHolder> {
    private List<UserRcm> userRcms;
    private Context context;
    private OnItemRcmUserClickListener listener;
    public interface OnItemRcmUserClickListener {
        void onItemClick(UserRcm userRcm,int potition);
    }
    public void setOnItemClickListener(OnItemRcmUserClickListener listener) {
        this.listener = listener;
    }
    public RecommendUserAdapter(Context context, List<UserRcm> userRcms) {
        this.context = context;
        this.userRcms = userRcms;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_recommenduser, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = userRcms.get(position).getUsername();
        holder.textView.setText(item);

        String avatar = userRcms.get(position).getAvatar();
        Picasso.get().load(avatar).into(holder.imgRecommendUser);
        if (userRcms.get(position).getStatus() == "1") {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = 0;
            holder.itemView.setLayoutParams(layoutParams);
            holder.itemView.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return userRcms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public LinearLayout linearLayout;
        public ShapeableImageView imgRecommendUser;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textRecommendUser);
            linearLayout =itemView.findViewById(R.id.item_recommendUser);
            imgRecommendUser = itemView.findViewById(R.id.imgRecommendUser);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(userRcms.get(position),position);
                        }
                    }
                }
            });
        }
    }
}
