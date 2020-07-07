package com.miguel.go4lunch_p6;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.miguel.go4lunch_p6.models.JsonResponseDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ListViewAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable {

    private List<JsonResponseDetails> mJsonResponse;
    private List<JsonResponseDetails> mJsonResponseFull;
    private Context mContext;
    private String positionA;

    public ListViewAdapter(List<JsonResponseDetails> jsonResponse, Context context, String position){
        mJsonResponse = jsonResponse;
        mContext = context;
        positionA = position;
        mJsonResponseFull = new ArrayList<>(jsonResponse);
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
        holder.displayName(mJsonResponse.get(position));
        holder.displayPicture(mJsonResponse.get(position), mContext);
        holder.displayDate(mJsonResponse.get(position));
        holder.displayRating(mJsonResponse.get(position));
        holder.displayAdress(mJsonResponse.get(position));
        holder.displayDistance(mJsonResponse.get(position), positionA);
        holder.setonclicklistener(mJsonResponse.get(position), mContext);


    }

    @Override
    public int getItemCount() {
        return mJsonResponse.size();
    }

    @Override
    public Filter getFilter() {
        return listfilter;
    }
    private Filter listfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<JsonResponseDetails> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mJsonResponseFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(JsonResponseDetails jsonDetails : mJsonResponseFull){
                    if(jsonDetails.getResult().getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(jsonDetails);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mJsonResponse.clear();
            mJsonResponse.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

class MyViewHolder extends RecyclerView.ViewHolder {

    private ImageView mPicture;
    private TextView mName;
    private TextView mAdress;
    private TextView mHours;
    private TextView mDistance;
    private TextView mNumberOfPeople;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ConstraintLayout cell;


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
        cell = itemView.findViewById(R.id.cell);

    }
    void setonclicklistener(final JsonResponseDetails jsonResponse, final Context context){
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantDetailsActivity.class);
                intent.putExtra("json", jsonResponse);
                context.startActivity(intent);
            }
        });
    }

    void displayPicture(JsonResponseDetails jsonResponse, Context context) {
     //   String Urlphoto = jsonResponse.getResult().getIcon();
     //   Glide.with(context).load(Urlphoto).into(mPicture);
        try {
            String photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + jsonResponse.getResult().getPhotos().get(0).getPhotoreference() + "&sensor=false&key=AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU";
            Glide.with(context).load(photo).into(mPicture);
        }catch (NullPointerException e){
               String Urlphoto = jsonResponse.getResult().getIcon();
               Glide.with(context).load(Urlphoto).into(mPicture);
        }
    }

    void displayName(JsonResponseDetails jsonResponse){
        try {
            mName.setText(jsonResponse.getResult().getName().substring(0,33));
        }catch (StringIndexOutOfBoundsException e){
            mName.setText(jsonResponse.getResult().getName());
        }
    }

    void displayDate(JsonResponseDetails jsonResponse) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        try {
            switch (day) {
                case Calendar.SUNDAY:
                    String hours6 = (jsonResponse.getResult().getOpeningHours().getWeekdayText().get(6));
                    String[] hours6split = hours6.split("y");
                    mHours.setText(hours6split[1].trim().substring(2));
                    break;
                case Calendar.MONDAY:
                    String hours0 = (jsonResponse.getResult().getOpeningHours().getWeekdayText().get(0));
                    String[] hours0split = hours0.split("y");
                    mHours.setText(hours0split[1].trim().substring(2));
                    break;
                case Calendar.TUESDAY:
                    String hours1 = (jsonResponse.getResult().getOpeningHours().getWeekdayText().get(1));
                    String[] hours1split = hours1.split("y");
                    mHours.setText(hours1split[1].trim().substring(2));
                    break;
                case Calendar.WEDNESDAY:
                    String hours2 = (jsonResponse.getResult().getOpeningHours().getWeekdayText().get(2));
                    String[] hours2split = hours2.split("y");
                    mHours.setText(hours2split[1].trim().substring(2));
                    break;
                case Calendar.FRIDAY:
                    String hours4 = (jsonResponse.getResult().getOpeningHours().getWeekdayText().get(4));
                    String[] hours4split = hours4.split("y");
                    mHours.setText(hours4split[1].trim().substring(2));
                    break;
                case Calendar.THURSDAY:
                    String hours3 = (jsonResponse.getResult().getOpeningHours().getWeekdayText().get(3));
                    String[] hours3split = hours3.split("y");
                    mHours.setText(hours3split[1].trim().substring(2));
                    break;
                case Calendar.SATURDAY:
                    String hours5 = (jsonResponse.getResult().getOpeningHours().getWeekdayText().get(5));
                    String[] hours5split = hours5.split("y");
                    mHours.setText(hours5split[1].trim().substring(2));
                    break;
            }
        }catch (NullPointerException e){
            mHours.setText("No hours found");
        }

    }

    void displayAdress (JsonResponseDetails jsonResponse) {
        try {
            mAdress.setText(jsonResponse.getResult().getVicinity().substring(0,40));
        }catch (StringIndexOutOfBoundsException e){
            mAdress.setText(jsonResponse.getResult().getVicinity());
        }
    }

    void displayRating (JsonResponseDetails jsonResponse){
        try {
            Double rating = jsonResponse.getResult().getRating();
            Double ratingstar = rating / 5 * 3;
            long finalnote = Math.round(ratingstar);
            Log.i("rating ==", "" + finalnote);
            if (finalnote == 1) {
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
            } else if (finalnote == 2) {
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.INVISIBLE);

            } else if (finalnote == 3) {
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
            } else {
                star1.setVisibility(View.INVISIBLE);
                star2.setVisibility(View.INVISIBLE);
                star3.setVisibility(View.INVISIBLE);
            }
        }catch (NullPointerException ignored) { }
    }

    void displayDistance (JsonResponseDetails jsonResponseDetails, String position) {
        Location locationB = new Location("B");
        locationB.setLatitude(jsonResponseDetails.getResult().getGeometry().getLocation().getLat());
        locationB.setLongitude(jsonResponseDetails.getResult().getGeometry().getLocation().getLng());
        String[] positionA =  position.split(",");
        double latitudeA = Double.parseDouble(positionA[0]);
        double longitudeA = Double.parseDouble(positionA[1]);
        Location locationA = new Location("A");
        locationA.setLongitude(longitudeA);
        locationA.setLatitude(latitudeA);
        double distance = locationA.distanceTo(locationB);
        double distanceroundded = Math.round(distance);
        String distancefinal = String.valueOf(distanceroundded);
        String [] finaldistance = distancefinal.split("\\.");
        mDistance.setText(String.format("%sm", finaldistance[0]));
    }

    void displayPhoto (JsonResponseDetails jsonResponseDetails, String position){

    }
}
