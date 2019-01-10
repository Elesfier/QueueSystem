
package QueueSystem;

import Communication.LocalService;

public class UserAuthorization extends LocalService {

    UserAuthorization() {
        super("User-Authorization", "AUTHORIZATION");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "INVESTMENT":
            {
                System.out.println("INVESTMENT");
                sendMessage("DATABASE", "DDDDD" );
                //TODO
                break;
            }
            case "DATABASE":
            {
                System.out.println("DATABASE");
                sendMessage("INVESTMENT", "DDDDD" );
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
