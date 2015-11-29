package com.hendalqett.products.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = ProductProvider.AUTHORITY, database = ProductDatabase.class)
public final class ProductProvider  {

    public static final String AUTHORITY =
            "com.hendalqett.products.data.ProductProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path{
        String PRODUCTS = "products";

    }

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }
    @TableEndpoint(table = ProductDatabase.PRODUCTS) public static class Products{
        @ContentUri(
                path = Path.PRODUCTS,
                type = "vnd.android.cursor.dir/product",
                defaultSort = ProductColumns.PRODUCT_ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.PRODUCTS);


        @InexactContentUri(
                name = "ID",
                path = Path.PRODUCTS + "/#",
                type = "vnd.android.cursor.item/product",
                whereColumn = ProductColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri(Path.PRODUCTS, String.valueOf(id));
        }
    }


}
