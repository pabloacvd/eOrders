package ar.com.xeven.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        return prefix + Math.round(Math.random()*1000);
    }

    private static Connection con = null;

    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                Runtime.getRuntime().addShutdownHook(new MyShDwnHook());
                ResourceBundle rb = ResourceBundle.getBundle("resources.db");
                String driver = rb.getString("driver");
                String url = rb.getString("url");
                String pwd = rb.getString("pwd");
                String usr = rb.getString("usr");
                Class.forName(driver);
                con = DriverManager.getConnection(url, usr, pwd);
            }
            return con;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("La conexión no pudo ser establecida con el driver.", e);
        } catch (SQLException e) {
            System.out.println("La conexión no pudo ser establecida con la base de datos. Reintentando.");
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost/eorders", "root", "");
            } catch (SQLException ex) {
                throw new RuntimeException("La conexión no pudo ser establecida con el driver.", ex);
            }
            return con;
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