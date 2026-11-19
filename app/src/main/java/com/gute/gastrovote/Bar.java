package com.gute.gastrovote;

import android.net.Uri;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Bar implements Serializable {
    private String barName;
    private String tapaName;
    private String tapaDesc;
    private double precio;
    private boolean done, fav;
    private float rating;
    private String review = "";
    private String telf;
    private Uri web;
    private Uri geo;




    //Constructors
    public Bar (String barName, String tapa, double precio){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = "";
        this.precio = precio;
        this.done = false;
        this.fav = false;
        this.rating = 0.0f;
    }
    public Bar (String barName, String tapa, double precio, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = "";
        this.precio = precio;
        this.done = false;
        this.fav = false;
        this.rating = rating;
    }
    public Bar (String barName, String tapa, double precio, boolean fav, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = "";
        this.precio = precio;
        this.done = false;
        this.fav = fav;
        this.rating = rating;
    }
    public Bar (String barName, String tapa, String desc, double precio){
        this.barName = barName;
        this.tapaName = tapa;
        this.precio = precio;
        this.tapaDesc = desc;
        this.done = false;
        this.fav = false;
        this.rating = 0.0f;
    }
    public Bar (String barName, String tapa, String desc, double precio, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.precio = precio;
        this.tapaDesc = desc;
        this.done = false;
        this.fav = false;
        this.rating = rating;
    }
    public Bar (String barName, String tapa, String desc, double precio, boolean fav, float rating){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = desc;
        this.precio = precio;
        this.done = false;
        this.fav = fav;
        this.rating = rating;
    }
    public Bar (String barName, String tapa, String desc, double precio, boolean done, boolean fav, float rating, String telf, Uri web, Uri geo){
        this.barName = barName;
        this.tapaName = tapa;
        this.tapaDesc = desc;
        this.precio = precio;
        this.done = done;
        this.fav = fav;
        this.rating = rating;
        this.telf = telf;
        this.web = web;
        this.geo = geo;
    }

    // Getters y setters
    public String getBarName() {
        return barName;
    }
    public void setBarName(String barName) {
        this.barName = barName;
    }
    public String getTapaName() {
        return tapaName;
    }
    public void setTapaName(String tapaName) {
        this.tapaName = tapaName;
    }
    public String getTapaDesc() {
        return tapaDesc;
    }
    public void setTapaDesc(String tapaDesc) {
        this.tapaDesc = tapaDesc;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
    public boolean isFav() {
        return fav;
    }
    public void setFav(boolean fav) {
        this.fav = fav;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }
    public String getTelf() {
        return telf;
    }
    public Uri getTelfUri(){
        String[] digits = this.telf.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String digit : digits) {
            sb.append(digit);
        }
        return Uri.parse("tel:" + sb);
    }
    public void setTelf(String telf) {
        this.telf = telf;
    }
    public Uri getWeb() {
        return web;
    }
    public void setWeb(Uri web) {
        this.web = web;
    }
    public Uri getGeo() {
        return geo;
    }
    public void setGeo(Uri geo) {
        this.geo = geo;
    }
}
