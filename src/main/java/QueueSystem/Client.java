
package QueueSystem;

import Communication.LocalService;

public class Client extends LocalService {

    Client() {
        super("Client", "CLIENT");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "INVESTMENT":
            {
                System.out.println("INVESTMENT");
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
            client.sendMessage("INVESTMENT", message );
        }
    }
}
