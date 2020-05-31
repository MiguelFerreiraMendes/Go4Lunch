package com.miguel.go4lunch_p6;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListViewFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;

    public static ListViewFragment newInstance(/*JsonResponse jsonResponse*/) {
        ListViewFragment frag1 = new ListViewFragment();
        Bundle arg = new Bundle();
        arg.getParcelable("json");
        frag1.setArguments(arg);
        return (frag1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.listviewfragment, container, false);
        //JsonResponse jsonResponse = arg.getParcelable("jsonResponse");
        Log.i("json", "json dans le fragment listview = " + getArguments().getParcelable("json"));
        JsonResponse jsonResponse = getArguments().getParcelable("json");
        this.progressBar = result.findViewById(R.id.listview_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        if (jsonResponse != null) {
            Log.i("json", "" + jsonResponse.getResult().get(0).getName());
            mRecyclerView = result.findViewById(R.id.recycleview_view_listview);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            updateRecycleView(jsonResponse);
        }

        return result;
    }

    public void updateRecycleView (JsonResponse jsonResponse) {
        ListViewAdapter mondapteur;
        mondapteur = new ListViewAdapter(jsonResponse, getContext());
        mRecyclerView.setAdapter(mondapteur);
        progressBar.setVisibility(View.INVISIBLE);
    }
}