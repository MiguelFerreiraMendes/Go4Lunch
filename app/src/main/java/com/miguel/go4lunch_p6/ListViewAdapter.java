package com.miguel.go4lunch_p6;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    private ImageButton mPicture;
    private TextView mName;
    private TextView mResume;
    private TextView mHours;
    private TextView mDistance;
    private TextView mNumberOfPeople;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;


    public MyViewHolder(View itemView) {
        super(itemView);

        mPicture = itemView.findViewById(R.id.imageView);
        mName = itemView.findViewById(R.id.textView3);
        mResume = itemView.findViewById(R.id.textView);
        mHours = itemView.findViewById(R.id.hours);
        mDistance = itemView.findViewById(R.id.textView2);
        mNumberOfPeople = itemView.findViewById(R.id.numberofpeople);
        star1 = itemView.findViewById(R.id.toggleButton);
        star2 = itemView.findViewById(R.id.toggleButton2);
        star3 = itemView.findViewById(R.id.toggleButton3);
    }

    void displayPicture(JsonResponse jsonResponse, Context context) {


    }

    void displayName(JsonResponse jsonResponse){

    }

    void displayDate(JsonResponse jsonResponse) {

    }

    void displayResume (JsonResponse jsonResponse) {

    }
}
