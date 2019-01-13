
package QueueSystem;

import Communication.LocalService;

public class CreditService extends LocalService {

    CreditService(String brokerUrl ) {
        super("Credit-Service", "CREDIT", brokerUrl);
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {

        switch (request) {
            case "CLIENT":
            {
                System.out.println("Nowe zlecenie na kredyt stworzone przez uzytkownika " + messages[0] + ".");
                sendMessage("AUTHORIZATION", messages );
                break;
            }
            case "AUTHORIZATION":
            {
                System.out.println("Autoryzacja uzytkownika " + messages[0] + " sie powiodla.");
                sendMessage("DATABASE", messages );
                break;
            }
            case "DATABASE":
            {
                System.out.println("Dane o kredycie zostaly wprowadzone do bazy danych dla uzytkownika " + messages[0] + ".");
                sendMessage("CLIENT", messages );
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        String brokerUrl = (args.length < 1 )?("tcp://localhost:61616"):(args[0]);
        CreditService investmentService = new CreditService( brokerUrl );
        investmentService.start();
    }
}
