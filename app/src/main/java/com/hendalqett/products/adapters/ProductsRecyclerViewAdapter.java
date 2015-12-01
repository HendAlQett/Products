package com.hendalqett.products.adapters;


import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hendalqett.products.R;
import com.hendalqett.products.data.ProductColumns;
import com.hendalqett.products.data.ProductProvider;
import com.hendalqett.products.models.Product;
import com.hendalqett.products.retrofit.GetMethods;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ProductsViewHolder> {


    Context mContext;
    ArrayList<Product> productsList;
    int numberOfProductsPerRequest;
    int startingProductId;
    OnItemClickListener mItemClickListener;
    boolean isOffline = false;

    GetMethods getMethods;


    public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvProductDescription;
        TextView tvPrice;
        ImageView ivProduct;

        ProductsViewHolder(View itemView) {
            super(itemView);

            tvProductDescription = (TextView) itemView.findViewById(R.id.tvProductDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            ivProduct = (ImageView) itemView.findViewById(R.id.ivProduct);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }


    public ProductsRecyclerViewAdapter(Context mContext, ArrayList<Product> productsList, int numberOfProductsPerRequest, int startingProductId) {
        this.mContext = mContext;
        this.productsList = productsList;
        this.numberOfProductsPerRequest = numberOfProductsPerRequest;
        this.startingProductId = startingProductId;
        getMethods = new GetMethods();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_product_item, parent, false);
        ProductsViewHolder productsViewHolder = new ProductsViewHolder(view);
        return productsViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductsViewHolder holder, int position) {

        Product productItem = productsList.get(position);
        holder.tvProductDescription.setText(productItem.getProductDescription());
        holder.tvPrice.setText(mContext.getString(R.string.price, productItem.getPrice()));
        Picasso mPicasso = Picasso.with(mContext);
        mPicasso.load(productItem.getImage().getUrl()).placeholder(R.mipmap.ic_launcher).into(holder.ivProduct);

        if (position == getItemCount() - 1 && !isOffline) {
            startingProductId = numberOfProductsPerRequest + startingProductId;
            getMethods.updateProducts(this, productsList, numberOfProductsPerRequest, startingProductId);
        }

    }

    @Override
    public int getItemCount() {
        if (productsList != null) {
            return productsList.size();
        } else {
            return 0;
        }
    }

    public void setIsOffline(boolean isOffline) {
        this.isOffline = isOffline;
    }

    public void resetStartingProductId() {
        startingProductId = 0;
    }

    public void emptyList() {
        productsList.clear();
    }


    public void updateDatabase(ArrayList<Product> products) {
        mContext.getContentResolver().delete(ProductProvider.Products.CONTENT_URI, null, null);
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(products.size());

        for (Product product : products) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    ProductProvider.Products.CONTENT_URI);
            builder.withValue(ProductColumns.PRODUCT_ID, product.getId());
            builder.withValue(ProductColumns.PRODUCT_DESCRIPTION, product.getProductDescription());
            builder.withValue(ProductColumns.PRODUCT_IMAGE_URL, product.getImage().getUrl());
            builder.withValue(ProductColumns.PRODUCT_PRICE, product.getPrice());
            batchOperations.add(builder.build());
        }

        try {
            mContext.getContentResolver().applyBatch(ProductProvider.AUTHORITY, batchOperations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e("Insert Error", "Error applying batch insert", e);
        }


    }


}
