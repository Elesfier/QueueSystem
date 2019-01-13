
package QueueSystem;

import Communication.LocalService;

public class Client extends LocalService {

    Client( String brokerUrl ) {
        super("Client", "CLIENT", brokerUrl);
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "INVESTMENT":
            {
                System.out.println("Inwestycja zostala przeprowadzona dla uzytkownika " + messages[0]);
                break;
            }
            case "CREDIT":
            {
                System.out.println("Zostal udzielony kredyt dla uzytkownika " + messages[0]);
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        String brokerUrl = (args.length < 1 )?("tcp://localhost:61616"):(args[0]);
        Client client = new Client( brokerUrl );
        client.start();
        int counter = 0;

        while( true ) {

            try {
                Thread.sleep(2000);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            String[] message1 = { "user" + Integer.toString(++counter), "password" + Integer.toString(counter), "Investment", "Gold", "100zl" };
            System.out.println("Nowe zlecenie na inwestycje dla uzytkownika " + message1[0]);
            client.sendMessage("INVESTMENT", message1 );

            try {
                Thread.sleep(1000);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            String[] message2 = { "user" + Integer.toString(++counter), "password" + Integer.toString(counter), "Credit", "10000zl" };
            System.out.println("Nowe zlecenie na kredyt dla uzytkownika " + message2[0]);
            client.sendMessage("CREDIT", message2 );
        }
    }
}
