package musicbattle.server;

import musicbattle.common.protocol.Message;
import musicbattle.common.protocol.MessageType;
import musicbattle.server.game.ScoreBoard;
import musicbattle.server.network.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {
    // 1
    public static final int PORT = 5050;
    public static final int MAX_PLAYERS = 2;

    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final ScoreBoard scoreBoard = new ScoreBoard();

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("🎵 MusicBattle server started on port " + PORT);
            System.out.println("Ожидание подключения " + MAX_PLAYERS + " игроков...");

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, scoreBoard);
                clients.add(handler);
                System.out.println("✅ Клиент подключился. Всего: " + clients.size() + "/" + MAX_PLAYERS);
                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ClientHandler> getClients() {
        return clients;
    }
    // 5
    public static synchronized void tryStartGame() {
        if (clients.size() == MAX_PLAYERS) {
            System.out.println("🎮 Достаточно игроков! Начинаем игру...");
            scoreBoard.reset();
            broadcast(Message.serialize(MessageType.START_GAME, ""));
        }
    }

    public static void broadcastScore() {
        String data = scoreBoard.toProtocolString();
        if (!data.isEmpty()) {
            broadcast(Message.serialize(MessageType.SCORE_UPDATE, data));
        }
    }

    public static void broadcast(String msg) {
        System.out.println("[Broadcast] " + msg);
        for (ClientHandler client : clients) {
            if (client.getOut() != null) {
                client.getOut().println(msg);
            }
        }
    }

    public static ScoreBoard getScoreBoard() {
        return scoreBoard;
    }
}