/**
 * @author Ngoc Tram
 * @project streaming-service
 * @created 08/08/2024 - 11:45
 */
package com.vou.streaming_service.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    private final SocketIOServer socketIOServer;

    public void sendMessage( String room, String message, String senderUsername, String targetUsername,String TOPIC) {
        log.info("Send Message: [{}]" ,message);
        if (TOPIC == null || TOPIC.isEmpty()){
            TOPIC = "read_message";
        }
        if (targetUsername != null && !targetUsername.isEmpty()) {
            // Send to a specific user
            String finalTOPIC = TOPIC;
            socketIOServer.getRoomOperations(room).getClients().stream()
                    .filter(client -> targetUsername.equals(client.get("username")))
                    .findFirst()
                    .ifPresent(client -> {
                        client.sendEvent(finalTOPIC, message);
                        log.debug("Sent message to user {} in room {}: {}", targetUsername, room, message);
                    });
        } else {
            // Send to all users in the room except the sender
            String finalTOPIC1 = TOPIC;
            socketIOServer.getRoomOperations(room).getClients().stream()
                    .filter(client -> !senderUsername.equals(client.get("username")))
                    .forEach(client -> {
                        client.sendEvent(finalTOPIC1, message);
                        log.debug("Sent message to user {} in room {}: {}", client.get("username"), room, message);
                    });
        }
    }


}