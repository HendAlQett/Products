package com.hendalqett.products.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = ProductDatabase.VERSION)
public final class ProductDatabase {

    private ProductDatabase() {
    }

    public static final int VERSION = 1;

    @Table(ProductColumns.class)
    public static final String PRODUCTS = "products";


}
