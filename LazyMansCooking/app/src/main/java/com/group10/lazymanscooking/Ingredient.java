package com.group10.lazymanscooking;

/**
 * Created by Anjelo on 13-Jan-16.
 */
public class Ingredient {
    private String id;
    private String title;

    public Ingredient(String id, String title)
    {
        this.id = id;
        this.title = title;
    }
    public String getId()
    {
        return this.id;
    }
    public void setId(String input)
    {
        this.id = input;
    }
    public String getTitle()
    {
        return this.title;
    }
    public void setTitle(String input)
    {
        this.title = input;
    }
}
