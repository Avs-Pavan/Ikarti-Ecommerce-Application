package com.webinfrasolutions.ikarti.Adapters;

/**
 * Created by kevin on 17/10/17.
 */

public class CategorySpinModel {
    private String Category="";
    private String Image="";
   // private String code="";

    /*********** Set Methods ******************/
    public void setCategory(String CompanyName)
    {
        this.Category = CompanyName;
    }

    public void setImage(String Image)
    {
        this.Image = Image;
    }


    /*********** Get Methods ****************/
    public String getCategory()
    {
        return this.Category;
    }

    public String getImage()
    {
        return this.Image;
    }



}