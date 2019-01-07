
package Services;

import Communication.LocalService;

public class Database extends LocalService {

    Database() {
        super("Database");
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
        Database database = new Database();
        database.start();
    }
}
