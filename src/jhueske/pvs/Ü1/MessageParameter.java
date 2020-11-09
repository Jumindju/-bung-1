package jhueske.pvs.Ü1;

public class MessageParameter {
    private String parameter;
    private String value;

    public MessageParameter(String parameter, Object value) {
        this.parameter = parameter;
        this.value = value.toString();
    }

    public String getMessagePart() {
        return parameter + "=" + value;
    }
    public String getParameter(){return parameter;}
    public String getValue(){return value;}
}
