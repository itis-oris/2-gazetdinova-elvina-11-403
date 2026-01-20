package musicbattle.server.network;

import musicbattle.common.protocol.Message;
import musicbattle.common.protocol.MessageType;
import musicbattle.server.ServerMain;
import musicbattle.server.game.ScoreBoard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler extends Thread {

    private static final List<String> MELODY = List.of("C", "D", "E");

    private final Socket socket;
    private final ScoreBoard scoreBoard;

    private PrintWriter out;
    private String playerName;
    private final List<String> inputNotes = new ArrayList<>();

    public ClientHandler(Socket socket, ScoreBoard scoreBoard) {
        this.socket = socket;
        this.scoreBoard = scoreBoard;
    }

    public PrintWriter getOut() {
        return out;
    }

    @Override
    // 3 в отдельном потоке для этого клиента
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            out = new PrintWriter(socket.getOutputStream(), true);

            String rawMessage;
            while ((rawMessage = in.readLine()) != null) {
            // блокирует, ждет строку от клиента
                Message message = Message.deserialize(rawMessage);
                if (message == null) {
                    System.err.println("Ошибка парсинга: " + rawMessage);
                    continue;
                }

                MessageType messageType = message.getType();
                String payload = message.getPayload();

                System.out.println("[ClientHandler] Получено: " + messageType + " | " + payload);

                if (messageType == MessageType.CONNECT) {
                    playerName = payload;
                    scoreBoard.addPlayer(playerName);
                    // добавляет клиента в мапу
                    int connected = ServerMain.getClients().size(); // =1
                    if (connected < ServerMain.MAX_PLAYERS) { // 1 < 2 true
                        out.println(Message.serialize(MessageType.WAIT, ""));
                    }

                    ServerMain.tryStartGame(); // проверка старта
                }

                // 8 проверка мелодии (result)
                else if (messageType == MessageType.NOTE_INPUT) {
                    String note = payload;
                    inputNotes.add(note);

                    if (inputNotes.size() == MELODY.size()) {
                        checkMelody();
                        inputNotes.clear();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkMelody() {
        if (inputNotes.equals(MELODY)) {
            // ArrayList.equals
            scoreBoard.addPoint(playerName);
            out.println(Message.serialize(MessageType.RESULT, "SUCCESS"));
        } else {
            out.println(Message.serialize(MessageType.RESULT, "FAIL"));
        }

        ServerMain.broadcastScore();

        if (scoreBoard.hasWinner()) {
            String winner = scoreBoard.getWinner();
            String rating = scoreBoard.toProtocolString();
            ServerMain.broadcast(Message.serialize(MessageType.GAME_OVER, winner + ":" + rating));
        }
    }
}
