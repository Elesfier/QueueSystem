
package Services;

import Communication.LocalService;

public class Client extends LocalService {

    Client() {
        super("Client");
        addReplier("Client-Replier", "EXAMPLE.CLIENT");
        addRequestor("Client-Investment", "EXAMPLE.INVESTMENT");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        System.out.println( "Client Received form " + request + " : {");
        for ( String mess : messages ) {
            System.out.print( mess + "," );
        }
        System.out.println("}");
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
            client.sendMessage("Client-Investment", message );
        }
    }
}
