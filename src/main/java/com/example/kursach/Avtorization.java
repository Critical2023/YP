package com.example.kursach;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Avtorization {
    public static String login;

    @FXML
    private TextField idLogin;

    @FXML
    private PasswordField idPassword;
    @FXML
    private TextField idpas2;

    @FXML
    private CheckBox pokaz;
    @FXML
    private AnchorPane root;
    String x;
    DB db = new DB();
    // Метод синхронизации для показа введенного пароля
    @FXML
    void changeVisibility() {
        if (pokaz.isSelected()) {
            idpas2.setText(idPassword.getText());
            idpas2.setVisible(true);
            idPassword.setVisible(false);
            return;
        }
        idPassword.setText(idpas2.getText());
        idPassword.setVisible(true);
        idpas2.setVisible(false);
    }

    @FXML
    void initialize() {
        idpas2.setVisible(false);
    }
    // Метод вывода всплывающих сообщений
    void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("error.png"));
        alert.showAndWait();
    }
    // Метод нажатия на иконку регистрации который перекидывает на форму регистрации
    @FXML
    void registr(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("registration.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Регистрация");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.getIcons().add(new Image("logo.jpg"));
        Stage currentStage = (Stage) idpas2.getScene().getWindow();
        currentStage.close();

    }
// Метод обработи нажатия на иконку входа
    @FXML
    void voiti(){
        try {
            // Записываем логин в статическую переменную чтобы можно было потом обратится к ней из любой части проекта
            login=idLogin.getText();
            if(pokaz.isSelected()){

                idPassword.setText(idpas2.getText());
            }
            // Проверяем является ли поле заполненным
            if (!idLogin.getText().trim().equals("") & !idPassword.getText().trim().equals("")) {
                // Проверяем к какой роли относится пользователь
                int a = db.getUser(idLogin.getText(), idPassword.getText());
                if (a == 1) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load(), 830, 550);
                    stage.setTitle("Личный кабинет: Администратора");
                    stage.setScene(scene);
                    stage.getIcons().add(new Image("logo.jpg"));
                    stage.show();
                    stage.setResizable(false);
                    Admin controller = fxmlLoader.getController();
                    x = db.getname(idLogin.getText());
                    controller.fio_admin.setText(x);
                    Stage stage1=(Stage) idpas2.getScene().getWindow();
                    stage1.close();
                }
                // Если пользователь не относится ни к какой роли то значит данные которые он вводит не верны
                if (a == 0) {
                    showMessage("Ошибка!","Неверные данные");
                }
                if (a == 2) {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load(), 730, 480);
                    stage.setTitle("Личный кабинет: Клиента");
                    stage.setScene(scene);
                    stage.show();
                    stage.setResizable(false);
                    stage.getIcons().add(new Image("logo.jpg"));
                    Stage stage1=(Stage) idpas2.getScene().getWindow();
                    stage1.close();
                    HelloController controller = fxmlLoader.getController();
                    x = db.getname(idLogin.getText());
                    controller.fio_polz.setText(x);
                }
            }else {
                showMessage("Ошибка!","Неверные данные");
            }
        }

        catch (SQLException e) { // Отслеживаем ошибки
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

