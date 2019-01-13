
package QueueSystem;

import Communication.LocalService;

public class Database extends LocalService {

    Database( String brokerUrl ) {
        super("Database", "DATABASE", brokerUrl);
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "INVESTMENT":
            {
                System.out.println("Baza danych (Inwestycja): <" + String.join("><", messages) + ">");
                sendMessage("INVESTMENT", messages );
                break;
            }
            case "CREDIT":
            {
                System.out.println("Baza danych (Kredyt): <" + String.join("><", messages) + ">");
                sendMessage("CREDIT", messages );
                break;
            }
            case "AUTHORIZATION":
            {
                System.out.println("Baza danych (Autoryzacja): <" + messages[0] + "><" + messages[1] + ">" );
                sendMessage("AUTHORIZATION", messages );
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        String brokerUrl = (args.length < 1 )?("tcp://localhost:61616"):(args[0]);
        Database database = new Database( brokerUrl );
        database.start();
    }
}
