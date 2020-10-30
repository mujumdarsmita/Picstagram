package com.example.picstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.picstagram.Post;
import com.example.picstagram.PostAdapter;
import com.example.picstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

  RecyclerView rvPosts;
  public static final String TAG = "TimelineFragment";
  protected PostAdapter adapter;
  protected List<Post> allPosts;

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public TimelineFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_timeline, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    rvPosts = view.findViewById(R.id.rvPosts);
    allPosts = new ArrayList<>();
    adapter = new PostAdapter(getContext(), allPosts);
    rvPosts.setAdapter(adapter);
    rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
    queryPosts();
  }
  protected void queryPosts() {
    // Specify which class to query
    final ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
    query.include(Post.KEY_USER);
    query.setLimit(20);
    query.addDescendingOrder(Post.KEY_CREATED_AT);
    query.findInBackground(new FindCallback<Post>() {
      @Override
      public void done(List<Post> posts, ParseException e) {
        if (e != null) {
          Log.e(TAG, "Issue with getting posts");
        }

        for (Post post : posts) {
          Log.i(TAG,
                "Post received" +
                post.getDescription() +
                "username:" +
                post.getUser().getUsername());
        }
        allPosts.addAll(posts);
        adapter.notifyDataSetChanged();
      }
    });
  }
}