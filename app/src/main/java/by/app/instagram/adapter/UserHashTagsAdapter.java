package by.app.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.app.instagram.R;
import by.app.instagram.model.firebase.HashTagObject;

public class UserHashTagsAdapter extends RecyclerView.Adapter {

    private List<HashTagObject> list;
    Context context;

    public UserHashTagsAdapter(List<HashTagObject> _list, Context context) {
        list = _list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_hashtags_user, parent, false);

        vh = new HashtagHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HashtagHolder) {

            final HashTagObject item = list.get(position);

            ((HashtagHolder) holder).tv_name.setText(item.getName());
            ((HashtagHolder) holder).tv_count_posts.setText(
                    context.getResources().getString(R.string.find_to_posts)+" "+
                            String.valueOf(item.getCount_all())+" "+
                            context.getResources().getString(R.string.posts_1)
            );

            ((HashtagHolder) holder).tv_count_comments.setText(String.valueOf(item.getCount_comments()));
            ((HashtagHolder) holder).tv_count_likes.setText(String.valueOf(item.getCount_like()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HashtagHolder extends RecyclerView.ViewHolder {
        public TextView tv_name, tv_count_posts,
                tv_count_comments, tv_count_likes;

        public HashtagHolder(View v) {
            super(v);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_count_posts = (TextView) v.findViewById(R.id.tv_count_posts);
            tv_count_comments = (TextView) v.findViewById(R.id.tv_count_comments);
            tv_count_likes = (TextView) v.findViewById(R.id.tv_count_likes);
        }
    }
}
