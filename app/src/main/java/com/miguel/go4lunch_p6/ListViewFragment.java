package com.miguel.go4lunch_p6;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miguel.go4lunch_p6.models.JsonResponseDetails;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import controler.ListViewAdapter;


public class ListViewFragment extends Fragment implements CallRestaurant.CallbacksDetails {

    private RecyclerView mRecyclerView;
    private ArrayList<JsonResponseDetails> mJsonResponseDetails = new ArrayList<>();
    private String position;
    OnAdapteurPass mAdapteurPass;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAdapteurPass = (OnAdapteurPass) context;
    }

    public static ListViewFragment newInstance() {
        ListViewFragment frag1 = new ListViewFragment();
        Bundle arg = new Bundle();
        arg.getParcelable("json");
        frag1.setArguments(arg);
        return (frag1);
    }
    public interface OnAdapteurPass {
        public void onAdapteurPass(ListViewAdapter adapteur);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.listviewfragment, container, false);
        JsonResponse jsonResponse = getArguments().getParcelable("json");
        position = getArguments().getString("position");

        if (jsonResponse != null) {
            int size = jsonResponse.getResult().size();
        mRecyclerView = result.findViewById(R.id.recycleview_view_listview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            for (int i = 0; i < size; i++){
                CallRestaurant.fetchRestaurantDetails(this, jsonResponse.getResult().get(i).getPlace_id());
            }
        }

        return result;
    }

    public void updateRecycleView () {
        ListViewAdapter mondapteur = new ListViewAdapter(mJsonResponseDetails, getContext(), position);
        passData(mondapteur);
        mRecyclerView.setAdapter(mondapteur);
    }
    public void passData(ListViewAdapter adapter){
        mAdapteurPass.onAdapteurPass(adapter);
    }

    @Override
    public void onResponse(@Nullable JsonResponseDetails details) {
        mJsonResponseDetails.add(details);
        updateRecycleView();
    }

    @Override
    public void onFailure() {

    }
}