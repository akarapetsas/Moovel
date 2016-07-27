package com.moovel.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.moovel.entities.Users;

import rx.Observable;

public class RetainedFragment extends Fragment implements MainContract.RetainedFragmentView {

    private Observable<Users> observable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Observable<Users> getRetainedObservable() {
        return observable;
    }

    @Override
    public void retainObservable(@NonNull Observable<Users> observable) {
        this.observable = observable;
    }

}

