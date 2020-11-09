package jhueske.pvs.Ü1;

import java.util.Vector;

public class MessageHelper {
    public static String Serialize(Vector<MessageParameter> messageParts){
        String message = "";
        for (MessageParameter messagePart : messageParts) {
            message += messagePart.getMessagePart() + ";";
        }

        return message.substring(0,message.length()-1) + "\n";
    }

    public static Vector<MessageParameter> Deserialize(String message){
        if (message==null)
            throw new IllegalArgumentException();

        Vector<MessageParameter> parameters = new Vector<>();
        String parts[] = message.split(";");
        for(String part : parts){
            String messageParts[] = part.split("=");
            if(messageParts.length != 2)
                throw new IllegalArgumentException();

            parameters.add(new MessageParameter(messageParts[0],messageParts[1]));
        }

        return parameters;
    }
}

