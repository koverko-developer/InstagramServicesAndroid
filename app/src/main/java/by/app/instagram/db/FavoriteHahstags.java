package by.app.instagram.db;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import by.app.instagram.model.firebase.PodcategoryHashtag;

public class FavoriteHahstags {

    private static Prefs prefs;
    Context context;

    public FavoriteHahstags(Context context) {
        this.context = context;
        prefs = new Prefs(context);
    }

    public void addToFavorite(PodcategoryHashtag podcat){

        Gson gson = new Gson();
        List<PodcategoryHashtag> list = new ArrayList<>();
        if(!prefs.getAllFavorites().equals("0")){
            List<PodcategoryHashtag> _l = gson.fromJson(prefs.getAllFavorites(), List.class);
            if(_l != null)
                if(!isFavorite(podcat)) list.addAll(_l);
        }
        list.add(podcat);
        String _nl = gson.toJson(list);
        prefs.setFavorites(_nl);

    }

    public boolean isFavorite(PodcategoryHashtag podcat){
        // TODO pltcm gиздец полный, я хз просто
        Gson gson = new Gson();
        List<PodcategoryHashtag> list = new ArrayList<>();
        if(!prefs.getAllFavorites().equals("0")){
            List<Object> _l = gson.fromJson(prefs.getAllFavorites(), List.class);
            if(_l != null) {
                for (Object i: _l
                     ) {
                    try{

                        String s_i = i.toString();
                        String cat = s_i.substring(s_i.indexOf("key=") + 4, s_i.length()-1);
                        Log.e("_l list favorite", cat);
                        String arr = cat.split(",")[0];
                        if(arr.equals(podcat.getKey())) {
                            Log.e("_l list favorite", arr + " = "+podcat.getKey());
                            return true;
                        }


                    }catch (Exception e){
                        return false;
                    }
                    //List<PodcategoryHashtag> h = (List<PodcategoryHashtag>) i;
                }
            }
        }



        return false;
    }

    public boolean removeFavorite(PodcategoryHashtag podcat){
        // TODO pltcm gиздец полный, я хз просто
        Gson gson = new Gson();
        List<PodcategoryHashtag> list = new ArrayList<>();
        if(!prefs.getAllFavorites().equals("0")){
            List<Object> _l = gson.fromJson(prefs.getAllFavorites(), List.class);
            if(_l != null) {
                int i1 = 0;
                int ii = 0;
                for (Object i: _l
                        ) {
                    try{

                        String s_i = i.toString();
                        String cat = s_i.substring(s_i.indexOf("category=") + 9, s_i.length()-1);
                        String arr = cat.split(",")[0];
                        if(arr.equals(podcat.getCategory())) ii = i1;
                        i1++;
                        //Log.e("_l list favorite", arr);

                    }catch (Exception e){
                        return false;
                    }
                    //List<PodcategoryHashtag> h = (List<PodcategoryHashtag>) i;
                }
                _l.remove(ii);
                String _nl = gson.toJson(_l);
                prefs.setFavorites(_nl);

            }
        }



        return false;
    }

    public void addToMy(PodcategoryHashtag podcat){

        Gson gson = new Gson();
        List<PodcategoryHashtag> list = new ArrayList<>();
        if(!prefs.getAllMy().equals("0")){
            List<PodcategoryHashtag> _l = gson.fromJson(prefs.getAllMy(), List.class);
            list.addAll(_l);
        }
        list.add(podcat);
        String _nl = gson.toJson(list);
        prefs.setMy(_nl);

    }
}
