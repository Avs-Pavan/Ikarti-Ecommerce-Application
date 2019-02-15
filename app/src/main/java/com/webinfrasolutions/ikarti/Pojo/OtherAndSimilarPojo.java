package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 24/12/17.
 */

public class OtherAndSimilarPojo implements Serializable {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Similar Products")
    @Expose
    private List<Product> similarProducts = null;
    @SerializedName("Other Products In Shop")
    @Expose
    private List<Product> otherProductsInShop = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<Product> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public List<Product> getOtherProductsInShop() {
        return otherProductsInShop;
    }

    public void setOtherProductsInShop(List<Product> otherProductsInShop) {
        this.otherProductsInShop = otherProductsInShop;
    }

}
