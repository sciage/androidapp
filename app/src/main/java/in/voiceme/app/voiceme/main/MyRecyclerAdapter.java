package in.voiceme.app.voiceme.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

import in.voiceme.app.voiceme.R;

/**
 * Created by Harish on 7/31/2016.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder> implements OnLikeListener {
    public Context context;
    ArrayList<UserPostDetails> userDetails;
    private LikeButton likeButton;
    private LikeButton HugButton;
    private LikeButton SameButton;

    public MyRecyclerAdapter(Context context, ArrayList<UserPostDetails> userDetails) {
        this.context = context;
        this.userDetails = userDetails;
    }

    // initilise holder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_posts_cardview, null);
        likeButton = (LikeButton) view.findViewById(R.id.list_item_like_button);
        HugButton = (LikeButton) view.findViewById(R.id.list_item_hug_button);
        SameButton = (LikeButton) view.findViewById(R.id.list_item_same_button);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    //bind the data
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.user_name.setText(userDetails.get(position).getUsername());
        holder.feeling.setText(userDetails.get(position).getFeeling());
        holder.category.setText(userDetails.get(position).getCategory());
        holder.timeStamp.setText(userDetails.get(position).getTimeStamp());
        holder.postMessage.setText(userDetails.get(position).getPostMessage());
        holder.postReadMore.setText(userDetails.get(position).getReadMore());
        holder.user_avatar.setImageResource(userDetails.get(position).getAvatarUrl());
        holder.play_button.setImageResource(userDetails.get(position).getPlayButton());

        likeButton.setOnLikeListener(this);
        HugButton.setOnLikeListener(this);
        SameButton.setOnLikeListener(this);

        //listener
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(context, userDetails.get(position).getUsername(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return userDetails.size();
    }

    @Override
    public void liked(LikeButton likeButton) {
        Toast.makeText(context, "Liked!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unLiked(LikeButton likeButton) {

        Toast.makeText(context, "Disliked!", Toast.LENGTH_SHORT).show();
    }
}
