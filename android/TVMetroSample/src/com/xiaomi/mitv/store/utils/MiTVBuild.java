package com.xiaomi.mitv.store.utils;

import java.util.Locale;

public class MiTVBuild {

    public static final int M3_PRODUCT = 0;
    public static final int TV_PRODUCT = 1;
    public static final int M6_PRODUCT = 2;
    public static final int M8_PRODUCT = 3;
    public static final int TV2_PRODUCT = 4;
    public static final int M7_PRODUCT = 5;
    public static final int TV2_XP_PRODUCT = 6;

    private static int sProductCode = M3_PRODUCT;

    static{
            String product = android.os.Build.PRODUCT;
            if(product != null){
                    product = product.toLowerCase(Locale.getDefault());
                    if(product.contains("braveheart")){
                            sProductCode = TV_PRODUCT;
                    }else if(product.contains("casablanca")){
                            sProductCode = M6_PRODUCT;
                    }else if (product.contains("dredd")) {
                            sProductCode = M8_PRODUCT;
                    }else if (product.contains("entrapment_xp")) {
                        sProductCode = TV2_XP_PRODUCT;
                    }else if(product.contains("entrapment")){
                            sProductCode = TV2_PRODUCT;
                    }else if(product.contains("forrestgump")){
                        sProductCode = M7_PRODUCT;
                    }
            }
    }

    public static int getProductCode(){
            return sProductCode;
    }

    public static boolean isBoxProduct(){
            if(sProductCode == M3_PRODUCT || sProductCode == M6_PRODUCT ||
                            sProductCode == M8_PRODUCT || sProductCode == M7_PRODUCT){
                    return true;
            }
            return false;
    }

    public static boolean isTvProduct(){
            if(sProductCode == TV_PRODUCT || sProductCode == TV2_PRODUCT
                    || sProductCode == TV2_XP_PRODUCT){
                    return true;
            }
            return false;
    }
}
