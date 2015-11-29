package com.hendalqett.products.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class Product implements Parcelable{

    @SerializedName("id")
    int id;
    @SerializedName("productDescription")
    String productDescription;
    @SerializedName("image")
    Image image;
    @SerializedName("price")
    int price;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Product(int id, String productDescription, Image image, int price) {
        this.id = id;
        this.productDescription = productDescription;
        this.image = image;
        this.price = price;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(image,flags);
        dest.writeInt(id);
        dest.writeString(productDescription);
        dest.writeInt(price);


    }
    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }


    protected Product(Parcel in) {
        image = in.readParcelable(Image.class.getClassLoader());
        id = in.readInt();
        productDescription = in.readString();

        price = in.readInt();
    }



}
