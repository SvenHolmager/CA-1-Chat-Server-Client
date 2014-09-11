package echoclient;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.ProtocolStrings;

public class EchoClient extends Thread {

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    List<EchoListener> listeners = new ArrayList();

    public static void main(String[] args) {
        int port = 9090;
        String ip = "localhost";
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            ip = args[1];

        }
        try {
            EchoClient client = new EchoClient();
            client.connect(ip, port);

            //        client.registerEchoListener(new EchoListener);
        } catch (UnknownHostException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
        start();
    }

    public void send(String msg) throws IOException {
        if (socket.isOutputShutdown()) {
            throw new IOException("Outbound socket is closed");
        }
        output.println(msg);
        output.flush();
    }

    public void stopClient() throws IOException {
        output.println(ProtocolStrings.CLOSE);
        socket.shutdownOutput();
    }

    public void registerEchoListener(EchoListener listener) {
        listeners.add(listener);
    }

    public void unRegisterEchoListener(EchoListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String msg, ArrayList<String> userNames) {
        for (EchoListener listener : listeners) {
            listener.messageArrived(msg);
            listener.updateUserList(userNames);
        }
    }

    public void run() {
        String msg = input.nextLine();
        while (!msg.equals(ProtocolStrings.CLOSE)) {
            msg = input.nextLine();
        }
        try {
            socket.close();
            input.close();
            input = null;
        } catch (IOException ex) {
            Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String receive() {
        String msg = input.nextLine();
        if (msg.equals(ProtocolStrings.CLOSE)) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(EchoClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (msg.equals(ProtocolStrings.ONLINE)) {
                }

                if (msg.equals(ProtocolStrings.MESSAGE)) {
                }
                {
                    return msg;
                }
            } catch (Exception e) {
            }
        }
    }
}
