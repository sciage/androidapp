package in.voiceme.app.voiceme.ActivityPage;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseFragment;
import in.voiceme.app.voiceme.main.MyRecyclerAdapter;
import in.voiceme.app.voiceme.main.UserPostDetails;


public class ActivityInteractionFragment extends BaseFragment {
    public static final String ARG_INTERACTION_PAGE = "ARG_INTERACTION_PAGE";

    private int mPage;


    public ActivityInteractionFragment() {
        // Required empty public constructor
    }

    public static ActivityYourFeedFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_INTERACTION_PAGE, page);
        ActivityYourFeedFragment fragment = new ActivityYourFeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_INTERACTION_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_interaction, container, false);

        //recyclerview
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_main_interaction_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(new MyRecyclerAdapter(this.getActivity(), getMainInteraction()));

        return view;
    }

    private ArrayList<UserPostDetails> getMainInteraction() {
        // collection of crime movies
        ArrayList<UserPostDetails> userPostDetails = new ArrayList<>();
        UserPostDetails movie = new UserPostDetails(R.mipmap.ic_launcher,
                "timestofly",
                "is",
                "sad",
                "family",
                "5 minutes ago",
                "this is a sample post message",
                "READ MORE....",
                R.drawable.ic_play_circle_outline_black_24dp);
        userPostDetails.add(movie);

        movie = new UserPostDetails(R.mipmap.ic_launcher,
                "timestofly",
                "is",
                "sad",
                "family",
                "5 minutes ago",
                "this is a sample post message",
                "READ MORE....",
                R.drawable.ic_play_circle_outline_black_24dp);
        userPostDetails.add(movie);

        movie = new UserPostDetails(R.mipmap.ic_launcher,
                "timestofly",
                "is",
                "sad",
                "family",
                "5 minutes ago",
                "this is a sample post message",
                "READ MORE....",
                R.drawable.ic_play_circle_outline_black_24dp);
        userPostDetails.add(movie);

        movie = new UserPostDetails(R.mipmap.ic_launcher,
                "timestofly",
                "is",
                "sad",
                "family",
                "5 minutes ago",
                "this is a sample post message",
                "READ MORE....",
                R.drawable.ic_play_circle_outline_black_24dp);
        userPostDetails.add(movie);

        movie = new UserPostDetails(R.mipmap.ic_launcher,
                "timestofly",
                "is",
                "sad",
                "family",
                "5 minutes ago",
                "this is a sample post message",
                "READ MORE....",
                R.drawable.ic_play_circle_outline_black_24dp);
        userPostDetails.add(movie);

        return userPostDetails;
    }

}
