package in.voiceme.app.voiceme.DiscoverPage;

import android.view.View;

import com.like.LikeButton;

import in.voiceme.app.voiceme.services.PostsModel;

public interface LikeUnlikeClickListener {
    void onItemClick(PostsModel model, View v);

    void onLikeUnlikeClick(PostsModel model, LikeButton v);
}
