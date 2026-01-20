package musicbattle.common.protocol;

public class Message {
    private MessageType type;
    private String payload;

    public Message(MessageType type, String payload) {
        this.type = type;
        this.payload = payload != null ? payload : "";
    }

    public Message(MessageType type) {
        this(type, "");
    }

    public static String serialize(MessageType type, String payload) {
        return type.name() + ":" + (payload != null ? payload : "");
    }

    public static Message deserialize(String raw) {
        if (raw == null || raw.isEmpty()) return null;

        String[] parts = raw.split(":", 2);
        try {
            MessageType type = MessageType.valueOf(parts[0]);
            String payload = parts.length > 1 ? parts[1] : "";
            return new Message(type, payload);
        } catch (IllegalArgumentException e) {
            // Неизвестный тип сообщения
            return null;
        }
    }

    public MessageType getType() {
        return type;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return serialize(type, payload);
    }
}