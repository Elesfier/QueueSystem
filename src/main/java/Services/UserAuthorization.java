
package Services;

import Communication.LocalService;

public class UserAuthorization extends LocalService {

    UserAuthorization() {
        super("User-Authorization");
        addReplier("Authorization-Replier", "EXAMPLE.AUTHORIZATION");
        addRequestor("Authorization-Investment", "EXAMPLE.INVESTMENT");
        addRequestor("Authorization-Database", "EXAMPLE.DATABASE");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "Investment-Authorization":
            {
                System.out.println("Investment->Authorization");
                sendMessage("Authorization-Database", "DDDDD" );
                //TODO
                break;
            }
            case "Database-Authorization":
            {
                System.out.println("Database->Authorization");
                sendMessage("Authorization-Investment", "DDDDD" );
                //TODO
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        UserAuthorization userAuthorization = new UserAuthorization();
        userAuthorization.start();
    }
}
