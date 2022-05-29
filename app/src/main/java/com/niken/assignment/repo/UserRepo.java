package com.niken.assignment.repo;

import android.annotation.SuppressLint;
import android.util.Log;

import com.niken.assignment.model.CallBack;
import com.niken.assignment.model.User;
import com.niken.assignment.model.UserListCallBack;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class UserRepo {
    private static final String TAG = "UserRepo";

    public void insertUser(String name, String email, String mobileNumber, String gender, String deviceType, String profileUri, Long createdDate, CallBack callBack) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(bgRealm -> {
                    User user = bgRealm.createObject(User.class);
                    user.setName(name);
                    user.setEmail(email);
                    user.setPhoneNumber(mobileNumber);
                    user.setGender(gender);
                    user.setTypeOfDevice(deviceType);
                    user.setProfileImage(profileUri);
                    user.setCreatedDate(createdDate);
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        callBack.onSuccess();
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        callBack.onError();
                    }
                }

        );
    }

    public void retrieveUserList(UserListCallBack callBack) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<User> userList = realm.where(User.class).findAllAsync().sort("createdDate");
        callBack.onUserListRetrieved(userList);
    }

    @SuppressLint("SimpleDateFormat")
    public void filterUserList(String fromDate, String toDate, UserListCallBack callBack) {
        Realm realm = Realm.getDefaultInstance();
        Log.d(TAG, "from filterUserList: " + Long.parseLong(fromDate));
        Log.d(TAG, "to filterUserList: " + Long.parseLong(toDate));

        RealmResults<User> userList = realm.where(User.class).greaterThanOrEqualTo("createdDate", Long.parseLong(fromDate)).findAll()
                .where().lessThanOrEqualTo("createdDate", Long.parseLong(toDate)).findAll().sort("createdDate");
        callBack.onUserListRetrieved(userList);

    }

}
