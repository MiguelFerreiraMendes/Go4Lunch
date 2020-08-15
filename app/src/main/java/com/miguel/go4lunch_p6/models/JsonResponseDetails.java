package com.miguel.go4lunch_p6.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonResponseDetails implements Parcelable {


    public JsonResponseDetails(Parcel in) {
        result = in.readParcelable(Result.class.getClassLoader());
        status = in.readString();
    }

    public JsonResponseDetails() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(result, flags);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JsonResponseDetails> CREATOR = new Creator<JsonResponseDetails>() {
        @Override
        public JsonResponseDetails createFromParcel(Parcel in) {
            return new JsonResponseDetails(in);
        }

        @Override
        public JsonResponseDetails[] newArray(int size) {
            return new JsonResponseDetails[size];
        }
    };

    public Result getResult() {
        return  result;
    }

    public String getStatus() {
        return status;
    }

    @SerializedName("result")
    @Expose
    private Result result;

    @SerializedName("status")
    @Expose
    private String status;



    public static class Result implements Parcelable {

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

        protected Result(Parcel in) {
            formattedAddress = in.readString();
            formattedPhoneNumber = in.readString();
            geometry = in.readParcelable(Geometry.class.getClassLoader());
            icon = in.readString();
            internationalPhoneNumber = in.readString();
            name = in.readString();
            openingHours = in.readParcelable(OpeningHours.class.getClassLoader());
            if (in.readByte() == 0) {
                rating = null;
            } else {
                rating = in.readDouble();
            }
            url = in.readString();
            vicinity = in.readString();
            website = in.readString();
            place_id = in.readString();
            photos = in.createTypedArrayList(photo.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(formattedAddress);
            dest.writeString(formattedPhoneNumber);
            dest.writeParcelable(geometry, flags);
            dest.writeString(icon);
            dest.writeString(internationalPhoneNumber);
            dest.writeString(name);
            dest.writeParcelable(openingHours, flags);
            if (rating == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(rating);
            }
            dest.writeString(url);
            dest.writeString(vicinity);
            dest.writeString(website);
            dest.writeString(place_id);
            dest.writeTypedList(photos);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };

        public List<photo> getPhotos() {
            return photos;
        }


        public void setPhotos(List<photo> photos) {
            this.photos = photos;
        }

        @SerializedName("photos")
        @Expose
        private List<photo> photos;

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

    public static class photo implements Parcelable {

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getPhotoreference() {
            return photoreference;
        }

        public void setPhotoreference(String photoreference) {
            this.photoreference = photoreference;
        }

        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("width")
        @Expose
        private String width;
        @SerializedName("photo_reference")
        @Expose
        private String photoreference;

        protected photo(Parcel in) {
            height = in.readString();
            width = in.readString();
            photoreference = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(height);
            dest.writeString(width);
            dest.writeString(photoreference);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<photo> CREATOR = new Creator<photo>() {
            @Override
            public photo createFromParcel(Parcel in) {
                return new photo(in);
            }

            @Override
            public photo[] newArray(int size) {
                return new photo[size];
            }
        };
    }

    public static class Geometry implements Parcelable {

        @SerializedName("location")
        @Expose
        private Location location;

        protected Geometry(Parcel in) {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
            @Override
            public Geometry createFromParcel(Parcel in) {
                return new Geometry(in);
            }

            @Override
            public Geometry[] newArray(int size) {
                return new Geometry[size];
            }
        };

        public Location getLocation() {
            return location;
        }
    }

    public static class Location implements Parcelable{


        @SerializedName("lat")
        @Expose
        private Double lat;
        @SerializedName("lng")
        @Expose
        private Double lng;

        protected Location(Parcel in) {
            if (in.readByte() == 0) {
                lat = null;
            } else {
                lat = in.readDouble();
            }
            if (in.readByte() == 0) {
                lng = null;
            } else {
                lng = in.readDouble();
            }
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (lat == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(lat);
            }
            if (lng == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(lng);
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Location> CREATOR = new Creator<Location>() {
            @Override
            public Location createFromParcel(Parcel in) {
                return new Location(in);
            }

            @Override
            public Location[] newArray(int size) {
                return new Location[size];
            }
        };

        public Double getLat() {
            return lat;
        }

        public Double getLng() {
            return lng;
        }
    }

    public static class OpeningHours implements Parcelable {

        @SerializedName("open_now")
        @Expose
        private Boolean openNow;
        @SerializedName("weekday_text")
        @Expose
        private List<String> weekdayText;

        protected OpeningHours(Parcel in) {
            byte tmpOpenNow = in.readByte();
            openNow = tmpOpenNow == 0 ? null : tmpOpenNow == 1;
            weekdayText = in.createStringArrayList();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (openNow == null ? 0 : openNow ? 1 : 2));
            dest.writeStringList(weekdayText);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<OpeningHours> CREATOR = new Creator<OpeningHours>() {
            @Override
            public OpeningHours createFromParcel(Parcel in) {
                return new OpeningHours(in);
            }

            @Override
            public OpeningHours[] newArray(int size) {
                return new OpeningHours[size];
            }
        };

        public Boolean getOpenNow() {
            return openNow;
        }

        public List<String> getWeekdayText() {
            return weekdayText;
        }
    }

}
