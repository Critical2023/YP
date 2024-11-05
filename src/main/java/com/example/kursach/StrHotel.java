package com.example.kursach;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class StrHotel extends ListCell<HotelData> {
    @FXML
    private AnchorPane idContainer;

    @FXML
    private Label idCost;

    @FXML
    private Label idNum;

    @FXML
    private Label idFloor;

    @FXML
    private ImageView idPhoto;
    @FXML
    private Label idid1;

    @FXML
    private Label idid2;

    @FXML
    private Label idStat;
    String statys;

    private FXMLLoader mLLoader;
    // Метод для подгрузки данных об отелях в отдельный fxml файл
    @Override
    protected void updateItem(HotelData hotel, boolean empty) {
        super.updateItem(hotel, empty);

        if(empty || hotel == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("strstud.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            idCost.setText(Integer.toString(hotel.getCost()));
            idFloor.setText(Integer.toString(hotel.getFloor()));
            idStat.setText(hotel.getStatus());
            statys= hotel.getStatus();
            idNum.setText(Integer.toString(hotel.getNum()));
            idid1.setText(Integer.toString(hotel.getIdid()));
            idid1.setVisible(false);
// Проверка номера отеля на занятость
            if (statys!= null && statys.equals("Занят") || statys.equals("занят")) {
                idContainer.setStyle("-fx-background-color: gray;");
            } else {
                idContainer.setStyle(""); // Сброс стиля ячейки
            }
            File file = new File(hotel.getPhoto());
            try {
                String urlImage = file.toURI().toURL().toString();
                Image image = new Image(urlImage);
//                System.out.println(urlImage);
                idPhoto.setImage(image);
            } catch (MalformedURLException ignored) {}
            setGraphic(idContainer);
        }
    }
}