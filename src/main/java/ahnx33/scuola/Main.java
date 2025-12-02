package ahnx33.scuola;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) throws IOException {
    /*
     * method = ""
     * path = ""
     * if method != GET
     * risponde con 405
     * else
     * if path finisce con /
     * path = path + "index.html"
     * cerco la risorsa sul disco
     * se file esiste
     * rispondo con la risorsa (impostando il giusto Content-Type)
     * else
     * rispondo con un 404
     */

    // Codice di esempio

    final int port = 8080;
    try (ServerSocket server = new ServerSocket(port)) {

      System.out.println("\nListening...\n");

      while (true) {

        Socket socket = server.accept();

        ClientHandler client = new ClientHandler(socket);

        String request = client.readMessage();
        String[] parts = request.split(" ", 3);

        if (!parts[0].equals("GET")) {
          client.sendMessage("HTTP/1.1 405 Method Not Allowed");
          client.sendMessage("Content-Length: 0\n");
          client.close();
          continue;
        }

        String path = parts[1];
        File file;

        if (path.equals("/")) {
          file = new File("htdocs/index.html");
        } else {

          file = new File("htdocs" + path);
        }

        if (file.exists()) {
          client.sendMessage("HTTP/1.1 200 OK");
          client.sendMessage("Content-Length: " + file.length() + "");
          client.sendMessage("Content-Type: " + getContentType(file) + "");
          client.sendMessage("");
          client.sendFile(file);
        }
        client.close();
      }

    }

  }

  private static String getContentType(File f) {
    String type = f.getName().split("\\.", 2)[1];
    switch (type) {
      case "html":
        return "text/html";
      case "txt":
        return "text/plain";
      case "css":
        return "text/css";
      case "js":
        return "application/javascript";
      case "json":
        return "application/json";
      case "xml":
        return "application/xml";
      case "jpg":
      case "jpeg":
        return "image/jpeg";
      case "png":
        return "image/png";
      case "gif":
        return "image/gif";
      case "svg":
        return "image/svg+xml";
      case "pdf":
        return "application/pdf";
      case "mp4":
        return "video/mp4";
      case "mp3":
        return "audio/mpeg";
      case "zip":
        return "application/zip";
      case "csv":
        return "text/csv";
      default:
        return "application/octet-stream";
    }
  }
}