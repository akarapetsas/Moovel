package com.moovel.users;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.moovel.R;
import com.moovel.entities.Users;
import com.moovel.service.APIService;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private MainPresenter presenter;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progressDialog;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RetainedFragment retainedFragment = (RetainedFragment) fragmentManager.findFragmentByTag(RetainedFragment.class.getName());

        if (retainedFragment == null) {
            retainedFragment = new RetainedFragment();
            fragmentManager.beginTransaction().add(retainedFragment, RetainedFragment.class.getName()).commit();
            presenter = new MainPresenter(this, retainedFragment);
            Observable<Users> observable = new APIService().getService().getGithubUsers()
                    .replay()
                    .autoConnect();
            presenter.processRequest(observable);
        } else {
            Log.d(getClass().getName(), "Reusing existing retained fragment.");
            presenter = new MainPresenter(this, retainedFragment);
            presenter.resubscribeObservable(retainedFragment.getRetainedObservable());
        }
    }

    @Override
    public void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", "Loading users");
    }

    @Override
    public void showImages(@NonNull Users users) {
        adapter = new MainAdapter(users.getItems());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}
