
package Services;

import Communication.LocalService;

import java.util.Arrays;

public class Client extends LocalService {

    Client() {
        super("Client");
        addReplier("Client-Replier", "EXAMPLE.CLIENT");
        addRequestor("Client-Investment", "EXAMPLE.INVESTMENT");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "Investment-Client":
            {
                System.out.println("Investment-Client");
                //TODO
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        Client client = new Client();
        client.start();

        while( true ) {

            try {
                Thread.sleep(2000);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            String[] message = { "user", "password", "investement", "BLABLA", "100" };
            System.out.println("Client->Investment");
            client.sendMessage("Client-Investment", message );
        }
    }
}
