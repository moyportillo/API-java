package server;

import server.Handlers.RootHttpHandler;
import com.sun.net.httpserver.HttpServer;
import server.Handlers.UserLoginHeadler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ServerHttp {
    public static Connection conn;

    public static void main(String[] args) {

        try{
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

            server.createContext("/", new RootHttpHandler());
            server.createContext("/user/login", new UserLoginHeadler());
            server.start();
            initdb();

            System.out.println("Servidor iniciado en el puerto 8080");
        }
        catch (IOException ex){
            System.out.println(ex.getStackTrace());
        }


    }

    public static void initdb(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connString = "jdbc:mysql://localhost:3306/negocios"; // aqui ponemos el localhost o la ip de nuestro servidor en red o poner el archivo de configuracion o el lookback 127.0.0.1
            conn = DriverManager.getConnection(connString, "root", "18231129Mt");
            System.out.println("Conectado a la DB");
        }
        catch (ClassNotFoundException e){
            System.out.println("Driver Mysql no encontrados");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error conectando a la DB");
            e.printStackTrace();
        }
    }

}
