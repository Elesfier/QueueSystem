
package Communication;

import java.util.HashMap;

public abstract class LocalService {

    private HashMap<String, Replier> repliers;
    private HashMap<String, Requestor> requestors;

    public LocalService() {
        this.repliers = new HashMap<String, Replier>();
        this.requestors = new HashMap<String, Requestor>();
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

    //TODO close service

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

    public void sendMessage( String request, String message ) {
        if ( this.requestors.containsKey(request) ) {
            this.requestors.get(request).sendMessage( message );
        }
    }

    public void sendMessage( String request, String[] messages ) {
        if ( this.requestors.containsKey(request) ) {
            this.requestors.get(request).sendMessage( messages );
        }
    }

    //TODO obsluga received messages

    public abstract void onReceivedMessage( String request, String[] messages );
}
