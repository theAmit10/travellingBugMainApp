package com.travel.travellingbug.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.SearchHistoryModel;

import java.util.ArrayList;

public class SearchHistoryAdpater extends RecyclerView.Adapter<SearchHistoryAdpater.ViewHolder> {

    Context context;
    ArrayList<SearchHistoryModel> list;

    int selectedPosition=-1;

    SearchHistoryItemClickListener searchHistoryItemClickListener;

    public SearchHistoryAdpater(Context context, ArrayList<SearchHistoryModel> list) {
        this.context = context;
        this.list = list;
    }

    public SearchHistoryAdpater(Context context, ArrayList<SearchHistoryModel> list, SearchHistoryItemClickListener searchHistoryItemClickListener) {
        this.context = context;
        this.list = list;
        this.searchHistoryItemClickListener = searchHistoryItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.design_search_history,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SearchHistoryModel searchHistoryModel = list.get(position);



        holder.searchHistoryAddress.setText(searchHistoryModel.getFromAddress() + " -> " +searchHistoryModel.getDestAddress());
        holder.searchHistoryPassenger.setText(searchHistoryModel.getPassenger());

        holder.searchHistoryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();
                // call listener
                searchHistoryItemClickListener.onClick(position,searchHistoryModel);
//                System.out.println("FROM ADAPTER ADDRESS: "+searchHistoryModel.getFromAddress());
                // update position
                selectedPosition=position;
                // notify
                notifyDataSetChanged();


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView searchHistoryPassenger, searchHistoryAddress;
        LinearLayout searchHistoryContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            searchHistoryAddress = itemView.findViewById(R.id.searchHistoryAddress);
            searchHistoryPassenger = itemView.findViewById(R.id.searchHistoryPassenger);
            searchHistoryContainer = itemView.findViewById(R.id.searchHistoryContainer);
        }
    }
}
