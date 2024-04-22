package com.example.myapplication.apdater;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.controller.activities.AccountFeatureActivity;
import com.example.myapplication.dto.response.TeamResponse;
import com.example.myapplication.entity.Team;

import java.util.List;

public class ListTeamAdapter extends RecyclerView.Adapter<ListTeamAdapter.TeamViewHolder> {
    private List<Team> mListTeam;
    private Context mcontext; //cung cấp thông tin về môi trường ứng dụng

    public ListTeamAdapter(Context context, List<Team> mListTeam) {
        this.mcontext = context;
        this.mListTeam = mListTeam;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_team, parent, false);

        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        final Team team = mListTeam.get(position);
        if (team == null){
            return;
        }
        holder.teamName.setText(team.getTeamName());

        holder.itemTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoToNotificationDetail(team);
            }
        });
    }

    private void onClickGoToNotificationDetail(Team team){
        Intent intent = new Intent(mcontext, AccountFeatureActivity.class); //khai báo để mở activity mới
        Bundle bundle = new Bundle();
        bundle.putSerializable("idTeam", team.getIdTeam());
        intent.putExtras(bundle);
        mcontext.startActivity(intent);
    }
    @Override
    public int getItemCount() {
        if (mListTeam != null){
            return mListTeam.size();
        }
        return 0;
    }

    public class TeamViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout itemTeam;

        private TextView teamName;
        private TextView txtInfo;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemTeam = itemView.findViewById(R.id.item_team);
            this.teamName = itemView.findViewById(R.id.teamName);
        }
    }
}