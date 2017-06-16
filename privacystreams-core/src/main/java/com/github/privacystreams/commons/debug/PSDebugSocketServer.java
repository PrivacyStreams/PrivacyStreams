package com.github.privacystreams.commons.debug;

import android.os.AsyncTask;

import com.github.privacystreams.utils.Globals;
import com.github.privacystreams.utils.Logging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Socket server
 */

class PSDebugSocketServer {
    private static PSDebugSocketServer socketServer;

    static PSDebugSocketServer v() {
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

    public void send(String message) {
        new SocketSendThread().execute(message);
    }

    private ServerSocket serverSocket;
    private AcceptingThread acceptingThread;
    private HashSet<Socket> sockets;

    private PSDebugSocketServer() {
        serverSocket = null;
        sockets = new HashSet<>();
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

    private class SocketSendThread extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            for (String message : params) {
                byte[] bytes = message.getBytes();
                byte[] lenOfBytes = ByteBuffer.allocate(4).putInt(bytes.length).array();

                Set<Socket> socketsToRemove = new HashSet<>();

                for (Socket socket : sockets) {
                    try {
                        socket.getOutputStream().write(lenOfBytes);
                        socket.getOutputStream().write(message.getBytes());
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
            return null;
        }
    }
}
