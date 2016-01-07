package com.group10.lazymanscooking;

import java.io.Serializable;

/**
 * Created by Anjelo on 06-Jan-16.
 */
public class Recipe implements Serializable{
    private String objectId;
    private String title;

    public Recipe(String id, String title)
    {
        this.objectId = id;
        this.title = title;
    }

    public String getObjectId()
    {
        return this.objectId;
    }
    public String getTitle()
    {
        return this.title;
    }
    public void setObjectId(String id)
    {
        this.objectId = id;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    @Override
    public String toString(){
        return "" + title;
    }
}
