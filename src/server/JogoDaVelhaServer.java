package server;

import service.JogoDaVelhaService;
import service.JogoDaVelhaServiceImpl;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

public class JogoDaVelhaServer {
    public static void main(String[] args) {
        try {
            JogoDaVelhaService jogoDaVelhaService = new JogoDaVelhaServiceImpl();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("JogoDaVelhaService", jogoDaVelhaService);
            System.out.println("Servidor iniciado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
