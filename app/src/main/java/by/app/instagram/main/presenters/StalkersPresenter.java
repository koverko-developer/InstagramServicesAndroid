package by.app.instagram.main.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.Prefs;
import by.app.instagram.main.contracts.StalkersContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.firebase.HashTagObject;
import by.app.instagram.model.firebase.Progress;
import by.app.instagram.model.firebase.StalkersObject;
import by.app.instagram.model.realm.StalkerOnjectR;
import by.app.instagram.model.realm.UsersHashtagsR;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StalkersPresenter implements StalkersContract.Presenter{

    private static String TAG = StalkersPresenter.class.getName();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference progress;
    DatabaseReference allStalkers;

    StalkersContract.View _view;
    Context context;

    Prefs prefs;
    Realm realm;

    ValueEventListener listenerProgress;
    ValueEventListener listenerStalkers;

    boolean isFirst = true;

    Progress progressO;

    List<StalkersObject> list_all = new ArrayList<>();

    public StalkersPresenter(StalkersContract.View _view, Context context) {
        this._view = _view;
        this.context = context;
        prefs = new Prefs(context);
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        checkInternet();
    }

    @Override
    public void checkInternet() {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            addProgressListener();
            addListenerAll();
            if(!prefs.getStalkersFirst())_view.showSnackUpdate();
            else getAllData();
        }
        else {
            _view.showNoInternetConnection();
            getFromRealm();
        }
    }

    @Override
    public void addProgressListener() {
        listenerProgress = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Progress progress = dataSnapshot.getValue(Progress.class);

                if(progress != null){

                    progressO = progress;

                    if(progress.isValue()) _view.showProgress();
                    else _view.hideProgress();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        progress = database.getReference("users/"+prefs.getLApi() + "/stalkers/progress");
        progress.addValueEventListener(listenerProgress);
    }

    @Override
    public void destroyProgressListener() {
        addToRealm();
        progress.removeEventListener(listenerProgress);
    }

    @Override
    public void getAllData() {
        Map<String, String> map = new HashMap<>();
        map.put("count_media", String.valueOf(checkMediaRange(prefs.getCountMedia())));

        prefs.setStalkersFirst(false);
        rx.Observable<ResponseBody> observable =
                new ApiServices().getApi().getStalkers(String.valueOf(prefs.getLApi()), map);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        _view.hideProgress();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {

                        try{

                            if(responseBody != null){
                                String resp = responseBody.string();
                                Gson gson = new Gson();

                                if(resp.contains("error_type")){
                                    Meta meta = gson.fromJson(resp, Meta.class);
                                    String err = meta.getMeta().getErrorMessage();
                                    _view.hideProgress();
                                }else{
                                    ResponseApi responseApi =
                                            gson.fromJson(resp, ResponseApi.class);

                                }

                            }

                        }
                        catch (Exception e){

                        }

                    }
                });
    }

    @Override
    public int checkMediaRange(Long count_media) {
        int count_feed_media = 0;
        if(count_media <=18) count_feed_media = 1;
        else {
            //count_media = Long.valueOf(36);
            double range = (double) count_media / 18;
            if(range > 1) {
                if((range == Math.floor(range)) && !Double.isInfinite(range)){
                    count_feed_media = (int) range;
                }else {
                    count_feed_media = (int) range +1;
                }
            }
        }
        Log.e(TAG, "count media = "+count_feed_media);
        return count_feed_media;
    }

    @Override
    public void addListenerAll() {
        _view.showProgress();
        listenerStalkers = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  Log.e(TAG, "change listener stalkers");
                  if(progressO != null && !progressO.isValue()){
                      list_all.clear();
                      for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                          StalkersObject object = usersSnapshot.getValue(StalkersObject.class);
                          if(object != null)
                          {
                              list_all.add(object);
                          }

                      }

                      if(list_all != null){
                          Collections.sort(list_all, StalkersObject.StalkersComparator);
                      }

                      List<StalkersObject> sl = new ArrayList<>();
                      if(list_all.size() > 20){


                          for(int i = 0; i < 20; i++){
                              sl.add(list_all.get(i));
                          }
                      }
                      _view.setRecycler(sl);

                      _view.hideProgress();
                  }else if(isFirst){
                      isFirst = false;
                      list_all.clear();
                      for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                          StalkersObject object = usersSnapshot.getValue(StalkersObject.class);
                          if(object != null)
                          {
                              //if(list_all.size() < 20) list_all.add(object);
                              list_all.add(object);
                          }

                      }

                      if(list_all != null){
                          Collections.sort(list_all, StalkersObject.StalkersComparator);
                      }

                      List<StalkersObject> sl = new ArrayList<>();
                      if(list_all.size() > 20){


                          for(int i = 0; i < 20; i++){
                              sl.add(list_all.get(i));
                          }
                      }
                      _view.setRecycler(sl);

                  }else getFromRealm();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        allStalkers = database.getReference("users/"+prefs.getLApi() + "/stalkers/users/");
        allStalkers.addValueEventListener(listenerStalkers);
    }

    @Override
    public void destroyListenersAll() {
        allStalkers.removeEventListener(listenerStalkers);
    }

    @Override
    public void getFromRealm() {

        final RealmResults<StalkerOnjectR> infoRS =
                realm.where(StalkerOnjectR.class).findAll();

        if(infoRS != null){
            if(infoRS.size() != 0){
                list_all = infoRS.get(infoRS.size() -1).getList();
                _view.setRecycler(list_all);
            }
        }
    }

    @Override
    public void addToRealm() {
        if(list_all != null){
            realm.beginTransaction();

            StalkerOnjectR audienseInfoR = realm.createObject(StalkerOnjectR.class);

            for (StalkersObject object:list_all
                    ) {
                final StalkersObject fObject = realm.copyToRealm(object);
                audienseInfoR.getList().add(fObject);
            }


            realm.commitTransaction();
        }


    }
}
