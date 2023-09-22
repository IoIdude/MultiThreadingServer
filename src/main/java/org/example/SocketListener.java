package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

import static java.lang.System.out;

public class SocketListener implements Runnable {
    public static final Charset CS = Charset.defaultCharset();

    private Socket client;
    BufferedWriter dataOut;
    BufferedReader dataIn;

    public SocketListener(Socket client) throws IOException {
        this.client = client;
        this.dataIn = new BufferedReader(
                new InputStreamReader(
                        client.getInputStream(),
                        CS
                )
        );
        this.dataOut = new BufferedWriter(
                new OutputStreamWriter(
                        client.getOutputStream(),
                        CS
                )
        );
    }

    @Override
    public void run() {
        try {
            while (!client.isClosed()) {
                String entry = dataIn.readLine();

                if (entry.equals("q")) {
                    dataOut.write("Connection closed");
                    dataOut.flush();

                    Thread.sleep(5000);
                    break;
                }

                dataOut.write("Ur message - " + entry);
                dataOut.flush();
            }

            dataIn.close();
            dataOut.close();
            client.close();

            out.println("Closing connection and socket - done");
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
