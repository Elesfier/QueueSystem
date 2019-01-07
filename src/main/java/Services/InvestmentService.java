
package Services;

import Communication.LocalService;

public class InvestmentService extends LocalService {

    InvestmentService() {
        super("Investment-Service");
        addReplier("Investment-Replier", "EXAMPLE.CLIENT");
        addRequestor("Investment-Client", "EXAMPLE.CLIENT");
        addRequestor("Investment-Database", "EXAMPLE.DATABASE");
        addRequestor("Investment-Authorization", "EXAMPLE.AUTHORIZATION");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {

        switch (request) {
            case "Client-Investment":
            {
                //TODO
                break;
            }
            case "Authorization-Investment":
            {
                //TODO
                break;
            }
            case "Database-Investment":
            {
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
