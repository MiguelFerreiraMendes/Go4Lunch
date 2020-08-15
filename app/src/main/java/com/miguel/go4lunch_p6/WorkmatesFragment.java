package com.miguel.go4lunch_p6;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguel.go4lunch_p6.models.User;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import controler.WorkmatesAdapter;

public class WorkmatesFragment extends Fragment {

    private String myuserID = FirebaseAuth.getInstance().getCurrentUser().getUid();



    public static WorkmatesFragment newInstance() {
        WorkmatesFragment frag1 = new WorkmatesFragment();
        return (frag1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.workmates, container, false);
        setWorkmates();

        return result;
    }

    private void setWorkmates () {
        FirebaseFirestore.getInstance().collection("userschoices").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> list = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        list.add(user);
                    }
                    setrecyclerview(list, "b");
                }

            }
        });
    }

    private void setrecyclerview(List<User> listuserinteressed, String type) {
    try {
        if (listuserinteressed != null) {
            for (int i = 0; i < listuserinteressed.size(); i++) {
                if (listuserinteressed.get(i).getId().equals(myuserID)) {
                    listuserinteressed.remove(i);
                }
            }

            RecyclerView recyclerView = getView().findViewById(R.id.recycleview_view_workmates);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            WorkmatesAdapter mondapteur = new WorkmatesAdapter(listuserinteressed, getContext(), type);
            recyclerView.setAdapter(mondapteur);

        }
    }catch (NullPointerException ignored){}
    }
}
