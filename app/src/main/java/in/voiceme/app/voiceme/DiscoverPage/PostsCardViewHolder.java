package in.voiceme.app.voiceme.DiscoverPage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.VoicemeApplication;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.services.LikesResponse;
import in.voiceme.app.voiceme.services.PostsModel;
import rx.android.schedulers.AndroidSchedulers;

public abstract class PostsCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener {
    //Imageview for avatar and play pause button
    public ImageView user_avatar;
    public ImageView play_button;

    //username, feeling and category
    public TextView user_name;
    public TextView isPost;
    public TextView feeling;
    public TextView category;

    //post data
    public TextView timeStamp;
    public TextView postMessage;
    public TextView postReadMore;
    public TextView post_audio_duration;

    //counter numbers
    public TextView like_counter;
    public TextView hug_counter;
    public TextView same_counter;
    public TextView post_comments;
    public TextView post_listen;

    //emoji for like, hug and same above
    public ImageView likeCounterImage;
    public ImageView hugCounterImage;
    public ImageView sameCounterImage;
    public ImageView commentCounterImage;
    public ImageView listenCounterImage;

    //animated buttons
    public LikeButton likeButtonMain, HugButtonMain, SameButtonMain;

    public View parent_row;

    protected PostsModel dataItem;

    public PostsCardViewHolder(View itemView) {
        super(itemView);
        //Imageview for avatar and play pause button
        user_avatar = (ImageView) itemView.findViewById(R.id.list_item_posts_avatar);
        play_button = (ImageView) itemView.findViewById(R.id.list_item_posts_play_button);

        //username, feeling and category
        user_name = (TextView) itemView.findViewById(R.id.list_item_post_userNickName);
        isPost = (TextView) itemView.findViewById(R.id.list_item_post_is);
        feeling = (TextView) itemView.findViewById(R.id.list_item_posts_feeling);
        category = (TextView) itemView.findViewById(R.id.list_item_posts_category);

        //post data
        post_audio_duration = (TextView) itemView.findViewById(R.id.list_item_posts_duration_count);
        timeStamp = (TextView) itemView.findViewById(R.id.list_item_posts_timeStamp);
        postMessage = (TextView) itemView.findViewById(R.id.list_item_posts_message);
        postReadMore = (TextView) itemView.findViewById(R.id.list_item_posts_read_more);

        //counter numbers
        like_counter = (TextView) itemView.findViewById(R.id.post_likes_counter);
        hug_counter = (TextView) itemView.findViewById(R.id.post_hugs_counter);
        same_counter = (TextView) itemView.findViewById(R.id.post_same_counter);
        post_comments = (TextView) itemView.findViewById(R.id.post_comment_counter);
        post_listen = (TextView) itemView.findViewById(R.id.post_listen_counter);

        //emoji for like, hug and same above
        likeCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_like);
        hugCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_hug);
        sameCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_same);
        commentCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_comment);
        listenCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_listen);

        //animated buttons
        likeButtonMain = (LikeButton) itemView.findViewById(R.id.list_item_like_button);
        HugButtonMain = (LikeButton) itemView.findViewById(R.id.list_item_hug_button);
        SameButtonMain = (LikeButton) itemView.findViewById(R.id.list_item_same_button);

        parent_row = (View) itemView.findViewById(R.id.parent_row);

        //OnClickListeners
        likeButtonMain.setOnLikeListener(this);
        HugButtonMain.setOnLikeListener(this);
        SameButtonMain.setOnLikeListener(this);

        like_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCounterClicked(v);
            }
        });
        hug_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hugCounterClicked(v);
            }
        });
        same_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameCounterClicked(v);
            }
        });
        post_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsCounterClicked(v);
            }
        });
        post_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenCounterClicked(v);
            }
        });

        likeCounterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCounterClicked(v);
            }
        });
        hugCounterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hugCounterClicked(v);
            }
        });
        sameCounterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameCounterClicked(v);
            }
        });
        commentCounterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentsCounterClicked(v);
            }
        });
        listenCounterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenCounterClicked(v);
            }
        });

        parent_row.setOnClickListener(this);
    }

    protected void listenCounterClicked(View v) {
    }

    protected void commentsCounterClicked(View v) {
    }

    protected void likeCounterClicked(View v) {
    }

    protected void hugCounterClicked(View v) {
    }

    protected void sameCounterClicked(View v) {
    }

    @Override
    public void onClick(View v) {
    }

    public void bind(PostsModel dataItem) {
        this.dataItem = dataItem;

        user_name.setText(dataItem.getUserNicName());
        feeling.setText(dataItem.getEmotions());
        category.setText(dataItem.getCategory());
        timeStamp.setText(dataItem.getPostTime());
        postMessage.setText(dataItem.getTextStatus());
        post_comments.setText(String.valueOf(dataItem.getComments()));
        like_counter.setText(String.valueOf(dataItem.getLikes()));
        hug_counter.setText(String.valueOf(dataItem.getHug()));
        same_counter.setText(String.valueOf(dataItem.getSame()));
        post_listen.setText(String.valueOf(dataItem.getListen()));

        if (!dataItem.getAvatarPics().equals("")) {
            Picasso.with(itemView.getContext())
                    .load(dataItem.getAvatarPics())
                    .resize(75, 75)
                    .centerInside()
                    .into(user_avatar);
        }

        play_button.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
    }

    protected void sendLikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        int userId = application.getAuth().getUser().getIdUserName();
        String postId = dataItem.getIdPosts();
        application.getWebService().likes(userId, postId, like, hug, same, listen)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LikesResponse>() {
                    @Override
                    public void onNext(LikesResponse likesResponse) {
                        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void sendUnlikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        int userId = application.getAuth().getUser().getIdUserName();
        String postId = dataItem.getIdPosts();
        application.getWebService().likes(userId, postId, like, hug, same, listen)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LikesResponse>() {
                    @Override
                    public void onNext(LikesResponse likesResponse) {
                        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
