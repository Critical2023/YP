package com.example.kursach;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelloController {

    @FXML
    private TextField SearchField;

    @FXML
    private DatePicker data_priezda;

    @FXML
    private Button findNomer;

    @FXML
    public Label fio_polz;
    @FXML
    private DatePicker data_yezda;

    @FXML
    private TextField floor;
    @FXML
    private Button otmena;

    ObservableList<HotelData> hotelDataList;
    @FXML
    private ListView<HotelData> list_hotel;
DB db =new DB();
@FXML
void initialize(){
    data_yezda.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
        if (!newVal) {
            try {
                String dateValue = data_yezda.getEditor().getText();
                // Проверка что поле у datapicker не пустое
                if (dateValue != null && !dateValue.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate date = LocalDate.parse(dateValue, formatter);
                    data_yezda.setValue(date);
                }
            } catch (Exception e) {


                showMessage("Ошибка!", "Введены некорректные значения(dd.mm.yyyy)!");

                data_yezda.getEditor().clear();
            }
        }
    });

    data_priezda.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
        if (!newVal) {

            try {
                String dateValue = data_priezda.getEditor().getText();
                // Проверка что поле у datapicker не пустое
                if (dateValue != null && !dateValue.isEmpty()) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate date = LocalDate.parse(dateValue, formatter);
                    data_priezda.setValue(date);
                }
            } catch (DateTimeParseException e) {
                showMessage("Ошибка!", "Введены некорректные значения (dd.mm.yyyy)!");
                data_priezda.getEditor().clear();


            }
        }

    });

// поиск номеро по стоимости
    SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
        String searchText = SearchField.getText().toLowerCase();
        String searchText1 = floor.getText().toLowerCase();
        // Проверка если оба значения в сортировке по этажу и цене не пустые и введены числа
        if (!searchText.isEmpty() && !searchText1.isEmpty() && isNumeric(searchText) && isNumeric(searchText1)) {
            List<HotelData> searchResults = hotelDataList.stream()
                    .filter(product -> product.getCost() <= Integer.parseInt(searchText))
                    .filter(product -> product.getFloor() == Integer.parseInt(floor.getText().toLowerCase()))
                    .sorted(Comparator.comparing(HotelData::getCost))
                    .collect(Collectors.toList());
            list_hotel.setItems(FXCollections.observableArrayList(searchResults));
            // Проверка что одно из полей пустое а второе нет и в него введено число
        } else if (!searchText.isEmpty() && searchText1.isEmpty() && isNumeric(searchText)) {
            List<HotelData> searchResults = hotelDataList.stream()
                    .filter(product -> product.getCost() <= Integer.parseInt(searchText))
                    .sorted(Comparator.comparing(HotelData::getCost))
                    .collect(Collectors.toList());
            list_hotel.setItems(FXCollections.observableArrayList(searchResults));
            //Проверка что вводимые значения в сортировки не пустые но введенны не числа
        } else if ((!isNumeric(searchText1) && !searchText1.isEmpty()) || (!isNumeric(searchText) && !searchText.isEmpty())) {
            showMessage("Ошибка!","Нужно вводить только числа");
            SearchField.clear();
            floor.clear();
            //Проверка что оба поля сортировок пустые
        } else if (searchText1.isEmpty() && searchText.isEmpty()) {
            list_hotel.setItems(hotelDataList);
            // Проверка что одно из полей пустое а второе нет и в него введено число
        } else if (searchText.isEmpty() && !searchText1.isEmpty() && isNumeric(searchText1)) {
            List<HotelData> searchResults = hotelDataList.stream().filter(product -> product.getFloor() == Integer.parseInt(searchText1))
                    .collect(Collectors.toList());
            list_hotel.setItems(FXCollections.observableArrayList(searchResults));
        }
    });

    //поиск номеров по этажу проживания

    floor.textProperty().addListener((observable, oldValue, newValue) -> {
        String searchText = SearchField.getText().toLowerCase();
        String searchText1 = floor.getText().toLowerCase();
        // Проверка если оба значения в сортировке по этажу и цене не пустые и введены числа
        if (!searchText1.isEmpty() && !searchText.isEmpty() && isNumeric(searchText) && isNumeric(searchText1)) {
            List<HotelData> searchResults = hotelDataList.stream()
                    .filter(product -> product.getFloor() == Integer.parseInt(searchText1))
                    .filter(product -> product.getCost() <= Integer.parseInt(SearchField.getText().toLowerCase()))
                    .collect(Collectors.toList());
            list_hotel.setItems(FXCollections.observableArrayList(searchResults));
            // Проверка что одно из полей пустое а второе нет и в него введено число
        } else if (!searchText1.isEmpty() && searchText.isEmpty() && isNumeric(searchText1)) {
            List<HotelData> searchResults = hotelDataList.stream().filter(product -> product.getFloor() == Integer.parseInt(searchText1))
                    .collect(Collectors.toList());
            list_hotel.setItems(FXCollections.observableArrayList(searchResults));
            //Проверка что вводимые значения в сортировки не пустые но введенны не числа
        } else if ((!isNumeric(searchText1) && !searchText1.isEmpty()) || (!isNumeric(searchText) && !searchText.isEmpty())) {
            showMessage("Ошибка!","Нужно вводить только числа");
            floor.clear();
            SearchField.clear();
            // Проврка что оба поля сортировок пустые
        } else if (searchText1.isEmpty() && searchText.isEmpty()) {
            list_hotel.setItems(hotelDataList);
            // Проверка что одно из полей пустое а второе нет и в него введено число
        } else if (searchText1.isEmpty() && !searchText.isEmpty() && isNumeric(searchText)) {
            List<HotelData> searchResults = hotelDataList.stream()
                    .filter(product -> product.getCost() <= Integer.parseInt(searchText))
                    .sorted(Comparator.comparing(HotelData::getCost))
                    .collect(Collectors.toList());
            list_hotel.setItems(FXCollections.observableArrayList(searchResults));
        }
    });


    loadInfo();
    // Обработка нажатия на кнопку отменить поиск
    otmena.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            otmena();
            // Очищаем поля сортировок
            SearchField.clear();
            floor.clear();
            data_yezda.getEditor().clear();
            data_priezda.getEditor().clear();
        }
    });

}
    void loadInfo() {
        try {
            // Заполнение listview информацией об номерах отелей
            List<HotelData> ls = db.getHotel();
            hotelDataList = FXCollections.observableArrayList(ls);
            list_hotel.getItems().addAll(ls);
            list_hotel.setCellFactory(stringListView -> {
                ListCell<HotelData> cell = new StrHotel();
                ContextMenu contextMenu = new ContextMenu();
                list_hotel.setItems(hotelDataList);
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(contextMenu);
                    }
                });
                return cell;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Обработчик нажатия на кнопку найти номер (по датам)
        findNomer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    // Проверки на заполнение дат и чтобы дата приезда не была больше даты уезда
                    if (data_priezda.getValue() != null && data_yezda.getValue() != null) {
                        if (data_priezda.getValue().isBefore(data_yezda.getValue())) {
                            updateHotelList();
                        } else {
                            showMessage("Ошибка!", "Дата приезда не может быть больше даты уезда");
                        }
                    } else {
                        showMessage("Ошибка!", "Заполните все поля поска по дате");

                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    // Метод для сортировки номеров отеля по заданной дате
    private void updateHotelList() throws SQLException, ClassNotFoundException {
        List<HotelData> ls = db.getHotel();
        Date date1 = Date.valueOf(data_priezda.getValue());
        Date date2 = Date.valueOf(data_yezda.getValue());

        // Фильтруем список отелей по выбранным датам
        List<HotelData> filteredList = ls.stream()
                .filter(data -> {
                    try {
                        return (data.getStatus().equals("Свободен") || (data.getStatus().equals("Занят") && date1.after(db.getdatedep(data.getIdid())) && date2.after(date1)) || (data.getStatus().equals("Занят") && date1.before(db.getdatearr(data.getIdid()))) && date2.before(db.getdatearr(data.getIdid())));

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        // Обновляем отображаемый список отелей
        list_hotel.getItems().setAll(filteredList);
    }

    // Метод проверки на то является ли введенное значение числом
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }

    // Метод который отменяет все сортировки примененные ранее
    void otmena() {
        try {



            List<HotelData> ls = db.getHotel();
            hotelDataList = FXCollections.observableArrayList(ls);
            list_hotel.getItems().addAll(ls);
            list_hotel.setCellFactory(stringListView -> {
                ListCell<HotelData> cell = new StrHotel();
                ContextMenu contextMenu = new ContextMenu();
                list_hotel.setItems(hotelDataList);
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty) {
                        cell.setContextMenu(null);
                    } else {
                        cell.setContextMenu(contextMenu);
                    }
                });
                return cell;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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
    // Метод который переносит пользователя обратно на окно авторизации
    @FXML
    public void exit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("avtorization.fxml"));
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Авторизация");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.getIcons().add(new Image("logo.jpg"));
        Stage stage1=(Stage) data_yezda.getScene().getWindow();
        stage1.close();
    }
    }

