package ahnx33.scuola;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) throws IOException {

    final int port = 8080;
    try (ServerSocket server = new ServerSocket(port)) {

      System.out.println("\nListening...\n");

      while (true) {

        Socket socket = server.accept();
        System.out.println("ciao");

        ClientHandler client = new ClientHandler(socket);

        String request = client.readMessage();
        System.out.println(request);
        String[] parts = request.split(" ", 3);

        if (!parts[0].equals("GET")) {
          client.sendMessage("HTTP/1.1 405 Method Not Allowed");
          client.sendMessage("Content-Length: 0\n");
          client.close();
          continue;
        }

        String path = parts[1];
        System.out.println(path);
        File file;

        if (path.equals("/")) {
          file = new File("resources/htdocs/pages/index.html");
        } else if (path.endsWith(".html")) {
          file = new File("resources/htdocs/pages" + path);
        } else {
          file = new File("resources/htdocs" + path);
        }

        System.out.println(file.getAbsolutePath());

        if (file.exists()) {
          System.out.println("si");
          client.sendMessage("HTTP/1.1 200 OK");
          client.sendMessage("Content-Length: " + file.length() + "");
          client.sendMessage("Content-Type: " + getContentType(file) + "");
          client.sendMessage("");
          client.sendFile(file);
        } else {
          System.out.println("no");
          String prova = "no";
          client.sendMessage("HTTP/1.1 404 Not Found");
          client.sendMessage("Content-Length: " + prova.length() + "\n");
          client.sendMessage(prova);
        }
        client.close();
      }

    }

  }

  private static String getContentType(File f) {
    String type = f.getName().split("\\.", 2)[1];
    return switch (type) {
      case "html" -> "text/html";
      case "txt" -> "text/plain";
      case "css" -> "text/css";
      case "js" -> "application/javascript";
      case "json" -> "application/json";
      case "xml" -> "application/xml";
      case "jpg", "jpeg" -> "image/jpeg";
      case "png" -> "image/png";
      case "gif" -> "image/gif";
      case "svg" -> "image/svg+xml";
      case "pdf" -> "application/pdf";
      case "mp4" -> "video/mp4";
      case "mp3" -> "audio/mpeg";
      case "zip" -> "application/zip";
      case "csv" -> "text/csv";
      default -> "application/octet-stream";
    };
  }
}
