package service;

import client.JogoDaVelhaClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JogoDaVelhaService extends Remote {
    void fazerMovimento(int linha, int coluna, char simbolo) throws RemoteException;
    void registrarCliente(JogoDaVelhaClient cliente) throws RemoteException;
    char verificaGanhador() throws RemoteException;
    void zerarTabuleiro() throws RemoteException;
    char retornaJogadorAtual() throws RemoteException;
    char verificaGanhadorHorizontal() throws RemoteException;
    char verificaGanhadorVertical() throws RemoteException;
    char verificaGanhadorDiagonal() throws RemoteException;
    boolean verificaEmpate() throws RemoteException;
    void pararServidor() throws RemoteException;
}
