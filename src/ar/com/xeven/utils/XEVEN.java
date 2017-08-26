package ar.com.xeven.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**
 * Created by pacevedo on 5/08/17.
 */
public class XEVEN {

    public enum status {
        newOrder("Nuevo","New"),
        confirmedOrder("Confirmado","Confirmed"),
        preparedOrder("Preparado","Prepared"),
        deliveredOrder("Entregado","Delivered"),
        cancelledOrder("Cancelado","Cancelled");

        private String english;
        private String spanish;

        public String getEnglish() {
            return english;
        }

        public String getSpanish() {
            return spanish;
        }

        status(String spanish, String english) {
            this.spanish = spanish;
            this.english = english;
        }
    }

    public static String generateID(String prefix) {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("mmss");
        return prefix + sdf.format(date);
    }

    private static Connection con = null;

    public static Connection getConnection() {
        try {
            if (con == null) {
                Runtime.getRuntime().addShutdownHook(new MyShDwnHook());
                ResourceBundle rb = ResourceBundle.getBundle("db");
                String driver = rb.getString("driver");
                String url = rb.getString("url");
                String pwd = rb.getString("pwd");
                String usr = rb.getString("usr");

                Class.forName(driver);
                con = DriverManager.getConnection(url, usr, pwd);
            }
            return con;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("La conexión no pudo ser establecida", e);
        }
    }

    static class MyShDwnHook extends Thread {
        public void run() {
            try {
                Connection con = XEVEN.getConnection();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error al cerrar la conexión", e);
            }
        }
    }
}