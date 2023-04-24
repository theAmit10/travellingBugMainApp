package com.pinkcar.providers.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.pinkcar.providers.R;
import com.pinkcar.providers.models.IntroModel;

import java.util.ArrayList;

public class IntroAdapter extends PagerAdapter {

    Context context;
    ArrayList<IntroModel> list;

    public IntroAdapter(Context context, ArrayList<IntroModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }



    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View sliderLayout = inflater.inflate(R.layout.design_intro_view_pager,null);

        ImageView featured_image = sliderLayout.findViewById(R.id.my_featured_image);
        TextView my_caption_title = sliderLayout.findViewById(R.id.my_caption_title);
        TextView the_caption_Title_description = sliderLayout.findViewById(R.id.the_caption_Title_description);

        featured_image.setImageResource(list.get(position).getFeatured_image());
        my_caption_title.setText(list.get(position).getThe_caption_Title());
        the_caption_Title_description.setText(list.get(position).getThe_caption_Title_description());

        container.addView(sliderLayout);
        return sliderLayout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }



    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
