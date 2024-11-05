package com.example.kursach;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class NewKlient {
    @FXML
    private TextField fio_klient;

    @FXML
    private TextField passport;

    @FXML
    private Button save;

    @FXML
    private Button update;
    DB db = new DB();

    @FXML
    void initialize() {
        // Делаем изначально кнопку применить измменения неактивной
        update.setDisable(true);
        // Обработчик на кнопку сохранить
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    // Проверка на пустые значения
                    if (!fio_klient.getText().trim().equals("") && !passport.getText().trim().equals("")) {
                        if(!isNumeric(fio_klient.getText())) {
                            if(isNumeric(passport.getText())) {
                                db.insertklient(fio_klient.getText(), passport.getText());
                                fio_klient.clear();
                                passport.clear();
                                update.setDisable(false);
                            }else{
                                showMessage("Ошибка!","Некорректный формат для паспорта");
                            }
                        }else{
                            showMessage("Ошибка!","Некорректный формат для ФИО");
                        }
                    } else {
                        showMessage("Ошибка!", "Введены не все значения");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Обработчик нажатия кнопки приментить изменения
        update.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
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
                stage.setTitle("Личный кабинет: Администратора");
                stage.setScene(scene);
                stage.show();
                stage.setResizable(false);
                Avtorization avtorization=new Avtorization();
                Admin admin=fxmlLoader.getController();
                try {
                    // Устанавливаем фио админа из логина который сохраняли в статическую переменную
                    admin.fio_admin.setText(db.getname(avtorization.login));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Stage stage1=(Stage) save.getScene().getWindow();
                stage1.close();
            }
        });

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
    // Проверка на числовое значение
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");}
}
