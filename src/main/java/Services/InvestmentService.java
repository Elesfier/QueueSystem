
package Services;

import Communication.LocalService;

public class InvestmentService extends LocalService {

    InvestmentService() {
        super("Investment-Service", "INVESTMENT");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {

        switch (request) {
            case "CLIENT":
            {
                System.out.println("CLIENT");
                sendMessage("AUTHORIZATION", "CCCCC" );
                //TODO
                break;
            }
            case "AUTHORIZATION":
            {
                System.out.println("AUTHORIZATION");
                sendMessage("DATABASE", "CCCCC" );
                //TODO
                break;
            }
            case "DATABASE":
            {
                System.out.println("DATABASE");
                sendMessage("CLIENT", "udalo sie" );
                //TODO
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        InvestmentService investmentService = new InvestmentService();
        investmentService.start();
    }
}
