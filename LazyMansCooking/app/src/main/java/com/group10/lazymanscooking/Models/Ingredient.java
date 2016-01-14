package com.group10.lazymanscooking.Models;

import java.io.Serializable;

/**
 * Created by Anjelo on 13-Jan-16.
 */
public class Ingredient implements Serializable {
    private String objectId;
    private String title;

    public Ingredient(String id, String title)
    {
        this.objectId = id;
        this.title = title;
    }
    public String getobjectId()
    {
        return this.objectId;
    }
    public void setobjectId(String input)
    {
        this.objectId = input;
    }
    public String getTitle()
    {
        return this.title;
    }
    public void setTitle(String input)
    {
        this.title = input;
    }

    @Override
    public String toString(){
        return "" + title;
    }
}
