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
import by.app.instagram.model.fui.UserInfoTop;


public class AudienceAdapter extends RecyclerView.Adapter {

    private List<AudienceObject> list;
    Context context;

    public AudienceAdapter(List<AudienceObject> _list, Context context) {
        list = _list;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_audience, parent, false);

        vh = new AudienceHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AudienceHolder) {

            final AudienceObject item = (AudienceObject) list.get(position);
            ((AudienceHolder) holder).username.setText(item.getUsername());
            ((AudienceHolder) holder).fullname.setText(item.getFull_name());
            Glide.with(((AudienceHolder) holder).ava.getContext())
                    .load(item.getProfile_picture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(((AudienceHolder) holder).ava);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AudienceHolder extends RecyclerView.ViewHolder {
        public TextView username, fullname;
        ImageView ava;

        public AudienceHolder(View v) {
            super(v);
            ava  = (ImageView) v.findViewById(R.id.ava);
            username = (TextView) v.findViewById(R.id.username);
            fullname = (TextView) v.findViewById(R.id.fullname);
        }
    }
}
