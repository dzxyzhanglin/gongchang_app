package com.changdu.model;

public class HomeModel extends BaseModel {

    private int icon;

    private String text;

    public HomeModel() {
    }

    public HomeModel(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
