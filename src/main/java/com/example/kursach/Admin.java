package com.example.kursach;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Admin {
    @FXML
    private AnchorPane anchor;
    @FXML
    private Button findNomer;
    @FXML
    private Button zkz;
    @FXML
    private Button new_klient;

    @FXML
    private Button otmena;
    @FXML
    private TextField floor;
    @FXML
    private Label full;


    @FXML
    private DatePicker data_yezda;

    @FXML
    private DatePicker date_priezda;

    @FXML
    public Label fio_admin;
    @FXML
    private ListView<HotelData> list_hotel;
    @FXML
    private TextField SearchField;
    DB db = null;
    ObservableList<HotelData> hotelDataList;
    @FXML
    private ComboBox<String> idKlient;
    String x = "";


    @FXML
    void initialize() throws SQLException, ClassNotFoundException {

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

        date_priezda.getEditor().focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {

                try {
                    String dateValue = date_priezda.getEditor().getText();
                    // Проверка что поле у datapicker не пустое
                    if (dateValue != null && !dateValue.isEmpty()) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        LocalDate date = LocalDate.parse(dateValue, formatter);
                        date_priezda.setValue(date);
                    }
                } catch (DateTimeParseException e) {
                    showMessage("Ошибка!", "Введены некорректные значения (dd.mm.yyyy)!");
                    date_priezda.getEditor().clear();


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
        // установка слушателя на изменение клиента
        idKlient.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Сбрасываем значение full на 0
            full.setText("0");
        });

        db = new DB();
        loadInfo();
        ArrayList<String> list_products = db.getKlient();
        ObservableList<String> langs_products = FXCollections.observableArrayList();
        for (int i = 0; i < list_products.size(); i++) {
            langs_products.add(list_products.get(i) + "");
        }
        // Добавление всех клиентов в выпадающий список
        idKlient.setItems(langs_products);
        // Обработчик события нажатия кнопки добавления заказа

        zkz.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    // Проверка  на то чтобы у одного и того же клиента не было возможно забронировать один и то же номер
                    boolean flag = false;
                    if (!(list_hotel.getSelectionModel().getSelectedItem() == null) && !(idKlient.getSelectionModel().getSelectedItem() == null) && !(date_priezda.getValue() == null) && !(data_yezda.getValue() == null)) {
                        ArrayList<Integer> listhotelid = db.getKlienthotel(db.getidklient(idKlient.getValue()));
                        System.out.println(listhotelid);
                        if (!listhotelid.isEmpty()) {
                            for (int i = 0; i < listhotelid.size(); i++) {
                                System.out.println(listhotelid.size());
                                if (listhotelid.get(i) != list_hotel.getSelectionModel().getSelectedItem().getIdid() && i == listhotelid.size() - 1) {
                                    flag = true;
                                } else if (listhotelid.get(i) != list_hotel.getSelectionModel().getSelectedItem().getIdid() && i != listhotelid.size() - 1) {
                                    flag = false;
                                } else if (listhotelid.get(i) == list_hotel.getSelectionModel().getSelectedItem().getIdid()) {
                                    flag = false;
                                    showMessage("Ошибка!", "Нельзя добавить один и тот же номер на одного клиента");
                                } else {
                                    flag = true;
                                }
                            }
                        } else {
                            flag = true;

                        }
                    } else {
                        showMessage("Ошибка!", "Выбраны не все поля для офрмления заказа или некорректные данные");
                    }
                    // Добавление брони в базу данных

                    if (flag == true) {
                        LocalDate date1= LocalDate.of(2024,12,31);
                        //Проверка что дата уезда из номера свободна и никем не занята
                        if (db.getdatedep(list_hotel.getSelectionModel().getSelectedItem().getIdid()) == null) {
                            LocalDate date=LocalDate.now();
                            //Проверка что дата заселения не раньше чем сегодня
                                if (date_priezda.getValue().isAfter(date.minusDays(1))) {
                                    if (date_priezda.getValue().isBefore(date1)) {
                                        if (data_yezda.getValue().isBefore(date1)) {



                                        int a;
                                        Random random = new Random();
                                        a = random.nextInt(200);

                                        db.insertbron(list_hotel.getSelectionModel().getSelectedItem().getIdid(), db.getidkl(idKlient.getSelectionModel().getSelectedItem().toString()), String.valueOf(date_priezda.getValue()), String.valueOf(data_yezda.getValue()));
                                        System.out.println(list_hotel.getSelectionModel().getSelectedItem().getIdid());
                                        db.insertpay(list_hotel.getSelectionModel().getSelectedItem().getIdid(), db.getidkl(idKlient.getSelectionModel().getSelectedItem().toString()), db.costfull(db.selectdiff(list_hotel.getSelectionModel().getSelectedItem().getIdid()), db.getcost(list_hotel.getSelectionModel().getSelectedItem().getIdid())), a);

                                        a = db.getfullcost(db.getidklient(idKlient.getSelectionModel().getSelectedItem()));
                                        full.setText(Integer.toString(Integer.parseInt(full.getText()) + a));
                                        showMessage("Успех!", "Бронь сформирована!");
                                        loadInfo();
                                    }else{
                                            showMessage("Ошибка!","Дата не может быть больше 2024.12.30");
                                        }
                                }else{
                                        showMessage("Ошибка!","Дата не может быть больше 2024.12.30");
                                    }
                            }else{
                                showMessage("Ошибка!","Дата приезда не может быть меньше чем сегодня");
                            }
                            //прооверка номера на забронированность другим человеком на определенную дату
                        } else if (date_priezda.getValue().isAfter(db.getdatedep(list_hotel.getSelectionModel().getSelectedItem().getIdid()).toLocalDate())) {
                            LocalDate date=LocalDate.of(2023,10,29);
                            //Проверка что дата заселения не раньше 29.10.2023
                           if(date_priezda.getValue().isAfter(date)) {
                               int a;
                               Random random = new Random();
                               a = random.nextInt(200);

                               db.insertbron(list_hotel.getSelectionModel().getSelectedItem().getIdid(), db.getidkl(idKlient.getSelectionModel().getSelectedItem().toString()), String.valueOf(date_priezda.getValue()), String.valueOf(data_yezda.getValue()));
                               System.out.println(list_hotel.getSelectionModel().getSelectedItem().getIdid());
                               db.insertpay(list_hotel.getSelectionModel().getSelectedItem().getIdid(), db.getidkl(idKlient.getSelectionModel().getSelectedItem().toString()), db.costfull(db.selectdiff(list_hotel.getSelectionModel().getSelectedItem().getIdid()), db.getcost(list_hotel.getSelectionModel().getSelectedItem().getIdid())), a);

                               a = db.getfullcost(db.getidklient(idKlient.getSelectionModel().getSelectedItem()));
                               full.setText(Integer.toString(Integer.parseInt(full.getText()) + a));
                               showMessage("Успех!", "Бронь сформирована!");
                               loadInfo();
                           }else{
                               showMessage("Ошибка!","Дата приезда не может быть меньше чем 29.10.2023");
                           }
                        } else {
                            showMessage("Ошибка!", "На эту дату номер уже занят");
                        }
                    }
                } catch (SQLException e) {
                    x = e.getMessage();
                    showMessage("Ошибка!", "" + x);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    void loadInfo() {
        try {
            // Добавление инофрмации о номерах отеля в listview
            List<HotelData> ls = db.getHotel();
            hotelDataList = FXCollections.observableArrayList(ls);
            list_hotel.getItems().addAll(ls);
            // Наложение контекстного меню на listview
            list_hotel.setCellFactory(stringListView -> {
                ListCell<HotelData> cell = new StrHotel();
                ContextMenu contextMenu = new ContextMenu();
                MenuItem editItem = new MenuItem("Удалить");
                editItem.setOnAction(event -> {
                    HotelData item = cell.getItem();
                    try {
                        // Проверка на вычет стоимости динамически при удалении номера отеля
                        if (item.getIdid() == db.getKlienthotel1(db.getidklient(idKlient.getSelectionModel().getSelectedItem()))) {
                            full.setText(Integer.toString(Integer.parseInt(full.getText()) - db.getfullcost(db.getidklient(idKlient.getSelectionModel().getSelectedItem()))));
                        }
                        db.delete(item.getIdid());

                        hotelDataList.remove(item);
                    }
                    catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

                list_hotel.setItems(hotelDataList);

                MenuItem editItem1 = new MenuItem("Редактировать");
                editItem1.setOnAction(event -> {
                    try {
                        Stage stage=(Stage) anchor.getScene().getWindow();
                        stage.hide();
                        editItem();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                MenuItem editItem2 = new MenuItem("Добавить");
                editItem2.setOnAction(evrnt -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNomer.fxml"));
                        Stage stage = new Stage();
                        Scene scene = null;
                        try {
                            scene = new Scene(fxmlLoader.load(), 600, 400);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        stage.setTitle("Добавление номера отеля");
                        stage.setScene(scene);
                        stage.show();
                        stage.setResizable(false);
                        stage.getIcons().add(new Image("logo.jpg"));
                        Stage stage1 = (Stage) fio_admin.getScene().getWindow();
                        stage1.close();
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }

                });
                contextMenu.getItems().addAll(editItem2);
                contextMenu.getItems().addAll(editItem);
                contextMenu.getItems().addAll(editItem1);
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

        // Обработка кнопки отменить поиск
        otmena.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                otmena();
                //Очищенеие полей сортировок
                SearchField.clear();
                floor.clear();
                data_yezda.getEditor().clear();
                date_priezda.getEditor().clear();
            }
        });

        // Обработчик кнопки добавления нового клиента
        new_klient.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            Stage newClientStage = null;

            @Override
            public void handle(MouseEvent mouseEvent) {
                if (newClientStage != null && newClientStage.isShowing()) {
                    newClientStage.close();
                }
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newKlient.fxml"));
                newClientStage = new Stage();
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 600, 400);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                newClientStage.setTitle("Добавление нового клиента");
                newClientStage.setScene(scene);
                newClientStage.show();
                newClientStage.setResizable(false);
                newClientStage.getIcons().add(new Image("logo.jpg"));
                Stage stage1 = (Stage) fio_admin.getScene().getWindow();
                stage1.close();
            }
        });

        //Обработка кнопки найти нужный номер отеля по выбранным датам
        findNomer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    // Проверки на заполнение дат и чтобы дата приезда не была больше даты уезда
                    if (date_priezda.getValue() != null && data_yezda.getValue() != null) {
                        if (date_priezda.getValue().isBefore(data_yezda.getValue())) {
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

    // Метод на изменение данных об отеле
    @FXML
    void editItem() throws IOException, SQLException, ClassNotFoundException {
        // Получаем выбранный элемент из ListView
        HotelData selectedItem = list_hotel.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Создаем новое окно для редактирования элемента
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editHotel.fxml"));
            Parent root = loader.load();
            EditHotel controller = loader.getController();

            // Передаем данные выбранного элемента в контроллер формы редактирования
            controller.setHotelData(selectedItem);

            // Открываем новое окно
            Stage stage = new Stage();
            stage.setTitle("Редактирование номера");
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("logo.jpg"));
            stage.setResizable(false);
            stage.showAndWait();


            // Обновляем данные в ListView после закрытия окна редактирования
            if (!controller.isSaveClicked()) {
                hotelDataList.remove(selectedItem);
                hotelDataList.add(controller.getHotelData());
                db.update(selectedItem.getNum(), selectedItem.getStatus(), selectedItem.getCost(), selectedItem.getFloor(), selectedItem.getIdid());
                list_hotel.getSelectionModel().select(controller.getHotelData());
                loadInfo();
                Stage stage1=(Stage) anchor.getScene().getWindow();
                stage1.show();
            }
        }
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

    // Метод для сортировки номеров отеля по заданной дате
    private void updateHotelList() throws SQLException, ClassNotFoundException {
        List<HotelData> ls = db.getHotel();
        Date date1 = Date.valueOf(date_priezda.getValue());
        Date date2 = Date.valueOf(data_yezda.getValue());
        // Фильтруем список отелей по выбранным датам
        List<HotelData> filteredList = ls.stream()
                .filter(data -> {
                    try {
                        System.out.println(db.getdatedep(data.getIdid()));
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

    //метод который отменяет любые сортировки которые применялись ранее
    void otmena() {
        try {
            List<HotelData> ls = db.getHotel();
            hotelDataList = FXCollections.observableArrayList(ls);
            list_hotel.getItems().addAll(ls);
            list_hotel.setCellFactory(stringListView -> {
                ListCell<HotelData> cell = new StrHotel();
                ContextMenu contextMenu = new ContextMenu();
                MenuItem editItem = new MenuItem("Удалить");
                editItem.setOnAction(event -> {
                    HotelData item = cell.getItem();
                    try {
                        // Проверка на вычет стоимости динамически при удалении номера отеля
                        if (item.getIdid() == db.getKlienthotel1(db.getidklient(idKlient.getSelectionModel().getSelectedItem()))) {
                            full.setText(Integer.toString(Integer.parseInt(full.getText()) - db.getfullcost(db.getidklient(idKlient.getSelectionModel().getSelectedItem()))));
                        }
                        db.delete(item.getIdid());
                        hotelDataList.remove(item);

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

                list_hotel.setItems(hotelDataList);

                MenuItem editItem1 = new MenuItem("Редактировать");
                editItem1.setOnAction(event -> {
                    try {
                        Stage stage=(Stage) anchor.getScene().getWindow();
                        stage.hide();
                        editItem();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                MenuItem editItem2 = new MenuItem("Добавить");
                editItem2.setOnAction(evrnt -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addNomer.fxml"));
                        Stage stage = new Stage();
                        Scene scene = null;
                        try {
                            scene = new Scene(fxmlLoader.load(), 600, 400);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        stage.setTitle("Добавление номера отеля");
                        stage.setScene(scene);
                        stage.getIcons().add(new Image("logo.jpg"));
                        stage.show();
                        stage.setResizable(false);
                        Stage stage1 = (Stage) fio_admin.getScene().getWindow();
                        stage1.close();
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }

                });
                contextMenu.getItems().addAll(editItem2);
                contextMenu.getItems().addAll(editItem);
                contextMenu.getItems().addAll(editItem1);
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

    // метод для вохода обратно на фрорму авторизации
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
        Stage stage1 = (Stage) data_yezda.getScene().getWindow();
        stage1.close();
    }
    // Проверка на ввод числового значения
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }
}


