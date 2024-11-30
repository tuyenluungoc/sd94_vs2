package org.example.sd_94vs1.config.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {



    /**
     * Xử lý khi người dùng kết nối đến hệ thống chat.
     *
     * @param username tên người dùng
     * @return tin nhắn chào mừng
     */
    @MessageMapping("/chat.connect")
    @SendToUser("/queue/reply")
    public ChatMessage welcomeMessage(@Payload String username) {
        ChatMessage welcomeMessage = new ChatMessage();
        welcomeMessage.setSender("Bot");
        welcomeMessage.setContent("Chào " + username + ", tôi có thể giúp gì cho cậu?");
        welcomeMessage.setType(ChatMessage.MessageType.CHAT);
        return welcomeMessage;
    }
}
