package com.vytran.restapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder>  {

    private List<Model> dataset;
    private Context context;

    public RecyclerAdapter(List<Model> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
        System.out.println("Recycler" + dataset);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_details, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Model object = this.dataset.get(position);
        holder.postTitle.setText(object.getTitle());
        holder.postContent.setText(object.getContent());
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView postTitle, postContent;

        public ImageViewHolder(View itemView) {
            super (itemView);

            postTitle = itemView.findViewById(R.id.postTitle);
            postContent = itemView.findViewById(R.id.postContent);
        }
    }
}
