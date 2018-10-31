package by.app.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.R;
import by.app.instagram.db.FavoriteHahstags;
import by.app.instagram.main.ListHashtagsActivity;
import by.app.instagram.model.firebase.ItemHashtag;
import by.app.instagram.model.firebase.PodcategoryHashtag;


public class HashtagsAdapter extends RecyclerView.Adapter {

    private List<PodcategoryHashtag> list;
    Context context;
    ListHashtagsActivity activity;

    ArrayList<String> copyArr = new ArrayList<>();

    public HashtagsAdapter(List<PodcategoryHashtag> _list, Context context) {
        list = _list;
        this.context = context;
        activity = (ListHashtagsActivity) context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_card_hashtag, parent, false);

        vh = new HashtagsHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HashtagsHolder) {

            final PodcategoryHashtag item = (PodcategoryHashtag) list.get(position);
            FavoriteHahstags favoriteHahstags = new FavoriteHahstags(context);
            ((HashtagsHolder) holder).name.setText(item.getName());
            boolean isCreated = true;
            ((HashtagsHolder) holder).flowLayout.removeAllViews();

            ((HashtagsHolder) holder).fab_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addAllArr(item.getItemHashtags());
                    for (ItemHashtag i : item.getItemHashtags()){
                        i.setCopy(true);
                    }
                    notifyDataSetChanged();
                }
            });

            ((HashtagsHolder) holder).fab_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!favoriteHahstags.isFavorite(item)){
                        favoriteHahstags.addToFavorite(item);
                        notifyDataSetChanged();
                    }
                    else {
                        favoriteHahstags.removeFavorite(item);
                        notifyDataSetChanged();
                    }
                }
            });

            if(favoriteHahstags.isFavorite(item)){
                ((HashtagsHolder) holder).fab_favorite
                        .setBackground(context.getResources().getDrawable(R.drawable.fab_circle_favorite));
            }else {
                ((HashtagsHolder) holder).fab_favorite
                        .setBackground(context.getResources().getDrawable(R.drawable.fab_circle));
            }

            if(item.getItemHashtags() != null){
                int i = 0;

                for (ItemHashtag itemHashtag: item.getItemHashtags()
                     ) {
                    CardView cardView = new CardView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    //params.setMargins(0,0,4,0);
                    cardView.setLayoutParams(params);
                    cardView.setRadius(4);

                    if(item.getItemHashtags().get(i).isCopy())cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    else cardView.setCardBackgroundColor(context.getResources().getColor(R.color.border_ava));
                    cardView.setCardElevation(5);
                    TextView tv = new TextView(context);
                    tv.setLayoutParams(params);
                    if(item.getItemHashtags().get(i).isCopy())tv.setTextColor(context.getResources().getColor(R.color.cardview_light_background));
                    else tv.setTextColor(context.getResources().getColor(R.color.black));
                    tv.setText(itemHashtag.getName());
                    tv.setTextSize(16);
                    tv.setPadding(8,4,8, 4);
                    cardView.addView(tv);
                    ((HashtagsHolder) holder).flowLayout.addView(cardView);
                    item.getItemHashtags().get(i).setCreated(true);

                    int finalI = i;
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!item.getItemHashtags().get(finalI).isCopy()){
                                item.getItemHashtags().get(finalI).setCopy(true);
                                getCopyTextFromArr(item.getItemHashtags().get(finalI).getName());
                            }else {
                                item.getItemHashtags().get(finalI).setCopy(false);
                                removeFromArr(item.getItemHashtags().get(finalI).getName());
                            }
                            notifyDataSetChanged();
                        }
                    });

                    i++;

                }

            }

        }


    }

    private void addAllArr(ArrayList<ItemHashtag> _list){
        ArrayList<String> arr = new ArrayList<>();
        for(ItemHashtag item : _list){
            arr.add(item.getName());
        }
        //copyArr.addAll(arr);

        for(int i = 0; i < arr.size(); i++){
            if(!copyArr.contains(arr.get(i))) copyArr.add(arr.get(i));
        }

        StringBuilder builder = new StringBuilder();

        for (String s : copyArr){
            builder.append(s);
        }

        String s = builder.toString();
        setCopy(s);

        activity.setCountCopy(copyArr.size());
    }

    private void removeFromArr(String name) {

        if(copyArr.contains(name)) copyArr.remove(name);

        StringBuilder builder = new StringBuilder();

        for (String s : copyArr){
            builder.append(s);
        }

        String s = builder.toString();
        setCopy(s);

        activity.setCountCopy(copyArr.size());

    }



    public void setCopy(String _str){
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", _str);
        clipboard.setPrimaryClip(clip);
    }

    public String getCopyTextFromArr(String _str){

        copyArr.add(_str);

        StringBuilder builder = new StringBuilder();

        for (String s : copyArr){
            builder.append(s);
        }

        String s = builder.toString();
        setCopy(s);
        activity.setCountCopy(copyArr.size());
        return "";
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HashtagsHolder extends RecyclerView.ViewHolder {
        public TextView name;
        RelativeLayout fab_copy, fab_favorite;
        FlowLayout flowLayout;

        public HashtagsHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.text_category);
            fab_copy = (RelativeLayout) v.findViewById(R.id.fap_copy);
            fab_favorite = (RelativeLayout) v.findViewById(R.id.fab_language);
            flowLayout = (FlowLayout) v.findViewById(R.id.flow);
        }
    }
}