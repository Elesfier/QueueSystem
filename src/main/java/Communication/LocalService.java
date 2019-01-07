
package Communication;

import java.util.HashMap;
import java.util.Arrays;

public abstract class LocalService implements Runnable {

    private boolean isRunning;
    private String threadName;
    private Thread thread;

    private HashMap<String, Replier> repliers;
    private HashMap<String, Requestor> requestors;

    public LocalService( String threadName ) {
        this.threadName = threadName;
        this.isRunning = false;
        this.repliers = new HashMap<String, Replier>();
        this.requestors = new HashMap<String, Requestor>();
        System.out.println("Creating Service: " + threadName);
    }

    protected void addReplier( String replierName, String queuePath ) {
        try {
            Replier newReplier = new Replier(replierName, queuePath);
            newReplier.start();
            this.repliers.put( replierName, newReplier );
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    protected void addRequestor( String requestorName, String queuePath ) {
        try {
            Requestor newRequestor = new Requestor(requestorName, queuePath);
            newRequestor.start();
            this.requestors.put( requestorName, newRequestor );
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Starting Service: " + threadName );
        if (thread == null) {
            thread = new Thread (this, threadName);
            isRunning = true;
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
        if ( isRunning && this.requestors.containsKey(request) ) {
            this.requestors.get(request).sendMessage( message );
        }
    }

    public void sendMessage( String request, String[] messages ) {
        if ( isRunning && this.requestors.containsKey(request) ) {
            this.requestors.get(request).sendMessage( messages );
        }
    }

    public void run() {
        try {
            while(isRunning) {
                for (Replier replier : repliers.values()) {
                    String mess = replier.receiveMessage();
                    if (mess != null && mess != "") {
                        String[] messages = mess.substring( 1, mess.length()-1).split("><");
                        onReceivedMessage( messages[0], Arrays.copyOfRange(messages, 1, messages.length) );
                    }
                }
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public abstract void onReceivedMessage( String request, String[] messages );
}
