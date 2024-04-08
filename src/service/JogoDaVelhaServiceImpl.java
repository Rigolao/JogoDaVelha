package service;

import client.JogoDaVelhaClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class JogoDaVelhaServiceImpl extends UnicastRemoteObject implements JogoDaVelhaService {
    private char[][] tabuleiro;
    private List<JogoDaVelhaClient> clientes;

    public JogoDaVelhaServiceImpl() throws RemoteException {
        super();
        tabuleiro = new char[3][3];
        clientes = new ArrayList<>();
    }

    @Override
    public void fazerMovimento(int linha, int coluna, char simbolo) throws RemoteException {
        tabuleiro[linha][coluna] = simbolo;
        notificarClientes();
    }

    @Override
    public void registrarCliente(JogoDaVelhaClient cliente) throws RemoteException {
        clientes.add(cliente);
    }

    @Override
    public void zerarTabuleiro() throws RemoteException {
        tabuleiro = new char[3][3];
        notificarClientes();
    }

    @Override
    public char retornaJogadorAtual() throws RemoteException {
        int totalJogadas = 0;
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                if (tabuleiro[linha][coluna] != 0) {
                    totalJogadas++;
                }
            }
        }
        return totalJogadas % 2 == 0 ? 'X' : 'O';
    }

    @Override
    public char verificaGanhador() throws RemoteException {
        char horizontal = verificaGanhadorHorizontal();
        if (horizontal != 0) {
            return horizontal;
        }

        char vertical = verificaGanhadorVertical();
        if (vertical != 0) {
            return vertical;
        }

        char diagonal = verificaGanhadorDiagonal();
        if (diagonal != 0) {
            return diagonal;
        }

        if (verificaEmpate()) {
            return 'E';
        }

        return 0;
    }

    @Override
    public char verificaGanhadorHorizontal() throws RemoteException {
        for(int coluna = 0; coluna < 3; coluna++) {
            if(tabuleiro[0][coluna] != 0 && tabuleiro[0][coluna] == tabuleiro[1][coluna]
                    && tabuleiro[1][coluna] == tabuleiro[2][coluna]) {
                return tabuleiro[0][coluna];
            }
        }

        return 0;
    }

    @Override
    public char verificaGanhadorVertical() throws RemoteException {
        for(int linha = 0; linha < 3; linha++) {
            if(tabuleiro[linha][0] != 0 && tabuleiro[linha][0] == tabuleiro[linha][1]
                    && tabuleiro[linha][1] == tabuleiro[linha][2]) {
                return tabuleiro[linha][0];
            }
        }

        return 0;
    }

    @Override
    public char verificaGanhadorDiagonal() throws RemoteException {
        if(tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
            return tabuleiro[0][0];
        }
        if(tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
            return tabuleiro[0][2];
        }
        return 0;
    }

    @Override
    public boolean verificaEmpate() throws RemoteException {
        for (int linha = 0; linha < 3; linha++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                if (tabuleiro[linha][coluna] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void pararServidor() throws RemoteException {
        System.exit(0);
    }

    private void notificarClientes() throws RemoteException {
        char resultado = verificaGanhador();
        for (JogoDaVelhaClient cliente : clientes) {
            try {
                cliente.atualizaTabuleiro(tabuleiro);
                if (resultado != 0) {
                    if (resultado == 'E') {
                        cliente.notificaEmpate();
                    } else {
                        cliente.notificaVencedor(resultado);
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
