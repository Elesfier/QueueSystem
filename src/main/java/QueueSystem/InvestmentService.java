
package QueueSystem;

import Communication.LocalService;

public class InvestmentService extends LocalService {

    InvestmentService( String brokerUrl ) {
        super("Investment-Service", "INVESTMENT", brokerUrl);
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {

        switch (request) {
            case "CLIENT":
            {
                System.out.println("Nowa inwestycja powstala przez uzytkownika " + messages[0] + ".");
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
                System.out.println("Inwestycja wprowadzona w bazie danych banku dla uzytkownika " + messages[0] + ".");
                sendMessage("CLIENT", messages );
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        String brokerUrl = (args.length < 1 )?("tcp://localhost:61616"):(args[0]);
        InvestmentService investmentService = new InvestmentService( brokerUrl );
        investmentService.start();
    }
}
