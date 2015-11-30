package com.hendalqett.products;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.hendalqett.products.adapters.ProductsRecyclerViewAdapter;
import com.hendalqett.products.constants.Keys;
import com.hendalqett.products.data.ProductColumns;
import com.hendalqett.products.data.ProductProvider;
import com.hendalqett.products.models.Image;
import com.hendalqett.products.models.Product;
import com.hendalqett.products.otto.BusProvider;
import com.hendalqett.products.receivers.NetworkStateReceiver;
import com.hendalqett.products.retrofit.GetMethods;
import com.hendalqett.products.utils.NetworkStateChanged;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    final int mNumberOfColumns = 2;
    final int mGridOrientation = StaggeredGridLayoutManager.VERTICAL;
    final int mNumberOfProductsPerRequest = 10;
    int mStartingProductId = 0;
    boolean isLeavingActivity = false;
    ProductsRecyclerViewAdapter mProductsRecyclerViewAdapter;


    ArrayList<Product> productsList;

    final static String LOG_TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    NetworkStateReceiver mReceiver;
    GetMethods getMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BusProvider.getInstance().post(new NetworkStateChanged(false));
//        productsList = new ArrayList<>();
        setUpRecycler();
//        mProductsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(this, productsList, mNumberOfProductsPerRequest, mStartingProductId);
//        recyclerView.setAdapter(mProductsRecyclerViewAdapter);
        initializeAdapter();


        getMethods = new GetMethods();
    }

    @Override
    protected void onResume() {
        this.mReceiver = new NetworkStateReceiver();
        registerReceiver(this.mReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(mReceiver);
        super.onPause();
        BusProvider.getInstance().unregister(this);

    }

    @Subscribe
    public void onEventMainThread(NetworkStateChanged event) {

        if (!event.isInternetConnected()) {
            if (!isLeavingActivity) {

                productsList.clear();
                mProductsRecyclerViewAdapter.setIsOffline(true);
                Cursor cursor = getContentResolver().query(ProductProvider.Products.CONTENT_URI,
                        null, null, null, null);
                if (cursor.getCount() != 0 && cursor != null) {

                    int productId, productPrice;
                    String productDescription;
                    Image image;
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                        productId = cursor.getInt(cursor.getColumnIndex(ProductColumns.PRODUCT_ID));
                        productDescription = cursor.getString(cursor.getColumnIndex(ProductColumns.PRODUCT_DESCRIPTION));
                        image = new Image(cursor.getString(cursor.getColumnIndex(ProductColumns.PRODUCT_IMAGE_URL)));
                        productPrice = cursor.getInt(cursor.getColumnIndex(ProductColumns.PRODUCT_PRICE));
                        Product product = new Product(productId, productDescription, image, productPrice);
                        productsList.add(product);
                    }

                    cursor.close();
                }

                mProductsRecyclerViewAdapter.notifyDataSetChanged();

            } else {
                isLeavingActivity = false;
            }

        }
        if (event.isInternetConnected()) {
            if (!isLeavingActivity) {
                mProductsRecyclerViewAdapter.setIsOffline(false);
                productsList.clear();
                mStartingProductId = 0;
                mProductsRecyclerViewAdapter.resetStartingProductId();
                getMethods.updateProducts(mProductsRecyclerViewAdapter, productsList, mNumberOfProductsPerRequest, mStartingProductId);

            } else {
                isLeavingActivity = false;
            }
        }
    }

    void setUpRecycler() {
        StaggeredGridLayoutManager mStaggeredGridLayoutManager;
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(mNumberOfColumns, mGridOrientation);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        recyclerView.setHasFixedSize(false);
    }

    void initializeAdapter() {
        productsList = new ArrayList<>();
        mProductsRecyclerViewAdapter = new ProductsRecyclerViewAdapter(this, productsList, mNumberOfProductsPerRequest, mStartingProductId);
        recyclerView.setAdapter(mProductsRecyclerViewAdapter);
        mProductsRecyclerViewAdapter.SetOnItemClickListener(new ProductsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isLeavingActivity = true;
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(Keys.PRODUCT, productsList.get(position));
                startActivity(intent);
            }
        });
    }

}
