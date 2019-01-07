
package Services;

import Communication.LocalService;

public class Logger extends LocalService {

    Logger() {
        super("Logger");
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
        //TODO zapisywanie do bazy danych
    }

    public static void main( String[] args ) {
        Logger logger = new Logger();
        logger.start();
    }
}
