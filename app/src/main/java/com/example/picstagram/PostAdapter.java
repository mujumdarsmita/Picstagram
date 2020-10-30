package com.example.picstagram;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ui.widget.ParseImageView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
  Context context;
  List<Post> posts;

  public PostAdapter(Context context, List<Post> posts){
    this.context = context;
    this.posts = posts;
  }


  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
   View view  = LayoutInflater.from(context).inflate(R.layout.item_posts, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     Post post = posts.get(position);
     holder.bind(post);
  }

  @Override
  public int getItemCount() {
    return posts.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder{

    TextView tvName;
    TextView tvDescription;
    ParseImageView ivPost;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      tvName = itemView.findViewById(R.id.tvName);
      tvDescription = itemView.findViewById(R.id.tvDescription);
      ivPost = itemView.findViewById(R.id.ivPost);
    }

    public void bind(Post post) {
      tvName.setText(post.getUser().getUsername());
      tvDescription.setText(post.getDescription());
      ParseFile image = post.getImage();
      if(image != null){
        ivPost.setParseFile(post.getImage());
        ivPost.loadInBackground();
      }

    }
  }
}
