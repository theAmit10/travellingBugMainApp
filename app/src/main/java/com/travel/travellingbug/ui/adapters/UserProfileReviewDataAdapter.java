package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.travel.travellingbug.R;
import com.travel.travellingbug.helper.URLHelper;
import com.travel.travellingbug.models.UserProfileReviewDataModel;

import java.util.ArrayList;

public class UserProfileReviewDataAdapter extends RecyclerView.Adapter<UserProfileReviewDataAdapter.ViewHolder> {

    Context context;
    ArrayList<UserProfileReviewDataModel> list;

    public UserProfileReviewDataAdapter(Context context, ArrayList<UserProfileReviewDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_driver_profile_reviews,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserProfileReviewDataModel userProfileReviewDataModel = list.get(position);
        try {
            holder.nametv.setText(userProfileReviewDataModel.getFirst_name());
            if(userProfileReviewDataModel.getUser_comment().equalsIgnoreCase("") || userProfileReviewDataModel.getUser_comment().equalsIgnoreCase("null") || userProfileReviewDataModel.getUser_comment() == null){
                holder.commenttv.setText("");
            }else {
                holder.commenttv.setText(userProfileReviewDataModel.getUser_comment());
            }

            holder.timeTv.setText(userProfileReviewDataModel.getTime());

            holder.listitemrating.setRating(Float.parseFloat(userProfileReviewDataModel.getUser_rating()));
            Picasso.get().load(URLHelper.BASE + "storage/app/public/" + userProfileReviewDataModel.getAvatar())
                    .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(holder.profileImgeIv);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImgeIv;
        TextView nametv,commenttv,timeTv;
        RatingBar listitemrating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImgeIv =  itemView.findViewById(R.id.profileImgeIv);
            nametv =  itemView.findViewById(R.id.nametv);
            commenttv =  itemView.findViewById(R.id.commenttv);
            listitemrating =  itemView.findViewById(R.id.listitemrating);
            timeTv =  itemView.findViewById(R.id.timeTv);
        }
    }
}
