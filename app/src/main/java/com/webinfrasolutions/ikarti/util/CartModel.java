package com.webinfrasolutions.ikarti.util;


/**
 * Created by anupamchugh on 09/02/16.
 */
public class CartModel {


    public static final int PLACE_ORDER=0;
    public static final int STORE_ICK=1;
   // public static final int AUDIO_TYPE=2;

    public int type;
    public int data;
    public String text;



    public CartModel(int type, String text, int data)
    {
        this.type=type;
        this.data=data;
        this.text=text;

    }

}
