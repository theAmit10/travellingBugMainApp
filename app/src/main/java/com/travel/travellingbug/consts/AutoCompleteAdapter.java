package com.travel.travellingbug.consts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.travel.travellingbug.R;
import com.travel.travellingbug.models.PlaceAutoComplete;

import java.util.List;
import java.util.StringTokenizer;


public class AutoCompleteAdapter extends ArrayAdapter<PlaceAutoComplete> {
    ViewHolder holder;
    Context context;
    List<PlaceAutoComplete> Places;
    private Activity mActivity;

    public AutoCompleteAdapter(Context context, List<PlaceAutoComplete> modelsArrayList, Activity activity) {
        super(context, R.layout.autocomplete_row, modelsArrayList);
        this.context = context;
        this.Places = modelsArrayList;
        this.mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.autocomplete_row, parent, false);
            holder = new ViewHolder();
            holder.name = rowView.findViewById(R.id.place_name);
            holder.location = rowView.findViewById(R.id.place_detail);
            rowView.setTag(holder);

        } else
            holder = (ViewHolder) rowView.getTag();
        /***** Get each Model object from ArrayList ********/
        holder.Place = Places.get(position);


        StringTokenizer st = new StringTokenizer(holder.Place.getPlaceDesc(), ",");
        /************  Set Model values in Holder elements ***********/

        holder.name.setText(st.nextToken());
//        Toast.makeText(getContext(), "name : "+st, Toast.LENGTH_SHORT).show();
//        SharedHelper.putKey(getContext(),"destination_location",st.nextToken());
        String desc_detail = "";
        for (int i = 1; i < st.countTokens(); i++) {
            if (i == st.countTokens() - 1) {
                desc_detail = desc_detail + st.nextToken();
            } else {
                desc_detail = desc_detail + st.nextToken() + ",";
            }
        }
        holder.location.setText(desc_detail);

//        holder.name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(getContext(), ""+holder.name.getText(), Toast.LENGTH_SHORT).show();
////                Toast.makeText(getContext(), ""+holder.location.getText(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), ""+Places.get(position).toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), ""+Places.get(position).getPlaceDesc(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), ""+Places.get(position).getPlaceID(), Toast.LENGTH_SHORT).show();
//                Places.get(position).
//
//            }
//        });
//        Toast.makeText(getContext(), "location : "+desc_detail, Toast.LENGTH_SHORT).show();
        return rowView;
    }

    @Override
    public int getCount() {
        return Places.size();
    }

    class ViewHolder {
        PlaceAutoComplete Place;
        TextView name, location;
    }
}