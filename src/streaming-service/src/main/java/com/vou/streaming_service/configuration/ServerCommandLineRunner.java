package com.vou.streaming_service.configuration;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer server;

    @Override
    public void run(String... args) {
        int port = server.getConfiguration().getPort();

        try {
            server.start();
            log.info("SocketIOServer started successfully on {}:{}", server.getConfiguration().getHostname(), port);
        } catch (Exception e) {
            log.error("Failed to start SocketIOServer on {}:{}", server.getConfiguration().getHostname(), port, e);
            throw e;
        }
    }
}
