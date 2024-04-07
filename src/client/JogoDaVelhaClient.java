package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoDaVelhaClient extends Remote {
    void atualizaTabuleiro(char[][] tabuleiro) throws RemoteException;
    void notificaVencedor(char vencedor) throws RemoteException;
    void notificaEmpate() throws RemoteException;
}
