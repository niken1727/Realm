package com.niken.assignment.viewmodel;

import androidx.lifecycle.ViewModel;

import com.niken.assignment.model.CallBack;
import com.niken.assignment.model.User;
import com.niken.assignment.model.UserListCallBack;
import com.niken.assignment.repo.UserRepo;

import java.util.List;

public class UserViewModel extends ViewModel {

    UserRepo userRepo = new UserRepo();

    public void insertUser(String name, String email, String mobileNumber, String gender, String deviceType, String profileUri, Long createdDate, CallBack callBack) {
        userRepo.insertUser(name, email, mobileNumber, gender, deviceType, profileUri, createdDate, callBack);
    }

    public void retrieveUserList(UserListCallBack callBack) {
         userRepo.retrieveUserList(callBack);
    }

    public void filterUserList(String fromDate, String toDate, UserListCallBack callBack) {
        userRepo.filterUserList(fromDate, toDate, callBack);
    }
}
