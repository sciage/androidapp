package in.voiceme.app.voiceme.infrastructure;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.NotificationsPage.HomeActivity;
import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.ProfilePage.Account;
import in.voiceme.app.voiceme.ProfilePage.ProfileActivity;
import in.voiceme.app.voiceme.ProfilePage.User;


public class MainNavDrawer extends NavDrawer {
    private final TextView displayNameText;
    private final ImageView avatarImage;

    public MainNavDrawer(final BaseActivity activity) {
        super(activity);

        addItem(new ActivityNavDrawerItem(ProfileActivity.class, "Profile", null, R.mipmap.ic_action_unread, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(HomeActivity.class, "Notification", null, R.mipmap.ic_action_send_now, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(MainActivity.class, "Activity", null, R.mipmap.ic_action_group, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(DiscoverActivity.class, "Discover", null, R.mipmap.ic_action_person, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(LicenseActivity.class, "license", null, R.mipmap.ic_action_person, R.id.include_main_nav_drawer_topItems));

        addItem(new BasicNavDrawerItem("Logout", null, R.mipmap.ic_action_backspace, R.id.include_main_nav_drawer_bottomItems) {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(activity, "You have logged out!", Toast.LENGTH_SHORT).show();*/
                activity.getVoicemeApplication().getAuth().logout();
            }
        });

        displayNameText = (TextView) navDrawerView.findViewById(R.id.include_main_nav_drawer_displayName);
        avatarImage = (ImageView) navDrawerView.findViewById(R.id.include_main_nav_drawer_avatar);

        User loggedInUser = activity.getVoicemeApplication().getAuth().getUser();
        displayNameText.setText(loggedInUser.getUserNickName());

        Picasso.with(activity).load(loggedInUser.getAvatarPics()).into(avatarImage);
    }

    @Subscribe
    public void onUserDetailsUpdated(Account.UserDetailsUpdatedEvent event) {
        Picasso.with(activity).load(event.User.getAvatarPics()).into(avatarImage);
        displayNameText.setText(event.User.getUserNickName());
    }
}
