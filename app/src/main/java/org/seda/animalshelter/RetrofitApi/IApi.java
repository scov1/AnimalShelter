package org.seda.animalshelter.RetrofitApi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IApi {

    @GET("email_registration.php")
    Call<Users> getEmailRegistration(
            @Query("user_name") String user_name,
            @Query("user_email") String user_email,
            @Query("user_password") String user_password);

    @GET("email_login.php")
    Call<Users> getEmailLogin(
            @Query("user_email") String user_email,
            @Query("user_password") String user_password);

}
