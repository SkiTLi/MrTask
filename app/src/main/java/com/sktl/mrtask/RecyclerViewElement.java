package com.sktl.mrtask;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by USER-PC on 05.02.2018.
 */

public class RecyclerViewElement {
/*
public class RecyclerViewElement extends RecyclerView.Adapter {
    private ArrayList<String> items;

    public RecyclerViewElement(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycle_view, parent, false);

        Log.d("AAA", "onCreateViewHolder");
        return new Holder(root);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String item = items.get(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    public static class Holder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;


        public Holder(View itemView2) {
            super(itemView2);

            imageView = (ImageView) itemView2.findViewById(R.id.gallery_imageView);
            textView = (TextView) itemView2.findViewById(R.id.gallery_textView);
        }
    }
   */


}