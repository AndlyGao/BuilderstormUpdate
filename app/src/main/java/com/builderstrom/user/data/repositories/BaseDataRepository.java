package com.builderstrom.user.data.repositories;

import android.app.Application;

import com.builderstrom.user.data.Prefs;
import com.builderstrom.user.data.database.DatabaseHelper;
import com.builderstrom.user.data.retrofit.api.ApiWebInterface;
import com.builderstrom.user.data.retrofit.api.DataNames;
import com.builderstrom.user.data.retrofit.api.RestClient;
import com.builderstrom.user.data.retrofit.modals.ErrorModel;
import com.builderstrom.user.data.retrofit.modals.PojoErrorModel;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;

abstract public class BaseDataRepository {
    public Prefs mPrefs;
    public DatabaseHelper mDatabase;
    protected ApiWebInterface restClient;

    BaseDataRepository(Application application) {
        mPrefs = Prefs.with(application);
        mDatabase = DatabaseHelper.getDatabaseInstance(application);
        restClient = RestClient.createService();
    }

    ErrorModel handleErrorBody(ResponseBody errorBody) throws IOException {
        Converter<ResponseBody, PojoErrorModel> errorConverter =
                RestClient.getRetrofitInstance().responseBodyConverter(PojoErrorModel.class, new Annotation[0]);
        PojoErrorModel error = errorConverter.convert(errorBody);
        return new ErrorModel(DataNames.TYPE_ERROR_API, error.getMessage());
    }


}
