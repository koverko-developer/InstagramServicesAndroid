package by.app.instagram.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
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
import by.app.instagram.model.firebase.CategoryObject;

public class CategoryHashtagsAdapter extends RecyclerView.Adapter {

    private List<CategoryObject> list;
    Context context;

    public CategoryHashtagsAdapter(List<CategoryObject> _list, Context context) {
        list = _list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_grid_hashtags, parent, false);

        vh = new CategoryHashtagsHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryHashtagsHolder) {

            final CategoryObject item = (CategoryObject) list.get(position);
            if(item.getName() != null)
                ((CategoryHashtagsHolder) holder).text.setText(item.getName());

            if(item.getIcon() != null)
                Glide.with(context).load(item.getIcon())
                        .into(((CategoryHashtagsHolder) holder).fab);
            else {
                if(position == 0)
                    Glide.with(context).load(R.drawable.ic_add_black_24dp)
                        .into(((CategoryHashtagsHolder) holder).fab);
                else if(position == 1)
                    Glide.with(context).load(R.drawable.ic_favorite_border_black_24dp)
                            .into(((CategoryHashtagsHolder) holder).fab);
            }
            if(item.getColor() != null)
                ((CategoryHashtagsHolder) holder).fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(item.getColor())));
            else {
                if(position == 0)
                    ((CategoryHashtagsHolder) holder).fab.setBackgroundTintList(ColorStateList
                            .valueOf(context.getResources().getColor(R.color.colorPrimary)));
                else if(position == 1)
                    ((CategoryHashtagsHolder) holder).fab.setBackgroundTintList(ColorStateList
                        .valueOf(context.getResources().getColor(R.color.favorite)));
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CategoryHashtagsHolder extends RecyclerView.ViewHolder {
        public TextView text;
        FloatingActionButton fab;

        public CategoryHashtagsHolder(View v) {
            super(v);
            text  = (TextView) v.findViewById(R.id.text);
            fab = (FloatingActionButton) v.findViewById(R.id.fab);
        }
    }
}