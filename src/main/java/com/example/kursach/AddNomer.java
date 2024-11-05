package com.example.kursach;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddNomer {

    @FXML
    private Button exit;

    @FXML
    private TextField idCost;

    @FXML
    private TextField idFloor;

    @FXML
    private TextField idNum;

    @FXML
    private ComboBox<String> idStat;

    @FXML
    private TextField id_photo;
ObservableList<String> studentDataList;
    @FXML
    private Button save;
    String a = null;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        // делаем неактивной field для ввода пути для фото и вносим значение статуса в combobox
        DB db = new DB();
        id_photo.setDisable(true);
        ArrayList<String> ls = db.setHotelStatys();
        idStat.getItems().addAll(ls);

// Обработчик нажатия кнопки сохранить
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    // Проверка на пустые значения полей для создания нового номера отеля
                    if (!idNum.getText().trim().equals("") && !idCost.getText().trim().equals("") &&
                            idStat.getSelectionModel().getSelectedItem() != null&& !idFloor.getText().trim().equals("")) {
                        if(isNumeric(idNum.getText()) && Integer.parseInt(idNum.getText())>0) {
                            if(isNumeric(idFloor.getText()) && Integer.parseInt(idFloor.getText())>0 && Integer.parseInt(idFloor.getText())<15) {
                                if (isNumeric(idCost.getText()) && Integer.parseInt(idCost.getText()) > 0 && Integer.parseInt(idFloor.getText()) < 100000) {

                                // Проверка на то что добавили путь для фото
                                if (id_photo.getText().trim().equals("")) {
                                    db.insertHotel(db.getmaxid(), Integer.parseInt(idNum.getText()), Integer.parseInt(idCost.getText()), idStat.getSelectionModel().getSelectedItem(),
                                            Integer.parseInt(idFloor.getText()));
                                } else {
                                    db.insertHotel(db.getmaxid(), Integer.parseInt(idNum.getText()), Integer.parseInt(idCost.getText()), idStat.getSelectionModel().getSelectedItem(),
                                            Integer.parseInt(idFloor.getText()), id_photo.getText());
                                }
                                //Очищаем значения полей после сохраниения значений
                                idStat.setValue("");
                                id_photo.clear();
                                idFloor.clear();
                                idNum.clear();
                                idCost.clear();
                            }else{
                                    showMessage("Ошибка","Некорректный формат данных для  стоимости");
                                }
                            }else{
                                showMessage("Ошибка","Некорректный формат данных для  этажа");
                            }
                        }else{
                            showMessage("Ошибка","Некорректный формат данных для номера комнаты");
                        }
                    }else{
                        showMessage("Ошибка!","Заполнены не все поля");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        // Обраотчик кнопки выхода
        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
                Stage stage = new Stage();
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 830, 550);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("Добавление номера отеля");
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
                stage.getIcons().add(new Image("logo.jpg"));
                Stage stage1=(Stage) exit.getScene().getWindow();
                stage1.close();
            }
        });

    }

// Метод который открывает окно выбора фотографий с компьютера
    @FXML
    private void handleChoosePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        // Проверка на то выбран путь или нет
        if (selectedFile != null) {
            id_photo.setText(selectedFile.getAbsolutePath());
        }
    }
    // Метод на вывод всплывающих сообщений
    void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("error.png"));
        alert.showAndWait();
    }
    // Проверка на ввод числа
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }
}
