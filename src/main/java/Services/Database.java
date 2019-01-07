
package Services;

import Communication.LocalService;

public class Database extends LocalService {

    Database() {
        super("Database");
        addReplier("Database-Replier", "EXAMPLE.DATABASE");
        addRequestor("Database-Investment", "EXAMPLE.INVESTMENT");
        addRequestor("Database-Authorization", "EXAMPLE.AUTHORIZATION");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "Investment-Database":
            {
                System.out.println("Investment->Database");
                sendMessage("Database-Investment", "BBBBB" );
                //TODO
                break;
            }
            case "Authorization-Database":
            {
                System.out.println("Authorization->Database");
                sendMessage("Database-Authorization", "AAAA" );
                //TODO
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        Database database = new Database();
        database.start();
    }
}
