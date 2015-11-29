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
    boolean isLeavingActivity= false;
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
        productsList = new ArrayList<>();
        setUpRecycler();
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
        getMethods= new GetMethods();
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

                mProductsRecyclerViewAdapter.setIsOffline(true);
                productsList.clear();
                mProductsRecyclerViewAdapter.notifyDataSetChanged();
//                Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                Cursor c = getContentResolver().query(ProductProvider.Products.CONTENT_URI,
                        null, null, null, null);
                if (c.getCount()!=0 && c!=null){

                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                        Image image = new Image(c.getString(c.getColumnIndex(ProductColumns.PRODUCT_IMAGE_URL)));
                        Product product = new Product(c.getInt(c.getColumnIndex(ProductColumns.PRODUCT_ID)), c.getString(c.getColumnIndex(ProductColumns.PRODUCT_DESCRIPTION)), image, c.getInt(c.getColumnIndex(ProductColumns.PRODUCT_PRICE)));
                        productsList.add(product);
                    }

                    c.close();
                }

                mProductsRecyclerViewAdapter.notifyDataSetChanged();
            }
            else {
                isLeavingActivity=false;
            }

        }
        if (event.isInternetConnected() ) {
            if (!isLeavingActivity) {

                mProductsRecyclerViewAdapter.setIsOffline(false);
                productsList.clear();

                getMethods.updateProducts(mProductsRecyclerViewAdapter,productsList,mNumberOfProductsPerRequest,mStartingProductId);
//                Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();

            }
            else
            {
                isLeavingActivity=false;
            }
        }
    }

    void setUpRecycler() {
        StaggeredGridLayoutManager mStaggeredGridLayoutManager;
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(mNumberOfColumns, mGridOrientation);
        recyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        recyclerView.setHasFixedSize(false);
    }

}
