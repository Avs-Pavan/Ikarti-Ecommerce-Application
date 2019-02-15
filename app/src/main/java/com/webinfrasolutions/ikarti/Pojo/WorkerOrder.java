package com.webinfrasolutions.ikarti.Pojo;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 4/1/18.
 */

public class WorkerOrder implements  Serializable {


    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("delivery_type")
    @Expose
    private String deliveryType;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("order_ip_address")
    @Expose
    private String orderIpAddress;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("created_data")
    @Expose
    private String createdData;
    @SerializedName("order_details_id")
    @Expose
    private String orderDetailsId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_deal_price")
    @Expose
    private String productDealPrice;
    @SerializedName("item_total")
    @Expose
    private String itemTotal;
    @SerializedName("saved_price")
    @Expose
    private String savedPrice;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("worker_id")
    @Expose
    private String workerId;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("pics")
    @Expose
    private List<Pic> pics = null;
    @SerializedName("user")
    @Expose
    private List<UserData> user = null;
    @SerializedName("location")
    @Expose
    private List<LocationPojo> location = null;
    @SerializedName("address")
    @Expose
    private List<Address> address = null;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderIpAddress() {
        return orderIpAddress;
    }

    public void setOrderIpAddress(String orderIpAddress) {
        this.orderIpAddress = orderIpAddress;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getCreatedData() {
        return createdData;
    }

    public void setCreatedData(String createdData) {
        this.createdData = createdData;
    }

    public String getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(String orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDealPrice() {
        return productDealPrice;
    }

    public void setProductDealPrice(String productDealPrice) {
        this.productDealPrice = productDealPrice;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }

    public String getSavedPrice() {
        return savedPrice;
    }

    public void setSavedPrice(String savedPrice) {
        this.savedPrice = savedPrice;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> product) {
        this.product = product;
    }

    public List<Pic> getPics() {
        return pics;
    }

    public void setPics(List<Pic> pics) {
        this.pics = pics;
    }

    public List<UserData> getUser() {
        return user;
    }

    public void setUser(List<UserData> user) {
        this.user = user;
    }

    public List<LocationPojo> getLocation() {
        return location;
    }

    public void setLocation(List<LocationPojo> location) {
        this.location = location;
    }

    public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
}
