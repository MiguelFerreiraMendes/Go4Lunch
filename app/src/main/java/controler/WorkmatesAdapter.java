package controler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.miguel.go4lunch_p6.R;
import com.miguel.go4lunch_p6.RestaurantDetailsActivity;
import com.miguel.go4lunch_p6.models.User;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkmatesAdapter extends RecyclerView.Adapter<MyViewHolderWorkmates> {

    private List<User> mUserList;
    private Context mContext;
    private String mString;

    public WorkmatesAdapter (List<User> idofpeoplea, Context context, String s){
        mUserList = idofpeoplea;
        mContext = context;
        mString = s;
    }
    @NonNull
    @Override
    public MyViewHolderWorkmates onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.workmatescell, parent, false);
        return new MyViewHolderWorkmates(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolderWorkmates holder, final int position) {
        holder.displaypicture(mUserList.get(position), mContext);
        if (mString.equals("a")) {
            holder.settext(mUserList.get(position), mContext);
        }
        if (mString.equals("b")) {
            if (mUserList.get(position).getRestaurantInteressed().equals("false")){
                holder.settextnull(mUserList.get(position), mContext);
            }else {
                holder.settextRestaurant(mUserList.get(position), mUserList.get(position).getRestaurantInteressed(), mContext);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), RestaurantDetailsActivity.class);
                        intent.putExtra("place_id", mUserList.get(position).getIdOfRestaurantInteressed());
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}

class MyViewHolderWorkmates extends RecyclerView.ViewHolder {

    private ImageView mPicture;
    private TextView mtext;


    public MyViewHolderWorkmates(View itemView) {
        super(itemView);

        mPicture = itemView.findViewById(R.id.imageViewworkmate);
        mtext = itemView.findViewById(R.id.textviewworkmate);
    }

    void displaypicture(User user, final Context context){

        try {
            Glide.with(context).load(user.getUrlPicture()).circleCrop().into(mPicture);
        }catch (NullPointerException e){
        }

        if (user.getUrlPicture() == null){
            mPicture.setImageResource(R.drawable.ic_baseline_person_24);
        }
    }

    void settext(User user, Context context) {
        String username = user.getUsername();
        String[] separated = username.split(" ");
        String usernamefinal = separated[0] + context.getString(R.string.isjoining);
        mtext.setText(usernamefinal);
    }

    void settextnull(User user, Context context) {
        String username = user.getUsername();
        String[] separated = username.split(" ");
        String usernamefinal = separated[0] + context.getString(R.string.hasntdecided);
        mtext.setTypeface(null, Typeface.ITALIC);
        mtext.setTextColor(context.getResources().getColor(R.color.quantum_grey400));
        mtext.setText(usernamefinal);
    }

    public void settextRestaurant(User user, String restaurantName, Context context) {
        String username = user.getUsername();
        String[] separated = username.split(" ");
        String usernamefinal = separated[0] + context.getString(R.string.iseatingat) + restaurantName;
        mtext.setText(usernamefinal);
    }
}
