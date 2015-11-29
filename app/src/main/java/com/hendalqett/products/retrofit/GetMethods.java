package com.hendalqett.products.retrofit;

import com.hendalqett.products.adapters.ProductsRecyclerViewAdapter;
import com.hendalqett.products.models.Product;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GetMethods {
    public void updateProducts(final ProductsRecyclerViewAdapter adapter, final ArrayList<Product> productsList, int numberOfProductsPerRequest, int startPoint) {
        RestClient.get().getProducts(numberOfProductsPerRequest, startPoint, new Callback<ArrayList<Product>>() {
            @Override
            public void success(ArrayList<Product> products, Response response) {
                productsList.addAll(products);
                adapter.notifyDataSetChanged();
                adapter.updateDatabase(productsList);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
