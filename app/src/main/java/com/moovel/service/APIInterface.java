package com.moovel.service;

import com.moovel.entities.User;
import com.moovel.entities.Users;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface APIInterface {

    @GET("search/users?q=language:java")
    Observable<Users> getGithubUsers();

    @GET("users/{username}")
    Observable<User> getUser(@Path("username") String username);
}
