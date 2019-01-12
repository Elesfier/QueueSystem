
package Communication;

import java.util.Arrays;

public abstract class LocalService implements Runnable {

    private boolean isRunning;
    private String threadName;
    private Thread thread;

    private final String queueBase = "LOCALSERVICE.";
    private final String defaultBrokerUrl = "tcp://localhost:61616";

    //Queue Replier (in)
    Replier replier;

    //Queue Requestor (out)
    Requestor requestor;

    public LocalService( String threadName, String queuePath, String brokerUrl ) {
        this.threadName = threadName;
        this.isRunning = false;
        try {
            this.replier = new Replier(threadName + "-replier", queueBase + queuePath, brokerUrl);
            this.requestor = new Requestor(threadName + "-requestor", queuePath,  brokerUrl);
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
        //System.out.println("Creating Service: " + threadName);
    }

    public LocalService( String threadName, String queuePath ) {
        this.threadName = threadName;
        this.isRunning = false;
        try {
            this.replier = new Replier(threadName + "-replier", queueBase + queuePath, defaultBrokerUrl);
            this.requestor = new Requestor(threadName + "-requestor", queuePath,  defaultBrokerUrl);
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
        //System.out.println("Creating Service: " + threadName);
    }

    public void start() {
        System.out.println("Starting Service: " + threadName );
        if (thread == null) {
            thread = new Thread (this, threadName);
            isRunning = true;
            try {
                replier.start();
                requestor.start();
            } catch (Exception e) {
                System.out.println("Caught: " + e);
                e.printStackTrace();
            }
            thread.start();
        }
    }

    public void stop() {
        isRunning = false;
    }

    public String getName() {
        return threadName;
    }

    public void sendMessage( String request, String message ) {
        if ( isRunning ) {
            this.requestor.sendMessage( queueBase + request, message );
        }
    }

    public void sendMessage( String request, String[] messages ) {
        if ( isRunning ) {
            this.requestor.sendMessage( queueBase + request, messages );
        }
    }

    public void run() {
        try {
            while(isRunning) {
                String mess = replier.receiveMessage();
                if (mess != null && mess != "") {
                    String[] messages = mess.substring( 1, mess.length()-1).split("><");
                    onReceivedMessage( messages[0], Arrays.copyOfRange(messages, 1, messages.length) );
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public abstract void onReceivedMessage( String request, String[] messages );
}
