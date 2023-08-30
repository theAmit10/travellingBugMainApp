package com.travel.travellingbug.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.travel.travellingbug.R;
import com.travel.travellingbug.chat.InboxChatActivity;
import com.travel.travellingbug.models.InboxModel;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    Context context;
    ArrayList<InboxModel> list;

    public InboxAdapter(Context context, ArrayList<InboxModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_inbox_fragment,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        InboxModel inboxModel = list.get(position);

        try {

            holder.userName.setText(inboxModel.getUsername());
            Picasso.get().load(inboxModel.getProfileImage()).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);


            holder.historyContainerLL.setOnClickListener(view -> {

                Intent intent = new Intent(context, InboxChatActivity.class);

                intent.putExtra("requestId",inboxModel.getRequestId());
                intent.putExtra("providerId", inboxModel.getProviderId());
                intent.putExtra("userId", inboxModel.getUserId());
                intent.putExtra("userName", inboxModel.getUsername());
                intent.putExtra("messageType", inboxModel.getType());

                context.startActivity(intent);


            });

        }catch (Exception e){
            e.printStackTrace();
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView  userName;
        ImageView profileImgeIv;
        LinearLayout historyContainerLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            profileImgeIv = itemView.findViewById(R.id.profileImgeIv);
            historyContainerLL = itemView.findViewById(R.id.historyContainerLL);



        }
    }
}



