package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 15/12/17.
 */

public class Record  implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}