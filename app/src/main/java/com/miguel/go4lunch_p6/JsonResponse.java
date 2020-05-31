package com.miguel.go4lunch_p6;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class JsonResponse implements Parcelable {



    public List<Result> getResult() {
        return  result;
    }

    public String getStatus() {
        return status;
    }

    @SerializedName("results")
    @Expose
    private List<Result> result;

    @SerializedName("status")
    @Expose
    private String status;

    protected JsonResponse(Parcel in) {
        this.result = new ArrayList<Result>();
        in.readList(this.result, Result.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(result);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<JsonResponse> CREATOR = new Parcelable.Creator<JsonResponse>() {
        @Override
        public JsonResponse createFromParcel(Parcel in) {
            return new JsonResponse(in);
        }

        @Override
        public JsonResponse[] newArray(int size) {
            return new JsonResponse[size];
        }
    };


        protected static class Result {

        @SerializedName("formatted_address")
        @Expose
        private String formattedAddress;
        @SerializedName("formatted_phone_number")
        @Expose
        private String formattedPhoneNumber;
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("international_phone_number")
        @Expose
        private String internationalPhoneNumber;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;
        @SerializedName("rating")
        @Expose
        private Double rating;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("vicinity")
        @Expose
        private String vicinity;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("place_id")
        @Expose
        private String place_id;

        public String getPlace_id() {
            return place_id;
        }

        public String getWebsite() {
            return website;
        }

        public String getFormattedAddress() {
            return formattedAddress;
        }

        public String getFormattedPhoneNumber() {
            return formattedPhoneNumber;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public String getIcon() {
            return icon;
        }

        public String getInternationalPhoneNumber() {
            return internationalPhoneNumber;
        }

        public String getName() {
            return name;
        }

        public OpeningHours getOpeningHours() {
            return openingHours;
        }

        public Double getRating() {
            return rating;
        }

        public String getUrl() {
            return url;
        }

        public String getVicinity() {
            return vicinity;
        }

    }

        protected static class Geometry {

            @SerializedName("location")
            @Expose
            private Location location;

            public Location getLocation() {
                return location;
            }
        }

        protected static class Location {


            @SerializedName("lat")
            @Expose
            private Double lat;
            @SerializedName("lng")
            @Expose
            private Double lng;

            public Double getLat() {
                return lat;
            }

            public Double getLng() {
                return lng;
            }
        }

        protected static class OpeningHours {

            @SerializedName("open_now")
            @Expose
            private Boolean openNow;
            @SerializedName("weekday_text")
            @Expose
            private List<String> weekdayText;

            public Boolean getOpenNow() {
                return openNow;
            }

            public List<String> getWeekdayText() {
                return weekdayText;
            }
        }

}
