package com.miguel.go4lunch_p6;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




    public class JsonResponse {

        @SerializedName("result")
        @Expose
        private Result result;

        @SerializedName("status")
        @Expose
        private String status;

        public Result getResult() {
            return result;
        }

        public String getStatus() {
            return status;
        }

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
