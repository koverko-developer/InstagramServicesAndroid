package by.app.instagram.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import by.app.instagram.R;
import by.app.instagram.db.MyTexyUtil;
import by.app.instagram.model.firebase.MediaObject;
import by.app.instagram.model.firebase.StalkersObject;

public class FeedAdapter extends RecyclerView.Adapter {

    private List<MediaObject> list;
    Context context;

    public FeedAdapter(List<MediaObject> _list, Context context) {
        list = _list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_feed, parent, false);

        vh = new FeedHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeedHolder) {

            final MediaObject item = (MediaObject) list.get(position);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            ((FeedHolder) holder).date.setText(df.format(item.getTakenAt()));

            ((FeedHolder) holder).count_likes.setText(MyTexyUtil.countsInt(item.getCountsLikes()));
            ((FeedHolder) holder).count_comments.setText(MyTexyUtil.countsInt(item.getCountComments()));
            if(item.getCountViews() != 0){
                ((FeedHolder) holder).count_views.setText(MyTexyUtil.countsInt(item.getCountViews()));
                ((FeedHolder) holder).count_views.setVisibility(View.VISIBLE);
                ((FeedHolder) holder).img_views.setVisibility(View.VISIBLE);
            }else {
                ((FeedHolder) holder).count_views.setVisibility(View.GONE);
                ((FeedHolder) holder).img_views.setVisibility(View.GONE);
            }
            if(item.getText() != null) {
                if(item.getText().isEmpty()){
                    ((FeedHolder) holder).text.setVisibility(View.GONE);
                }else {
                    ((FeedHolder) holder).text.setText(item.getText());
                }
            }else ((FeedHolder) holder).text.setVisibility(View.GONE);
            Glide.with(((FeedHolder) holder).img_post.getContext())
                    .load(item.getImages().getUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .apply(RequestOptions.centerCropTransform())
                    .into(((FeedHolder) holder).img_post);
            if(item.getMediaType() == 1) ((FeedHolder) holder).img_media_type.setVisibility(View.GONE);
            else if(item.getMediaType() == 2){
                ((FeedHolder) holder).img_media_type.setVisibility(View.VISIBLE);
                ((FeedHolder) holder).img_media_type.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_videocam_black_24dp));
            }
            else if(item.getMediaType() == 8){
                ((FeedHolder) holder).img_media_type.setVisibility(View.GONE);
                ((FeedHolder) holder).img_media_type.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_collections_black_24dp));
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FeedHolder extends RecyclerView.ViewHolder {
        public TextView date, count_likes, count_comments, count_views,text;
        ImageView img_post, img_media_type, img_views;

        public FeedHolder(View v) {
            super(v);
            img_post  = (ImageView) v.findViewById(R.id.img_post);
            img_media_type  = (ImageView) v.findViewById(R.id.img_media_type);
            img_views  = (ImageView) v.findViewById(R.id.img_views);
            date = (TextView) v.findViewById(R.id.date);
            count_likes = (TextView) v.findViewById(R.id.count_likes);
            count_comments = (TextView) v.findViewById(R.id.count_comments);
            count_views = (TextView) v.findViewById(R.id.count_views);
            text = (TextView) v.findViewById(R.id.text);

        }
    }
}