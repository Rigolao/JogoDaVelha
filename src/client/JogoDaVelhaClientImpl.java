package client;

import service.JogoDaVelhaService;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class JogoDaVelhaClientImpl extends JFrame implements JogoDaVelhaClient {
    private JogoDaVelhaService jogoDaVelhaService;

    private JButton[][] botoes;

    public JogoDaVelhaClientImpl() {
        super("Jogo da Velha");

        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));

        try {
            jogoDaVelhaService = (JogoDaVelhaService) Naming.lookup("rmi://localhost:1099/JogoDaVelhaService");
            UnicastRemoteObject.exportObject(this, 0);
            jogoDaVelhaService.registrarCliente(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        criarTabuleiro();

        setVisible(true);
    }

    private void criarTabuleiro() {
        botoes = new JButton[3][3];
        for (int coluna = 0; coluna < 3; coluna++) {
            for (int linha = 0; linha < 3; linha++) {
                final int l = linha;
                final int c = coluna;
                botoes[linha][coluna] = new JButton();
                botoes[linha][coluna].setFont(new Font("Arial", Font.PLAIN, 40));
                botoes[linha][coluna].addActionListener(e -> {
                    try {
                        if (botoes[l][c].getText().isEmpty()) {
                            char symbol = jogoDaVelhaService.retornaJogadorAtual();
                            botoes[l][c].setText(String.valueOf(symbol));
                            jogoDaVelhaService.fazerMovimento(l, c, symbol);
                        }
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                });
                add(botoes[linha][coluna]);
            }
        }
    }

    @Override
    public void atualizaTabuleiro(char[][] tabuleiro) {
        SwingUtilities.invokeLater(() -> {
            for (int linha = 0; linha < 3; linha++) {
                for (int coluna = 0; coluna < 3; coluna++) {
                    final char symbol = tabuleiro[linha][coluna];
                    int finalLinha = linha;
                    int finalColuna = coluna;
                    if (symbol != 0) {
                        SwingUtilities.invokeLater(() -> {
                            botoes[finalLinha][finalColuna].setText(String.valueOf(symbol));
                        });
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            botoes[finalLinha][finalColuna].setText("");
                        });
                    }
                }
            }
        });
    }

    @Override
    public void notificaVencedor(char vencedor) {
        SwingUtilities.invokeLater(() -> {
            String mensagem;
            if (vencedor != 'E') {
                mensagem = "O jogador " + vencedor + " ganhou!";
            } else {
                mensagem = "O jogo terminou em empate!";
            }
            int opcao = JOptionPane.showOptionDialog(null, mensagem, "Fim de Jogo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Encerrar", "Recomeçar"}, "Encerrar");
            if (opcao == JOptionPane.YES_OPTION) {
                try {
                    jogoDaVelhaService.pararServidor();
                    System.exit(0);
                } catch (RemoteException e) {
                    System.exit(0);
                }
            } else {
                try {
                    jogoDaVelhaService.zerarTabuleiro();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void notificaEmpate() {
        notificaVencedor('E');
    }

    public static void main(String[] args) {
        new JogoDaVelhaClientImpl();
    }
}
