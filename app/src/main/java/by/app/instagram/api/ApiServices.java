package by.app.instagram.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServices {

    Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://apiinstagramanalitics.herokuapp.com/")
            .build();

    public IInstagramApi getApi(){

        IInstagramApi api = retrofit.create(IInstagramApi.class);
        return api;
    }

}
