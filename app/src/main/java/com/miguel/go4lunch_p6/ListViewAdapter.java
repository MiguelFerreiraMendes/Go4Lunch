package com.miguel.go4lunch_p6;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private JsonResponse mJsonResponse;
    private Context mContext;

    public ListViewAdapter(JsonResponse jsonResponse, Context context){
        mJsonResponse = jsonResponse;
        mContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.listviewcell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.displayName(mJsonResponse, position);
        holder.displayPicture(mJsonResponse, mContext,position);
        holder.displayDate(mJsonResponse, position);
        holder.displayRating(mJsonResponse, position);
        holder.displayAdress(mJsonResponse, position);


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    private ImageButton mPicture;
    private TextView mName;
    private TextView mAdress;
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
        mAdress = itemView.findViewById(R.id.textView);
        mHours = itemView.findViewById(R.id.hours);
        mDistance = itemView.findViewById(R.id.textView2);
        mNumberOfPeople = itemView.findViewById(R.id.numberofpeople);
        star1 = itemView.findViewById(R.id.toggleButton);
        star2 = itemView.findViewById(R.id.toggleButton2);
        star3 = itemView.findViewById(R.id.toggleButton3);
    }

    void displayPicture(JsonResponse jsonResponse, Context context, int index) {
        String Urlphoto = jsonResponse.getResult().get(index).getIcon();
        Glide.with(context).load(Urlphoto).into(mPicture);


    }

    void displayName(JsonResponse jsonResponse, int index){
        mName.setText(jsonResponse.getResult().get(index).getName());

    }

    void displayDate(JsonResponse jsonResponse, int index) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                mHours.setText(jsonResponse.getResult().get(index).getOpeningHours().getWeekdayText().get(6));
                break;
            case Calendar.MONDAY:
                mHours.setText(jsonResponse.getResult().get(index).getOpeningHours().getWeekdayText().get(0));
                break;
            case Calendar.TUESDAY:
                mHours.setText(jsonResponse.getResult().get(index).getOpeningHours().getWeekdayText().get(1));
                break;
            case Calendar.WEDNESDAY:
                mHours.setText(jsonResponse.getResult().get(index).getOpeningHours().getWeekdayText().get(2));
                break;
            case Calendar.FRIDAY:
                mHours.setText(jsonResponse.getResult().get(index).getOpeningHours().getWeekdayText().get(4));
                break;
            case Calendar.THURSDAY:
                mHours.setText(jsonResponse.getResult().get(index).getOpeningHours().getWeekdayText().get(3));
                break;
            case Calendar.SATURDAY:
                mHours.setText(jsonResponse.getResult().get(index).getOpeningHours().getWeekdayText().get(5));
                break;
        }

    }

    void displayAdress (JsonResponse jsonResponse, int index) {
        mAdress.setText(jsonResponse.getResult().get(index).getFormattedAddress());

    }

    void displayRating (JsonResponse jsonResponse, int index){
        Double rating = jsonResponse.getResult().get(index).getRating();
        Double ratingstar = rating/5*3;
        DecimalFormat df = new DecimalFormat();
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.format(ratingstar);
    }
}
