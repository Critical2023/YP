package com.example.kursach;

import java.sql.*;
import java.util.ArrayList;

public class DB {

    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:13306/kursach", "javafxTest", "changeme");
        return con;
    }

    // Метод на получение роли пользователя
    public int getUser(String log, String pass) throws SQLException, ClassNotFoundException {
        String sql = "SELECT role_idrole,count(*) as n FROM user where login=? and password=? group by  role_idrole";

        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setString(1, log);
        statement.setString(2, pass);
        ResultSet res = statement.executeQuery();
        int role = 0;
        while (res.next()) {

            role = res.getInt("role_idrole");
        }
        return role;
    }

    // Метод на получение id клиента по его имени
    public Integer getidkl(String name) throws SQLException, ClassNotFoundException {
        String sql = "select idklient from klient where FIO='" + name + "'";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prst.executeQuery();
        int id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt("idklient");
        }
        return id;
    }

    // Метод на получение имени пользователя по его логину
    public String getname(String login) throws SQLException, ClassNotFoundException {
        String sql = "Select name_user from user where login='" + login + "' ";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        String name = "";
        while (resultSet.next()) {
            name = resultSet.getString("name_user");
        }
        return name;
    }

    // Метод на получение максимального id комнаты отеля +1
    public int getmaxid() throws SQLException, ClassNotFoundException {
        String sql = "SELECT MAX(idhotel_room) + 1 AS idhotel_room FROM hotel_room";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();
        int id = 0;
        if (resultSet.next()) {
            id = resultSet.getInt("idhotel_room");
        }
        return id;
    }

    // Метод на получение стоимости проивания в отеле за день по id комнаты отеля
    public int getcost(Integer idid) throws SQLException, ClassNotFoundException {
        String sql = "select cost_for_day from hotel_room where idhotel_room='" + idid + "'";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prst.executeQuery();
        int cost = 0;
        while (resultSet.next()) {
            cost = resultSet.getInt("cost_for_day");
        }
        return cost;

    }

    // Метод на получение всех имен клиентов
    public ArrayList<String> getKlient() throws SQLException, ClassNotFoundException {
        String sql = "SELECT FIO FROM klient;";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        ArrayList<String> klients = new ArrayList<>();
        while (res.next()) {
            klients.add(res.getString("FIO"));
        }
        return klients;
    }

    // Метод на получение id всех номеров отеля которые  забронировал определенный клиент
    public ArrayList<Integer> getKlienthotel(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT hotel_room_idhotel_room  FROM bronirovanie where klient_idklient='" + id + "'";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        ArrayList<Integer> rooms = new ArrayList<>();
        while (res.next()) {
            rooms.add(res.getInt("hotel_room_idhotel_room"));
        }
        return rooms;
    }

    //Метод на получения списка всех логинов у пользователей
    public ArrayList<String> getKlietlogin() throws SQLException, ClassNotFoundException {
        String sql = "SELECT login  FROM user";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        ArrayList<String> log = new ArrayList<>();
        while (res.next()) {
            log.add(res.getString("login"));
        }
        return log;
    }

    // Метод на получение id  номера отеля который он только что забронировал
    public Integer getKlienthotel1(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT hotel_room_idhotel_room  FROM bronirovanie where klient_idklient='" + id + "'";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        Integer rooms = 0;
        while (res.next()) {
            rooms = (res.getInt("hotel_room_idhotel_room"));
        }
        return rooms;
    }

    //Метод на установку в combobox значения свободен
    public ArrayList<String> setHotelStatys() throws SQLException, ClassNotFoundException {

        ArrayList<String> rooms = new ArrayList<>();

        rooms.add("Свободен");

        return rooms;
    }

    // Метод на вставку в таблицу бронирования
    public void insertbron(Integer idroom, Integer idklient, String datapr, String dataye) throws SQLException, ClassNotFoundException {
        String sql = "insert into bronirovanie(hotel_room_idhotel_room,klient_idKlient,Date_arrival,Date_of_departure) values(?,?,?,?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, idroom);
        prst.setInt(2, idklient);
        prst.setString(3, datapr);
        prst.setString(4, dataye);
        prst.executeUpdate();

    }

    // Метод на получение количества дней между датой заселения и уезда из номера отеля
    public int selectdiff(Integer idhotel) throws SQLException, ClassNotFoundException {
        String sql = "SELECT DAtediff(Date_of_departure,Date_arrival)as count  \n" +
                "FROM bronirovanie\n" +
                "WHERE hotel_room_idhotel_room = '" + idhotel + "'";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prst.executeQuery();
        int counts = 0;
        while (resultSet.next()) {
            counts = resultSet.getInt("count");
        }
        return counts;
    }

    // Метод на получение полной стоимости проживания в отеле
    public Integer costfull(Integer cost, Integer diff) throws SQLException, ClassNotFoundException {
        int a = 0;
        CallableStatement proc = getDbConnection().prepareCall("{call fullcost(?,?)}");
        proc.setInt(1, cost);
        proc.setInt(2, diff);
        ResultSet res = proc.executeQuery();
        while (res.next()) {
            a = res.getInt("result");
        }
        return a;
    }

    // Метод на вставку в таблицу оплаты
    public void insertpay(Integer idroom, Integer idklient, Integer cost, Integer kvitan) throws SQLException, ClassNotFoundException {
        String sql = "insert into payment(hotel_room_idhotel_room,klient_idKlient,cost_full,receipt_number) values(?,?,?,?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setInt(1, idroom);
        prst.setInt(2, idklient);
        prst.setInt(3, cost);
        prst.setInt(4, kvitan);
        prst.executeUpdate();

    }

    //Метод на вставку в таблицу пользователей новых пользователей
    public void insertuser(String name, String login, String password) throws SQLException, ClassNotFoundException {
        String sql = "insert into user(name_user,login,password) values(?,?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setString(1, name);
        prSt.setString(2, login);
        prSt.setString(3, password);
        prSt.executeUpdate();
    }

    // Метод на добавление новыз клиентов в таблицу клиенты
    public void insertklient(String fio, String pasport) throws SQLException, ClassNotFoundException {
        String sql = "insert into klient(FIO,Passport) values(?,?)";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        prst.setString(1, fio);
        prst.setString(2, pasport);
        prst.executeUpdate();
    }

    // Метод на редактирование полей в определеном номере отеля
    public void update(Integer num, String statys, Integer cost, Integer floor, Integer id) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE hotel_room SET num_room = ?, statys = ? ,cost_for_day=? ,floor=? WHERE idhotel_room ='" + id + "' ";
        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setInt(1, num);
        prSt.setString(2, statys);
        prSt.setInt(3, cost);
        prSt.setInt(4, floor);
        prSt.executeUpdate();
    }

    // Метод на добавление нового номера отеля если фотография есть
    public void insertHotel(Integer id, Integer num, Integer cost, String statys, Integer floor, String photo) throws SQLException, ClassNotFoundException {
        String sql = "insert into hotel_room(idhotel_room,num_room,cost_for_day,statys,floor,photo) values(?,?,?,?,?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setInt(1, id);
        prSt.setInt(2, num);
        prSt.setInt(3, cost);
        prSt.setString(4, statys);
        prSt.setInt(5, floor);
        prSt.setString(6, photo);
        System.out.println(prSt);
        prSt.executeUpdate();

    }

    // Меотд на добавдение нового номера отеля если нет фотографии
    public void insertHotel(Integer id, Integer num, Integer cost, String statys, Integer floor) throws SQLException, ClassNotFoundException {
        String sql = "insert into hotel_room(idhotel_room,num_room,cost_for_day,statys,floor) values(?,?,?,?,?)";
        PreparedStatement prSt = getDbConnection().prepareStatement(sql);
        prSt.setInt(1, id);
        prSt.setInt(2, num);
        prSt.setInt(3, cost);
        prSt.setString(4, statys);
        prSt.setInt(5, floor);
        prSt.executeUpdate();
    }

    // Метод на удаление номера отеля из бд
    public void delete(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM hotel_room WHERE idhotel_room = ?";
        PreparedStatement statement = getDbConnection().prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    // Метод на получение всей информации о номерах отеля
    public ArrayList<HotelData> getHotel() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM hotel_room";
        Statement statement = getDbConnection().createStatement();
        ResultSet res = statement.executeQuery(sql);
        ArrayList<HotelData> prod = new ArrayList<>();
        while (res.next()) {
            prod.add(new HotelData(res.getInt("num_room"), res.getString("statys"),
                    res.getString("photo"), res.getInt("cost_for_day"), res.getInt("floor"), res.getInt("idhotel_room")));
        }

        return prod;
    }

    // Метод на получение полной стоимости которую должен заплатить определенный клиент
    public int getfullcost(Integer idkl) throws SQLException, ClassNotFoundException {
        String sql = "select cost_full from payment where klient_idklient ='" + idkl + "'";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prst.executeQuery();
        int cost = 0;
        while (resultSet.next()) {
            cost = resultSet.getInt("cost_full");
        }
        return cost;
    }

    // Метод на получение id клиента по его имени
    public int getidklient(String nameklient) throws SQLException, ClassNotFoundException {
        String sql = "select idklient from klient where FIO ='" + nameklient + "'";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        ResultSet resultSet = prst.executeQuery();
        int id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt("idklient");
        }
        return id;
    }

    // Метод на получение даты уезда для определенного номера отеля
    public Date getdatedep(Integer hotelid) throws SQLException, ClassNotFoundException {
        String sql = "select date_of_departure from bronirovanie where hotel_room_idhotel_room='" + hotelid + "'";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        System.out.println(prst);
        ResultSet resultSet = prst.executeQuery();
        Date data = null;
        while (resultSet.next()) {
            data = resultSet.getDate("date_of_departure");
        }
        return data;
    }

    public Date getdatearr(Integer hotelid) throws SQLException, ClassNotFoundException {
        String sql = "select date_arrival from bronirovanie where hotel_room_idhotel_room='" + hotelid + "'";
        PreparedStatement prst = getDbConnection().prepareStatement(sql);
        System.out.println(prst);
        ResultSet resultSet = prst.executeQuery();
        Date data = null;
        while (resultSet.next()) {
            data = resultSet.getDate("date_arrival");
        }
        return data;
    }
}




