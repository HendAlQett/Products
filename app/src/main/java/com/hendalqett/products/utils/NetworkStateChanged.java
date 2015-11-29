package com.hendalqett.products.utils;


public class NetworkStateChanged
{
    public boolean mIsInternetConnected;
    public NetworkStateChanged(boolean isInternetConnected)
    {
        this.mIsInternetConnected = isInternetConnected;
    }
    public boolean isInternetConnected()
    {
        return this.mIsInternetConnected;
    }
}