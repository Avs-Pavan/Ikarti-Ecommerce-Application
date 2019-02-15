package com.webinfrasolutions.ikarti.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 16/12/17.
 */

public class CartItem  implements Serializable{


    @SerializedName("cart_id")
    @Expose
    private String cartId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_description")
    @Expose
    private String productDescription;
    @SerializedName("no_of_products")
    @Expose
    private String noOfProducts;
    @SerializedName("delivery")
    @Expose
    private String delivery;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pics")
    @Expose
    private List<Pic> pics = null;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(String noOfProducts) {
        this.noOfProducts = noOfProducts;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Pic> getPics() {
        return pics;
    }

    public void setPics(List<Pic> pics) {
        this.pics = pics;
    }
}