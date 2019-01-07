
package Services;

import Communication.LocalService;

public class CreditService extends LocalService {

    CreditService() {
        super("Credit-Service");
        //TODO
        //addReplier();
        //addRequestor();
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        System.out.println( "Received form " + request + " : ");
        for ( String mess : messages ) {
            System.out.print( mess + "," );
        }
        System.out.println( ";" );
        //TODO
    }

    public static void main( String[] args ) {
        CreditService creditService = new CreditService();
        creditService.start();
    }
}
