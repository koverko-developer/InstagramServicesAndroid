package by.app.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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
import by.app.instagram.model.fui.UserInfoTop;

public class TopLikerUsersAdapter extends RecyclerView.Adapter {

    private List<UserInfoTop> list;
    Context context;

    public TopLikerUsersAdapter(List<UserInfoTop> _list, Context context) {
        list = _list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_user_small, parent, false);

        vh = new TopLikerHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopLikerHolder) {

            final UserInfoTop item = (UserInfoTop) list.get(position);
            ((TopLikerHolder) holder).username.setText(item.getUsername());
            Glide.with(((TopLikerHolder) holder).ava.getContext())
                    .load(item.getProfile_picture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(((TopLikerHolder) holder).ava);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TopLikerHolder extends RecyclerView.ViewHolder {
        public TextView username;
        ImageView ava;

        public TopLikerHolder(View v) {
            super(v);
            ava  = (ImageView) v.findViewById(R.id.ava);
            username = (TextView) v.findViewById(R.id.username);
        }
    }
}
