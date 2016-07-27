package com.moovel.users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moovel.R;
import com.moovel.entities.Items;
import com.moovel.entities.User;
import com.moovel.entities.Users;
import com.moovel.service.APIService;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.UserViewHolder> {

    private List<Items> itemsList;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();


    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.avatar_image)
        ImageView image;

        @BindView(R.id.username)
        TextView username;

        @BindView(R.id.email)
        TextView email;

        public UserViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
            image = (ImageView) view.findViewById(R.id.avatar_image);
            username = (TextView) view.findViewById(R.id.username);
            email = (TextView) view.findViewById(R.id.email);
        }

        @Override
        public void onClick(View view) {
        }
    }

    public MainAdapter(List<Items> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_row, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Items items = itemsList.get(position);

        Picasso.with(holder.image.getContext()).load(items.getAvatarUrl()).into(holder.image);
        holder.username.setText(items.getLogin());

        Observable<User> observable = new APIService().getService().getUser(items.getLogin())
                .replay()
                .autoConnect();

        Subscription subscription = observable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(User user) {
                        holder.email.setText(user.getEmail());
                        holder.email.setOnClickListener(view -> {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",user.getEmail(), null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, user.getEmail()); // String[] addresses

                            holder.email.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        });
                    }
                });

        compositeSubscription.add(subscription);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
