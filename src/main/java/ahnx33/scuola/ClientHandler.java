package ahnx33.scuola;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final DataOutputStream outBinary;

    ClientHandler(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        outBinary = new DataOutputStream(socket.getOutputStream());
    }

    void sendMessage(String response) {
        out.println(response);
    }

    void sendFile(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        byte[] buf = new byte[8192];
        int n;
        while ((n = input.read(buf)) != -1) {
            outBinary.write(buf, 0, n);
        }
        input.close();
    }

    String readMessage() throws IOException {
        return in.readLine();
    }

    void close() {
        try {
            in.close();
            out.close();
            outBinary.close();
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
