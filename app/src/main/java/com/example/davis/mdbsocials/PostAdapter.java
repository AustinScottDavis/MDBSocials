package com.example.davis.mdbsocials;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    ArrayList<Post> data;

    PostAdapter(Context context, ArrayList<Post> data) {
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_view, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostViewHolder holder, int position) {
        final int pos = position;
        holder.textView.setText(data.get(position).title);
        holder.dateview.setText(data.get(position).date);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child(data.get(position).ID + ".png");

        Glide.with(context).using(new FirebaseImageLoader()).load(storageRef).into(holder.imageView);
        holder.progressBar.setVisibility(View.GONE);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("events").child(data.get(pos).ID);
        //final boolean interested = data.get(pos).interested.contains(FirebaseAuth.getInstance().getCurrentUser().getUid());

        data.get(pos).updateLikes();
        holder.liked.setText(Integer.toString(data.get(pos).numLikes));
        if (data.get(pos).interested.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()) && data.get(pos).interested.get(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            holder.like.setText(R.string.unlike);
        } else {
            holder.like.setText(R.string.like);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(pos).updateInterested(FirebaseAuth.getInstance().getCurrentUser().getUid(), ref);
                data.get(pos).updateLikes();
                if (data.get(pos).interested.containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()) && data.get(pos).interested.get(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    holder.like.setText(R.string.like);
                } else {
                    holder.like.setText(R.string.unlike);
                }
                holder.liked.setText(Integer.toString(data.get(pos).numLikes));
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        TextView dateview;
        TextView liked;
        Button like;
        Post currentP;
        ProgressBar progressBar;
        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.posttitle);
            imageView = itemView.findViewById(R.id.imageView);
            dateview = itemView.findViewById(R.id.postdate);
            liked = itemView.findViewById(R.id.numLikes);
            like = itemView.findViewById(R.id.likeButton);
            progressBar = itemView.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Post post = data.get(getAdapterPosition());
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("post", post);
                    i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRef = database.getReference("events");

            /*mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentP = data.get(getAdapterPosition());
                    HashMap<String, Boolean> m = new HashMap<>();
                    for (DataSnapshot child : dataSnapshot.child(currentP.ID).child("interested").getChildren()) {
                        m.put(child.getKey(), (Boolean) child.getValue());
                    }
                    currentP.interested = m;
                    currentP.updateLikes();
                    liked.setText(Integer.toString(currentP.numLikes));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
