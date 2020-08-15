package com.miguel.go4lunch_p6;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JsonResponse implements Parcelable {


    protected JsonResponse(Parcel in) {
        result = in.createTypedArrayList(Result.CREATOR);
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(result);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JsonResponse> CREATOR = new Creator<JsonResponse>() {
        @Override
        public JsonResponse createFromParcel(Parcel in) {
            return new JsonResponse(in);
        }

        @Override
        public JsonResponse[] newArray(int size) {
            return new JsonResponse[size];
        }
    };

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

        protected static class Result implements Parcelable {

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
        @SerializedName("photos")
        @Expose
        private List<Photo> photo;


            protected Result(Parcel in) {
                formattedAddress = in.readString();
                formattedPhoneNumber = in.readString();
                icon = in.readString();
                internationalPhoneNumber = in.readString();
                name = in.readString();
                if (in.readByte() == 0) {
                    rating = null;
                } else {
                    rating = in.readDouble();
                }
                url = in.readString();
                vicinity = in.readString();
                website = in.readString();
                place_id = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(formattedAddress);
                dest.writeString(formattedPhoneNumber);
                dest.writeString(icon);
                dest.writeString(internationalPhoneNumber);
                dest.writeString(name);
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

            public List<Photo> getPhoto() {
                return photo;
            }

            public void setPhoto(List<Photo> photo) {
                this.photo = photo;
            }
        }
        protected static class Photo implements Parcelable {


            protected Photo(Parcel in) {
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<Photo> CREATOR = new Creator<Photo>() {
                @Override
                public Photo createFromParcel(Parcel in) {
                    return new Photo(in);
                }

                @Override
                public Photo[] newArray(int size) {
                    return new Photo[size];
                }
            };
        }

        protected static class Geometry implements Parcelable {

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

        protected static class Location implements Parcelable{


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

        protected static class OpeningHours implements Parcelable {

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
