package in.voiceme.app.voiceme.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.RecyclerViewDetails.UserHugCounterActivity;
import in.voiceme.app.voiceme.RecyclerViewDetails.UserLikeCounterActivity;
import in.voiceme.app.voiceme.RecyclerViewDetails.UserListenCounterActivity;
import in.voiceme.app.voiceme.RecyclerViewDetails.UserSameCounterActivity;


/**
 * Created by Harish on 7/31/2016.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView user_avatar;
    ImageView play_button;
    TextView user_name;
    TextView isPost;
    TextView feeling;
    TextView category;
    TextView timeStamp;
    TextView postMessage;
    TextView postReadMore;

    ImageView likeCounterImage;
    TextView likeCounterInt;
    ImageView hugCounterImage;
    TextView hugCounterInt;
    ImageView sameCounterImage;
    TextView sameCounterInt;
    ImageView listenCounterImage;
    TextView listenCounterInt;


    ItemClickListener listener;


    public MyViewHolder(View itemView) {
        super(itemView);

        likeCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_like);
        likeCounterInt = (TextView) itemView.findViewById(R.id.post_likes_counter);
        hugCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_hug);
        hugCounterInt = (TextView) itemView.findViewById(R.id.post_hugs_counter);
        sameCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_same);
        sameCounterInt = (TextView) itemView.findViewById(R.id.post_same_counter);
        listenCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_listen);
        listenCounterInt = (TextView) itemView.findViewById(R.id.post_listen_counter);

        user_name = (TextView) itemView.findViewById(R.id.list_item_post_userNickName);
        isPost = (TextView) itemView.findViewById(R.id.list_item_post_is);
        feeling = (TextView) itemView.findViewById(R.id.list_item_posts_feeling);
        category = (TextView) itemView.findViewById(R.id.list_item_posts_category);
        timeStamp = (TextView) itemView.findViewById(R.id.list_item_posts_timeStamp);
        postMessage = (TextView) itemView.findViewById(R.id.list_item_posts_message);
        postReadMore = (TextView) itemView.findViewById(R.id.list_item_posts_read_more);
        user_avatar = (ImageView) itemView.findViewById(R.id.list_item_posts_avatar);
        play_button = (ImageView) itemView.findViewById(R.id.list_item_posts_play_button);

        itemView.setOnClickListener(this);
        likeCounterImage.setOnClickListener(this);
        likeCounterInt.setOnClickListener(this);
        hugCounterImage.setOnClickListener(this);
        hugCounterInt.setOnClickListener(this);
        sameCounterImage.setOnClickListener(this);
        sameCounterInt.setOnClickListener(this);
        listenCounterImage.setOnClickListener(this);
        listenCounterInt.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        this.listener.onItemClick(view, getLayoutPosition());

        int itemId = view.getId();

        if (itemId == R.id.emoji_above_like || itemId == R.id.post_likes_counter) {
            Intent intent = new Intent(view.getContext(), UserLikeCounterActivity.class);
            view.getContext().startActivity(intent);
        } else if (itemId == R.id.emoji_above_hug || itemId == R.id.post_hugs_counter) {
            Intent intent = new Intent(view.getContext(), UserHugCounterActivity.class);
            view.getContext().startActivity(intent);
        } else if (itemId == R.id.emoji_above_same || itemId == R.id.post_same_counter) {
            Intent intent = new Intent(view.getContext(), UserSameCounterActivity.class);
            view.getContext().startActivity(intent);
        } else if (itemId == R.id.emoji_above_listen || itemId == R.id.post_listen_counter) {
            Intent intent = new Intent(view.getContext(), UserListenCounterActivity.class);
            view.getContext().startActivity(intent);
        }

    }

}
