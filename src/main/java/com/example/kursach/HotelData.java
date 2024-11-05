package com.example.kursach;

public class HotelData {
    private String statys;
    private String photo;
    private Integer num;
    private Integer idid;

    private Integer cost;
    private Integer floor;

    // Метод для инициализации объекта с заданными значениями
    public HotelData(Integer num, String statys, String photo, Integer cost, Integer floor, Integer idid) {
        this.statys = statys;
        this.idid=idid;
        this.photo = photo;
        this.num = num;
        this.cost = cost;
        this.floor = floor;


    }

    // Методы для получения и установки информации об отеле
    public String getStatus() {
        return this.statys;
    }

    public Integer getIdid(){
        return  this.idid;
    }

    public Integer getCost() {
        return this.cost;
    }

    public Integer getNum() {
        return this.num;
    }

    public Integer getFloor() {
        return this.floor;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setStatus(String status) {
        this.statys = status;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    public void setIdid(Integer idid) {
        this.idid = idid;
    }
    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}




