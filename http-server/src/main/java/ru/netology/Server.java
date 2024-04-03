package ru.netology;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class Server implements Runnable {
    private static Socket socket;


    public Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        final var validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js", "/default-get.html");
        while (!socket.isClosed()) {
            try (
                    var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    var out = new BufferedOutputStream(socket.getOutputStream())
            ) {
                final var requestLine = in.readLine();
                final var parts = requestLine.split(" ");
                if (parts.length != 3) {
                    continue;
                }
                final var path = parts[1];
                if (!validPaths.contains(path)) {
                    sendNotFoundResponse(out);
                    continue;
                }
                final var filePath = Path.of("C:\\Users\\Виктор\\Desktop\\Http_server-main\\http-server\\public", path);
                final var mimeType = Files.probeContentType(filePath);
                if (path.equals("/classic.html")) {
                    sendClassicHtmlResponse(out, filePath, mimeType);
                    continue;
                }
                sendFileResponse(out, filePath, mimeType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void sendNotFoundResponse(BufferedOutputStream out) throws IOException {
        out.write((
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Length: 0\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.flush();
    }

    private void sendClassicHtmlResponse(BufferedOutputStream out, Path filePath, String mimeType) throws IOException {
        var template = Files.readString(filePath);
        var content = template.replace(
                "{time}",
                LocalDateTime.now().toString()
        ).getBytes();
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        out.write(content);
        out.flush();
    }

    private void sendFileResponse(BufferedOutputStream out, Path filePath, String mimeType) throws IOException {
        var length = Files.size(filePath);
        out.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + mimeType + "\r\n" +
                        "Content-Length: " + length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        Files.copy(filePath, out);
        out.flush();
    }

//    private String getQueryParam(String name) throws IOException {
//
//
//    }
    private static int indexOf(byte[] array, byte[] target, int start, int max) {
        outer:
        for (int i = start; i < max - target.length + 1; i++) {
            for (int j = 0; j < target.length; j++) {
                if (array[i + j] != target[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

}
