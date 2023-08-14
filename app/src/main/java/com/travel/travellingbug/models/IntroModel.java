package com.travel.travellingbug.models;

public class IntroModel {

    private String featured_image;
    private int featured_image_local;
    private String the_caption_Title;
    private String the_caption_Title_description;



    public IntroModel() {
    }

    public IntroModel(String featured_image, String the_caption_Title, String the_caption_Title_description) {
        this.featured_image = featured_image;
        this.the_caption_Title = the_caption_Title;
        this.the_caption_Title_description = the_caption_Title_description;
    }

    public IntroModel(int featured_image_local, String the_caption_Title, String the_caption_Title_description) {
        this.featured_image_local = featured_image_local;
        this.the_caption_Title = the_caption_Title;
        this.the_caption_Title_description = the_caption_Title_description;
    }

    public int getFeatured_image_local() {
        return featured_image_local;
    }

    public void setFeatured_image_local(int featured_image_local) {
        this.featured_image_local = featured_image_local;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
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
