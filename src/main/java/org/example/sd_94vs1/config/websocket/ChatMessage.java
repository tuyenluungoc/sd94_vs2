package org.example.sd_94vs1.config.websocket;



public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;

    // Enum để phân loại các loại tin nhắn
    public enum MessageType {
        CHAT, LEAVE, JOIN
    }

    // Getter và Setter cho các thuộc tính
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
