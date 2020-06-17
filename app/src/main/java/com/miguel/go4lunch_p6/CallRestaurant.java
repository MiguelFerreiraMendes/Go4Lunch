package com.miguel.go4lunch_p6;

import android.util.Log;

import com.miguel.go4lunch_p6.models.JsonResponseDetails;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallRestaurant {

    private static final String API_KEY = "AIzaSyAfGC10zfgqg54n-hoMT1GhdoJMWFbUcxU" ;
    private static CallService callInformationService = null;

    public static CallService getCallInformationService(){
        if(callInformationService == null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(loggingInterceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com")
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            callInformationService = retrofit.create(CallService.class);
        }
        return  callInformationService;
    }

    public interface Callbacks {
        void onResponse(@Nullable JsonResponse details);
        void onFailure();
    }

    public interface CallbacksDetails {
        void onResponse(@Nullable JsonResponseDetails details);
        void onFailure();
    }

    public static void fetchRestaurantDetails(CallbacksDetails callbacks, String place_id){

        final WeakReference<CallbacksDetails> callbacksWeakReference = new WeakReference<CallbacksDetails>(callbacks);

        Call<JsonResponseDetails> call = getCallInformationService().getRestaurantDetails(API_KEY, place_id);
        call.enqueue(new Callback<JsonResponseDetails>() {

            @Override
            public void onResponse(Call<JsonResponseDetails> call, Response<JsonResponseDetails> response) {
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
                    Log.e("sucess", response.body().getStatus());
            }

            @Override
            public void onFailure(Call<JsonResponseDetails> call, Throwable t) {
                Log.e("failure", "throwable", t);
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    public static void fetchRestaurant(Callbacks callbacks, String location){

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        Call<JsonResponse> call = getCallInformationService().getRestaurant(API_KEY, "restaurant", 300, location);
        call.enqueue(new Callback<JsonResponse>() {

            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
                Log.e("sucess", response.body().getStatus());
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.e("failure", "throwable", t);
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
