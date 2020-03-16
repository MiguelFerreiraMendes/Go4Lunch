package com.miguel.go4lunch_p6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WorkmatesFragment extends Fragment {


    private ProgressBar progressBar;

    public static WorkmatesFragment newInstance() {
        WorkmatesFragment frag1 = new WorkmatesFragment();
        return (frag1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.workmates, container, false);
        this.progressBar = result.findViewById(R.id.workmates_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = result.findViewById(R.id.recycleview_view_workmates);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));


        return result;
    }
}