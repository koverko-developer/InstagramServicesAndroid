package by.app.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import by.app.instagram.R;
import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.firebase.StalkersObject;


public class StalkersAdapter extends RecyclerView.Adapter {

    private List<StalkersObject> list;
    Context context;

    public StalkersAdapter(List<StalkersObject> _list, Context context) {
        list = _list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_stalkers, parent, false);

        vh = new AudienceHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AudienceHolder) {

            final StalkersObject item = (StalkersObject) list.get(position);
            ((AudienceHolder) holder).username.setText(item.getUname());
            ((AudienceHolder) holder).fullname.setText(item.getFull_name());
            Glide.with(((AudienceHolder) holder).ava.getContext())
                    .load(item.getPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(((AudienceHolder) holder).ava);
            if(item.getCol_like() != 0){
                ((AudienceHolder) holder).tv_count_likes.setVisibility(View.VISIBLE);
                ((AudienceHolder) holder).tv_count_likes.setText(context.getResources()
                .getString(R.string.stalkers_find_like_h1)+ " "+ String.valueOf(item.getCol_like()));
            }else ((AudienceHolder) holder).tv_count_likes.setVisibility(View.GONE);

            if(item.getCol_comments() != 0){
                ((AudienceHolder) holder).tv_count_comments.setVisibility(View.VISIBLE);
                ((AudienceHolder) holder).tv_count_comments.setText(context.getResources()
                        .getString(R.string.stalkers_find_comments_h1)+ " "+ String.valueOf(item.getCol_comments()));
            }else ((AudienceHolder) holder).tv_count_comments.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AudienceHolder extends RecyclerView.ViewHolder {
        public TextView username, fullname, tv_count_likes, tv_count_comments;
        ImageView ava;

        public AudienceHolder(View v) {
            super(v);
            ava  = (ImageView) v.findViewById(R.id.ava);
            username = (TextView) v.findViewById(R.id.username);
            fullname = (TextView) v.findViewById(R.id.fullname);
            tv_count_likes = (TextView) v.findViewById(R.id.text_col_like);
            tv_count_comments = (TextView) v.findViewById(R.id.text_col_comments);
        }
    }
}