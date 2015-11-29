package com.hendalqett.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.hendalqett.products.constants.Keys;
import com.hendalqett.products.models.Product;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    Product product;
    @Bind(R.id.ivProduct)
    ImageView ivProduct;
    @Bind(R.id.tvProductDescription)
    TextView tvProductDescription;
    @Bind(R.id.tvPrice)
    TextView tvPrice;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        product = intent.getParcelableExtra(Keys.PRODUCT);
        toolbar.setTitle(getString(R.string.details_title));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar.setTitle(getString(R.string.details_title));
        loadViews();
    }

    private void loadViews() {
        tvProductDescription.setText(product.getProductDescription());
        tvPrice.setText(getString(R.string.price, product.getPrice()));
        Picasso mPicasso = Picasso.with(this);
        mPicasso.load(product.getImage().getUrl()).placeholder(R.mipmap.ic_launcher).into(ivProduct);

    }
}
