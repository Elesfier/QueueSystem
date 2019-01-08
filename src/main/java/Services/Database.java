
package Services;

import Communication.LocalService;

public class Database extends LocalService {

    Database() {
        super("Database", "DATABASE");
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        switch (request) {
            case "INVESTMENT":
            {
                System.out.println("INVESTMENT");
                sendMessage("INVESTMENT", "BBBBB" );
                //TODO
                break;
            }
            case "AUTHORIZATION":
            {
                System.out.println("AUTHORIZATION");
                sendMessage("AUTHORIZATION", "AAAA" );
                //TODO
                break;
            }
            default:
                System.out.println( getName() + ": strange message.");
        }
    }

    public static void main( String[] args ) {
        Database database = new Database();
        database.start();
    }
}
