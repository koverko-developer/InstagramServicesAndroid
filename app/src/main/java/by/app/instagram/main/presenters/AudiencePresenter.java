package by.app.instagram.main.presenters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import by.app.instagram.api.ApiServices;
import by.app.instagram.db.AudienceDatesSort;
import by.app.instagram.db.Prefs;
import by.app.instagram.enums.TypeAudience;
import by.app.instagram.main.contracts.AudienceContract;
import by.app.instagram.model.Meta;
import by.app.instagram.model.ResponseApi;
import by.app.instagram.model.firebase.AudienceObject;
import by.app.instagram.model.firebase.Progress;
import by.app.instagram.model.pui.ChartItem;
import by.app.instagram.model.realm.AudienseInfoR;
import by.app.instagram.model.realm.TopLikersR;
import by.app.instagram.view.filter.TypeSpinnerFilter;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AudiencePresenter implements AudienceContract.Presenter{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference progress;
    DatabaseReference usersOn;
    DatabaseReference usersOff;

    AudienceContract.View _view;
    Context context;

    Prefs prefs;

    long period1 = 0;
    long period2 = 0;
    TypeSpinnerFilter typeSpinnerFilter = TypeSpinnerFilter.All;
    TypeAudience typeAudience = TypeAudience.Follow;
    Realm realm;

    public AudiencePresenter(AudienceContract.View _view, Context context) {
        this._view = _view;
        this.context = context;
        prefs = new Prefs(context);
        Realm.init(context);
        realm = Realm.getDefaultInstance();
        checkInternet();
    }

    ValueEventListener listenerProgress;
    ValueEventListener listenerUsersOff;
    ValueEventListener listenerUsersOn;

    List<AudienceObject> unfollow_list = new ArrayList<>();
    List<AudienceObject> follow_list = new ArrayList<>();

    @Override
    public void checkInternet() {

        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            addprogressListener();
            addListenerFirebase();
            if(!prefs.getAudienceFirst())_view.showSnackUpdate();
            else getAllData();
        }
        else {
            _view.showNoInternetConnection();
            getFromRealm();
        }

    }

    @Override
    public void getFromRealm() {

        final RealmResults<AudienseInfoR> infoRS =
                realm.where(AudienseInfoR.class).findAll();

        if(infoRS != null){
            if(infoRS.size() != 0){
                follow_list = infoRS.get(infoRS.size() -1).getListFollow();
                unfollow_list = infoRS.get(infoRS.size() -1).getListUnFollow();
                _view.setUnfollowsTV(unfollow_list.size());
                //_view.setfollowsTV(follow_list.size());
                clickCard(TypeAudience.Follow);
            }
        }

    }

    @Override
    public void getAllData() {
        //_view.showProgress();
        prefs.setAudienceFirst(false);
        rx.Observable<ResponseBody> observable =
                new ApiServices().getApi().getAudience(String.valueOf(prefs.getLApi()));
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
    public void addListenerFirebase() {

        listenerUsersOff = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                unfollow_list.clear();
                for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                    AudienceObject object = usersSnapshot.getValue(AudienceObject.class);
                    unfollow_list.add(object);
                }
                _view.setUnfollowsTV(unfollow_list.size());
                //addToRealmObjects();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        listenerUsersOn = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                follow_list.clear();
                for(DataSnapshot usersSnapshot : dataSnapshot.getChildren()){

                    AudienceObject object = usersSnapshot.getValue(AudienceObject.class);
                    follow_list.add(object);
                }

               clickCard(TypeAudience.Follow);
               //addToRealmObjects();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        usersOff = database.getReference("users/"+prefs.getLApi()+"/audience/followers_off");
        usersOn = database.getReference("users/"+prefs.getLApi()+"/audience/followers_on");

        usersOff.addValueEventListener(listenerUsersOff);
        usersOn.addValueEventListener(listenerUsersOn);
    }

    @Override
    public void destroyListenerFirebase() {
        addToRealmObjects();
        if(listenerUsersOn != null)usersOn.removeEventListener(listenerUsersOn);
        if(listenerUsersOff != null)usersOff.removeEventListener(listenerUsersOff);
    }

    @Override
    public void addprogressListener() {

        listenerProgress = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Progress progress = dataSnapshot.getValue(Progress.class);

                if(progress != null){
                    if(progress.isValue()) _view.showProgress();
                    else _view.hideProgress();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        progress = database.getReference("users/"+prefs.getLApi() + "/audience/progress");
        progress.addValueEventListener(listenerProgress);

    }

    @Override
    public void destroyListenerProgress() {
        if(progress != null) progress.removeEventListener(listenerProgress);
    }

    @Override
    public void clickCard(TypeAudience type) {
        this.typeAudience = type;
        sortAudience();

    }

    @Override
    public void setPeriod1(Long _period1) {
        this.period1 = _period1;
    }

    @Override
    public void setPeriod2(Long _perio2) {
        this.period2 = period2;
    }

    @Override
    public void setTypeSpinnerFilter(TypeSpinnerFilter typeSpinnerFilter) {
        this.typeSpinnerFilter = typeSpinnerFilter;
    }

    @Override
    public void setTypeAudience(TypeAudience typeAudience) {
        this.typeAudience = typeAudience;
    }

    @Override
    public void sortAudience() {

        List<ChartItem> sortList = new ArrayList<>();
        List<AudienceObject> list = new ArrayList<>();
        List<AudienceObject> sortUsers = new ArrayList<>();
        if(typeAudience == TypeAudience.Follow) {
            list = follow_list;
        }
        else if(typeAudience == TypeAudience.UnFollow) list = unfollow_list;

        if(typeSpinnerFilter == TypeSpinnerFilter.All) {
            sortList = AudienceDatesSort.getAudience(list);
            sortUsers = AudienceDatesSort.getAudienceUsers(list);
        }
        else if(typeSpinnerFilter == TypeSpinnerFilter.SelectMonth){
            sortList = AudienceDatesSort.getSelectedMonth(list, period1, period2);
            sortUsers = AudienceDatesSort.getSelectedMonthUsers(list, period1, period2);
        }
        else if(typeSpinnerFilter == TypeSpinnerFilter.CurrentMonth){
            sortList = AudienceDatesSort.getCurrentMonth(list);
            sortUsers = AudienceDatesSort.getCurrentMonthUsers(list);
        }

        if(typeAudience == TypeAudience.Follow) {
            _view.setfollow(sortUsers.size(),sortUsers);
            _view.setUnfollowsTV(unfollow_list.size());
        }
        else if(typeAudience == TypeAudience.UnFollow) {
            _view.setfollowsTV(follow_list.size());
            _view.setUnfollow(sortUsers.size(), sortUsers);
        }

        _view.addToValueToLineView(sortList);

    }

    @Override
    public void addToRealmObjects() {

        if(follow_list != null){

            realm.beginTransaction();

            AudienseInfoR audienseInfoR = realm.createObject(AudienseInfoR.class);

            for (AudienceObject object:follow_list
                 ) {
                final AudienceObject fObject = realm.copyToRealm(object);
                audienseInfoR.getListFollow().add(fObject);
            }

            realm.commitTransaction();
        }

        if(unfollow_list != null){

            realm.beginTransaction();

            AudienseInfoR audienseInfoR = realm.createObject(AudienseInfoR.class);

            for (AudienceObject object:unfollow_list
                    ) {
                final AudienceObject fObject = realm.copyToRealm(object);
                audienseInfoR.getListUnFollow().add(fObject);
            }

            realm.commitTransaction();
        }

    }
}
