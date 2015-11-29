package com.hendalqett.products.retrofit;

import com.hendalqett.products.models.Product;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;



public interface Api {


    @GET("/products")
    void getProducts(@Query("count") int count, @Query("from") int fromId, Callback<ArrayList<Product>> callback);


}
