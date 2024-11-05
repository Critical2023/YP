package com.example.kursach;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class EditHotel {

    @FXML
    private Button exit;

    @FXML
    private TextField idCost;

    @FXML
    private TextField idFloor;

    @FXML
    private TextField idNum;

    @FXML
    private TextField idStat;
    private HotelData hotelData;
    private boolean saveClicked=false;
    @FXML
    void initialize(){
        // Обработка нажатия кнопки выхода
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(!idNum.getText().trim().equals("") && !idCost.getText().trim().equals("") && !idStat.getText().trim().equals("")&&!idFloor.getText().trim().equals("") ) {
                    if(isNumeric(idNum.getText()) && Integer.parseInt(idNum.getText())>0) {
                        if(isNumeric(idFloor.getText()) && Integer.parseInt(idFloor.getText())>0 && Integer.parseInt(idFloor.getText())<15) {
                            if (isNumeric(idCost.getText()) && Integer.parseInt(idCost.getText()) > 0 && Integer.parseInt(idFloor.getText()) < 100000) {
                                isSaveClicked();
                                Stage current = (Stage) exit.getScene().getWindow();
                                current.close();
                            }else{
                                showMessage("Ошибка!","некорректный формат данных для стоимости");
                            }
                        }else{
                            showMessage("Ошибка!","некорректный формат данных для этажа");
                        }
                    }else{
                        showMessage("Ошибка","Некорректный формат данных для номера комнаты");

                    }                }else{
                 showMessage("Ошибка!","Ни одно поле не может быть пустым");
                }
            }
        });
    }
    public void setHotelData(HotelData hotelData) {
        this.hotelData = hotelData;
        // Отображаем данные выбранного элемента в текстовых полях формы редактирования
        idNum.setText(Integer.toString(hotelData.getNum()));
        idStat.setText(hotelData.getStatus());
        idCost.setText(Integer.toString(hotelData.getCost()));
        idFloor.setText(Integer.toString(hotelData.getFloor()));
    }
    public HotelData getHotelData() {
        // Получаем данные из текстовых полей формы редактирования
        hotelData.setNum(Integer.valueOf(idNum.getText()));
        hotelData.setStatus(idStat.getText());
        hotelData.setCost(Integer.valueOf(idCost.getText()));
        hotelData.setFloor(Integer.valueOf(idFloor.getText()));
        return hotelData;
    }
    // Метод который будет проверять нажатие кнопки сохранения
    public boolean isSaveClicked() {return saveClicked;
    }
    // Метод вывода окна сообщения
    void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("error.png"));
        alert.showAndWait();
    }
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");}
}
