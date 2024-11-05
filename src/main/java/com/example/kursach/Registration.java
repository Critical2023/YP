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
import java.util.ArrayList;

public class Registration {
    @FXML
    private TextField fio;
    @FXML
    private TextField passport;

    @FXML
    private TextField login;

    @FXML
    private TextField password;

    @FXML
    private Button regist;

    DB db = new DB();

    @FXML
    void initialize() {
        //Обработчик нажатия кнопки зарегистрироваться
        regist.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Проверка на повтор логина
                boolean flag = true;
                ArrayList<String> ar1 = null;
                try {
                    ar1 = db.getKlietlogin();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < ar1.size(); i++) {
                    if (login.getText().trim().equals(ar1.get(i))) {
                        flag=false;
                        break;
                    }

                }
                // Проверка на пустые значения полей ввода
                if (!fio.getText().trim().equals("") && !login.getText().trim().equals("") && !password.getText().trim().equals("") && !passport.getText().trim().equals("")) {
                    if (flag == true) {
                        try {
                            // Вставка в таблицы бд
                            db.insertuser(fio.getText(), login.getText(), password.getText());
                            db.insertklient(fio.getText(), passport.getText());
                            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("avtorization.fxml"));
                            Stage stage = new Stage();
                            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                            stage.setTitle("Авторизация");
                            stage.setScene(scene);
                            stage.show();
                            stage.setResizable(false);
                            Stage stage1 = (Stage) regist.getScene().getWindow();
                            stage1.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        showMessage("Ошибка!", "Логин уже занят выбери другой");
                    }
                } else {
                    showMessage("Ошибка!", "Заполните все поля!");
                }
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
}
