package com.travel.travellingbug.models;

public class IntroModel {

    private int featured_image;
    private String the_caption_Title;
    private String the_caption_Title_description;

    public IntroModel(int featured_image, String the_caption_Title, String the_caption_Title_description) {
        this.featured_image = featured_image;
        this.the_caption_Title = the_caption_Title;
        this.the_caption_Title_description = the_caption_Title_description;
    }

    public int getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(int featured_image) {
        this.featured_image = featured_image;
    }

    public String getThe_caption_Title() {
        return the_caption_Title;
    }

    public void setThe_caption_Title(String the_caption_Title) {
        this.the_caption_Title = the_caption_Title;
    }

    public String getThe_caption_Title_description() {
        return the_caption_Title_description;
    }

    public void setThe_caption_Title_description(String the_caption_Title_description) {
        this.the_caption_Title_description = the_caption_Title_description;
    }
}
