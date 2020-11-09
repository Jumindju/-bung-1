package jhueske.pvs.Ü1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

public class Client {
    private static final int port = 4711;
    private static final String charsetName = "UTF-8";

    public static void main(String args[]){
        String msgToSend = "€";
        boolean toLowerCase = true;

        try{
            Socket talkSocket = new Socket("localhost", port);

            BufferedReader fromServer = new BufferedReader(new InputStreamReader(talkSocket.getInputStream(), charsetName));

            OutputStreamWriter toServer = new OutputStreamWriter(talkSocket.getOutputStream(), charsetName);

            Vector<MessageParameter> sendParameter = new Vector<>();
            sendParameter.add(new MessageParameter("m",msgToSend));
            sendParameter.add(new MessageParameter("l", toLowerCase));
            String msg = MessageHelper.Serialize(sendParameter);

            System.out.println("Send: " + msg);
            toServer.write(msg);
            toServer.flush();

            String result = fromServer.readLine();
            if(result.equals("Server shutdown")){
                System.out.println("Server was stopped");
            } else{
                Vector<MessageParameter> messageParts = MessageHelper.Deserialize(result);
                for(MessageParameter messagePart : messageParts){
                    switch (messagePart.getParameter()){
                        case "m":
                            System.out.println("Received message: " + messagePart.getValue());
                            break;
                        case "p":
                            System.out.println("ConvertPrice: " + messagePart.getValue() + "Ct");
                            break;
                        default:
                            System.out.println("Invalid message part: " + messagePart);
                            break;
                    }
                }
            }

            toServer.close();
            fromServer.close();
            talkSocket.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
