package cp2024.test;

public class Message {
    private long startTime;
    private final String caller;

    public Message(String caller){
        this.startTime = System.currentTimeMillis();
        this.caller = caller;
        sendStartMessage();
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
    }

    private void sendStartMessage() {
        System.out.printf("[%s]: %s\n", caller, "started");
    }

    public void sendFormattedMessage(String str) {
        System.out.printf("[%s]: %s [%dms]\n", caller, str, System.currentTimeMillis() - startTime);
    }

    public void sendEndMessage() {
        System.out.printf("[%s]: Done [%dms]\n\n", caller, System.currentTimeMillis() - startTime);
    }
}
