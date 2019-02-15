package com.webinfrasolutions.ikarti.Models;

/**
 * Created by apple on 21/05/16.
 */
public class BeanclassOrder {

    private int image;
    private String orderday;
    private String ordername;
    private String qt;
    private String date;
    private String idnumber;

    public BeanclassOrder(int image, String orderday, String ordername, String qt, String date, String idnumber) {



        this.image = image;
        this.orderday = orderday;
        this.ordername = ordername;
        this.qt = qt;
        this.date = date;
        this.idnumber = idnumber;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getOrderday() {
        return orderday;
    }

    public void setOrderday(String orderday) {
        this.orderday = orderday;
    }

    public String getOrdername() {
        return ordername;
    }

    public void setOrdername(String ordername) {
        this.ordername = ordername;
    }

    public String getQt() {
        return qt;
    }

    public void setQt(String qt) {
        this.qt = qt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }



}

