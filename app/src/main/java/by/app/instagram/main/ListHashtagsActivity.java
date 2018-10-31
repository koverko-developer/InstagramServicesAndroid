package by.app.instagram.main;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import by.app.instagram.R;
import by.app.instagram.adapter.FeedAdapter;
import by.app.instagram.adapter.HashtagsAdapter;
import by.app.instagram.db.FavoriteHahstags;
import by.app.instagram.main.contracts.ListHashtagsContract;
import by.app.instagram.main.presenters.ListHashtagPresenter;
import by.app.instagram.model.firebase.Image;
import by.app.instagram.model.firebase.PodcategoryHashtag;

public class ListHashtagsActivity extends AppCompatActivity
                                implements ListHashtagsContract.View{

    private static String TAG = ListHashtagsActivity.class.getName();

    String key, name;
    ListHashtagPresenter _presenter;

    RecyclerView recycler;
    ImageView img_back;
    TextView tv_title, tv_coun_copy, text_language;
    CardView copy_card;
    RelativeLayout rel_language;
    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hashtags);

        if(_presenter == null) _presenter = new ListHashtagPresenter(this, this);

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        name = intent.getStringExtra("name");

        initView();
        if( key == null) return;
        else if(key.equals("0")) {
            fab_add.setVisibility(View.VISIBLE);
            _presenter.getMyHashtags();
        }
        else if(key.equals("1")) _presenter.getFavoriteHashtags();
        else _presenter.getFBHashtabg(key);

        Log.e(TAG, "key = "+key);



    }

    @Override
    public void initView() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }
        recycler = (RecyclerView) findViewById(R.id.h_recyler);
        img_back = (ImageView) findViewById(R.id.img_menu);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_title = (TextView) findViewById(R.id.title_bar);
        if(name != null) tv_title.setText(name);

        copy_card = (CardView) findViewById(R.id.card_copy);
        tv_coun_copy = (TextView) findViewById(R.id.tv_count_copie);

        text_language = (TextView) findViewById(R.id.text_language);
        rel_language = (RelativeLayout) findViewById(R.id.fab_language);
        rel_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _presenter.changeLanguage();
            }
        });

        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
            }
        });

    }

    @Override
    public void setRecycler(List<PodcategoryHashtag> _list) {

        HashtagsAdapter adapter = new HashtagsAdapter(_list, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(mLayoutManager);
        recycler.setAdapter(adapter);

        if(_list.isEmpty()) {
            if(key.equals("0")) showAddDialog();
            showEmptyList();
        }

    }

    @Override
    public void changeLanguage(String _lg) {

        text_language.setText(_lg);

        YoYo.with(Techniques.Landing).duration(500).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(rel_language);
    }

    @Override
    public void setCountCopy(int col) {

        if(col != 0) {
            copy_card.setVisibility(View.VISIBLE);
            tv_coun_copy.setText(String.valueOf(col));
            YoYo.with(Techniques.Landing).duration(500).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).playOn(copy_card);
        }else copy_card.setVisibility(View.GONE);

    }

    @Override
    public void showEmptyList() {
        Toast.makeText(ListHashtagsActivity.this,
                getResources().getString(R.string.empty_list), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddDialog() {
        final Dialog dialogInfo = new Dialog(ListHashtagsActivity.this);
        //dialogEdit.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInfo.setContentView(R.layout.dialog_add_hashtags);

        EditText et_cat = (EditText) dialogInfo.findViewById(R.id.et_name_cat);
        EditText et_hashtangs = (EditText) dialogInfo.findViewById(R.id.et_hashtags);

        TextView tv_cancel = (TextView) dialogInfo.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInfo.dismiss();
            }
        });

        TextView tv_ok = (TextView) dialogInfo.findViewById(R.id.tv_apply);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    String cat =  et_cat.getText().toString();
                    String hashtags = et_hashtangs.getText().toString();

                    if(cat.isEmpty()){
                        Toast.makeText(ListHashtagsActivity.this,
                                getResources().getString(R.string.empty_add_cat), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(hashtags.isEmpty()){
                        Toast.makeText(ListHashtagsActivity.this,
                                getResources().getString(R.string.empty_add_hashtags), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    hashtags.replaceAll("#", "");
                    String[] arr_h = hashtags.split(" ");
                    String arr_str = "";
                    if(arr_h.length == 1){
                        arr_str = arr_h[0];
                    }else if(arr_h.length > 1){

                        for(int i = 0; i< arr_h.length; i++){
                            if(i == 0) arr_str = " #"+arr_h[0];
                            else arr_str = arr_str + ", #" + arr_h[i];
                        }

                    }



                    PodcategoryHashtag podcategoryHashtag = new PodcategoryHashtag();
                    podcategoryHashtag.setName(cat);
                    podcategoryHashtag.setArr(arr_str);
                    podcategoryHashtag.setItemHashtags(_presenter.stringToArr(arr_str));
                    podcategoryHashtag.setLanguage("RU");
                    podcategoryHashtag.setCategory(genTextKey());
                    podcategoryHashtag.setKey(genTextKey());

                    FavoriteHahstags p = new FavoriteHahstags(ListHashtagsActivity.this);
                    p.addToMy(podcategoryHashtag);
                    _presenter.getMyHashtags();


                }catch (Exception e){

                }

                dialogInfo.dismiss();
            }
        });



        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogInfo.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogInfo.setCancelable(true);
        dialogInfo.show();
        dialogInfo.getWindow().setAttributes(lp);
    }

    public String genTextKey(){
        byte[] array = new byte[12];
        new Random().nextBytes(array);
        String genStr = new String(array, Charset.forName("UTF-8"));
        return genStr;
    }
}
