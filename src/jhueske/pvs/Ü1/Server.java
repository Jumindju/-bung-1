package jhueske.pvs.Ü1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    private static final int port = 4711;
    private static final String charsetName = "Cp1252";

    public static void main(String args[]) {
        Socket talkSocket;
        BufferedReader fromClient;
        OutputStreamWriter toClient;
        String stringToConvert;

        try {
            ServerSocket listenSocket = new ServerSocket(port);

            while (true) {
                talkSocket = listenSocket.accept();

                // incoming message
                fromClient = new BufferedReader(new InputStreamReader(talkSocket.getInputStream(), charsetName));

                System.out.println("Connection request from address " + talkSocket.getInetAddress() + " port " + talkSocket.getPort());

                // outgoing message
                toClient = new OutputStreamWriter(talkSocket.getOutputStream(), charsetName);

                stringToConvert = fromClient.readLine();

                // Aufgabe b)
                boolean isShutDown = stringToConvert.equals("ENDE");
                if (isShutDown) {
                    toClient.write("Server shutdown\n");
                } else {
                    System.out.println("Message: " + stringToConvert);

                    String message = "";
                    boolean toLower = false;
                    Vector<MessageParameter> requestParameters = MessageHelper.deserialize(stringToConvert);
                    for (MessageParameter messageParameter : requestParameters) {
                        switch (messageParameter.getParameter()) {
                            case "m":
                                message = messageParameter.getValue();
                                break;
                            case "l":
                                toLower = Boolean.parseBoolean(messageParameter.getValue());
                                break;
                            default:
                                System.out.println("Parameter nicht unterstuetzt: " + messageParameter.getParameter());
                                break;
                        }
                    }

                    Vector<MessageParameter> responseParameters = new Vector<>();

                    responseParameters.add(new MessageParameter("p", message.length() * 1.5));
                    responseParameters.add(new MessageParameter("m", toLower ? message.toLowerCase() : message.toUpperCase()));

                    toClient.write(MessageHelper.serialize(responseParameters) + '\n');
                }

                toClient.close();
                fromClient.close();
                talkSocket.close();

                if (isShutDown) {
                    listenSocket.close();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("Server beendet!");
    }
}
