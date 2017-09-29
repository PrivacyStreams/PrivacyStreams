package io.github.privacystreams.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Socket server
 */

public class PSDebugSocketServer {
    private static PSDebugSocketServer socketServer;

    public static PSDebugSocketServer v() {
        if (socketServer == null) {
            socketServer = new PSDebugSocketServer();
        }
        socketServer.checkConnection();
        return socketServer;
    }

    private void checkConnection() {
        if (serverSocket == null || serverSocket.isClosed()) {
            try {
                serverSocket = new ServerSocket(Globals.DebugConfig.socketPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (acceptingThread == null) {
            acceptingThread = new AcceptingThread();
        }

        if (!acceptingThread.accepting) {
            acceptingThread.start();
        }

    }

    public void disconnect() {
        acceptingThread.accepting = false;
        for (Socket socket : sockets) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sockets.clear();
    }

    public void send(String... messages) {
        new SocketSendThread(messages).start();
    }

    private ServerSocket serverSocket;
    private AcceptingThread acceptingThread;
    private CopyOnWriteArraySet<Socket> sockets;

    private PSDebugSocketServer() {
        serverSocket = null;
        sockets = new CopyOnWriteArraySet<>();
        acceptingThread = new AcceptingThread();
    }

    private class AcceptingThread extends Thread {
        boolean accepting = false;

        @Override
        public void run() {
            try {
                accepting = true;
                Logging.debug("Start accepting socket clients...");
                while (accepting) {
                    Socket socket = serverSocket.accept();
                    Logging.debug("Accepted a socket client.");
                    sockets.add(socket);
                }
                Logging.debug("Stop accepting socket clients...");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                disconnect();
            }
        }
    }

    private class SocketSendThread extends Thread {
        String[] messages;

        SocketSendThread(String... messages) {
            this.messages = messages;
        }

        public void run() {
            for (String message : this.messages) {
                byte[] bytes = message.getBytes();
                byte[] header = ByteBuffer.allocate(6).put((byte) 0xFF).put((byte) 0x00).putInt(bytes.length).array();

                Set<Socket> socketsToRemove = new HashSet<>();

                synchronized (sockets) {
                    for (Socket socket : sockets) {
                        try {
                            socket.getOutputStream().write(header);
                            socket.getOutputStream().flush();
                            socket.getOutputStream().write(bytes);
                            socket.getOutputStream().flush();
                        } catch (IOException e) {
                            socketsToRemove.add(socket);
                            e.printStackTrace();
                        }
                    }

                    for (Socket socket : socketsToRemove) {
                        try {
                            if (!socket.isClosed())
                                socket.close();
                            sockets.remove(socket);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
