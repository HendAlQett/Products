package com.hendalqett.products.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;


public interface ProductColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String ID = "_id";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String PRODUCT_ID = "product_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String PRODUCT_DESCRIPTION = "product_description";


    @DataType(DataType.Type.TEXT)
    @NotNull
    String PRODUCT_IMAGE_URL = "url";

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String PRODUCT_PRICE = "product_price";

}
