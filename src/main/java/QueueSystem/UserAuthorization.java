
package QueueSystem;

import Communication.LocalService;

public class UserAuthorization extends LocalService {

    UserAuthorization( String brokerUrl ) {
        super("User-Authorization", "AUTHORIZATION", brokerUrl);
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "INVESTMENT":
            {
                System.out.println("Autoryzacja dla uzytkownika " + messages[0] + ". (Inwestycja)");
                sendMessage("DATABASE", messages );
                break;
            }
            case "DATABASE":
            {
                if ( messages.length > 4 ) {
                    System.out.println("Autoryzacja z bazy danych sie powiodla dla uzytkownika " + messages[0] + ". (Inwestycja)");
                    sendMessage("INVESTMENT", messages );
                } else {
                    System.out.println("Autoryzacja z bazy danych sie powiodla dla uzytkownika " + messages[0] + ". (Kredyt)");
                    sendMessage("CREDIT", messages );
                }
                break;
            }
            case "CREDIT":
            {
                System.out.println("Autoryzacja dla uzytkownika " + messages[0] + ". (Kredyt)");
                sendMessage("DATABASE", messages );
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        String brokerUrl = (args.length < 1 )?("tcp://localhost:61616"):(args[0]);
        UserAuthorization userAuthorization = new UserAuthorization( brokerUrl );
        userAuthorization.start();
    }
}
