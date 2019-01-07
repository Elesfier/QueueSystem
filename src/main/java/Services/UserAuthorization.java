
package Services;

import Communication.LocalService;

public class UserAuthorization extends LocalService {

    UserAuthorization() {
        super("User-Authorization");
        addReplier("Authorization-Replier", "EXAMPLE.AUTHORIZATION");
        addRequestor("Authorization-Investment", "EXAMPLE.INVESTMENT");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "Investment-Authorization":
            {
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
