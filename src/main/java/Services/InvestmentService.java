
package Services;

import Communication.LocalService;

public class InvestmentService extends LocalService {

    InvestmentService() {
        super("Investment-Service");
        addReplier("Investment-Replier", "EXAMPLE.INVESTMENT");
        addRequestor("Investment-Client", "EXAMPLE.CLIENT");
        addRequestor("Investment-Database", "EXAMPLE.DATABASE");
        addRequestor("Investment-Authorization", "EXAMPLE.AUTHORIZATION");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {

        switch (request) {
            case "Client-Investment":
            {
                System.out.println("Client->Investment");
                sendMessage("Investment-Authorization", "CCCCC" );
                //TODO
                break;
            }
            case "Authorization-Investment":
            {
                System.out.println("Authorization->Investment");
                sendMessage("Investment-Database", "CCCCC" );
                //TODO
                break;
            }
            case "Database-Investment":
            {
                System.out.println("Database->Investment");
                sendMessage("Investment-Client", "udalo sie" );
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
