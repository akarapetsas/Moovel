package com.moovel.users;

import android.support.annotation.NonNull;

import com.moovel.entities.Users;

import rx.Observable;

public interface MainContract {
    interface RetainedFragmentView {

        Observable<Users> getRetainedObservable();

        void retainObservable(@NonNull Observable<Users> observable);
    }

    interface View {

        void showProgressDialog();

        void showImages(@NonNull Users adImages);

        void dismissProgressDialog();
    }


    interface Presenter {
        void processRequest(@NonNull Observable<Users> observable);

        void clearSubscriptions();

        void resubscribeObservable(@NonNull Observable<Users> retainedObservable);
    }
}
