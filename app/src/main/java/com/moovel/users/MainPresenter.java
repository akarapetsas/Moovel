package com.moovel.users;

import android.support.annotation.NonNull;

import com.moovel.entities.Users;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter implements MainContract.Presenter, Observer<Users> {

    private MainContract.View activityView;
    private MainContract.RetainedFragmentView retainedFragmentView;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public MainPresenter(MainContract.View activityView, MainContract.RetainedFragmentView retainedFragmentView) {
        this.activityView = activityView;
        this.retainedFragmentView = retainedFragmentView;
    }

    @Override
    public void processRequest(@NonNull Observable<Users> observable) {
        retainedFragmentView.retainObservable(observable);
        subscribeTo(observable);
    }

    @Override
    public void clearSubscriptions() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear();
        }
    }

    @Override
    public void resubscribeObservable(@NonNull Observable<Users> retainedObservable) {
        subscribeTo(retainedObservable);
    }

    private void subscribeTo(Observable<Users> observable) {
        activityView.showProgressDialog();

        Subscription subscription = observable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);

        compositeSubscription.add(subscription);
    }

    @Override
    public void onCompleted() {
        activityView.dismissProgressDialog();
    }

    @Override
    public void onError(Throwable throwable) {
        activityView.dismissProgressDialog();
    }

    @Override
    public void onNext(Users marketPriceDataResponse) {
        activityView.showImages(marketPriceDataResponse);
    }
}
