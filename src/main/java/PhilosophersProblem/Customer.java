
package PhilosophersProblem;

import Communication.LocalService;
import java.util.Random;

public class Customer extends LocalService {

    private long sendingIndex;
    private long ownIndex;
    private long indexOfCustomer;

    Customer( long index ) {
        super("Customer","CUSTOMER" + Long.toString(index));
        this.ownIndex = index;
        this.indexOfCustomer = 0;
        this.sendingIndex = 0;
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        try {

            long index = Long.parseLong(request.substring(request.indexOf( "CUSTOMER" ), request.length()));

            if ( indexOfCustomer == ownIndex ) {
                System.out.println(getName() + ": is busy sending.");
            } else if ( indexOfCustomer == 0 ) {
                indexOfCustomer = index;
                System.out.println(getName() + ": is now provider && received.");
                receivedData( messages );
                if (messages[ messages.length-1 ].equals("END")) {
                    indexOfCustomer = 0;
                }
            } else if ( (indexOfCustomer == index) && (sendingIndex == 0) ) {
                System.out.println(getName() + ": received.");
                receivedData( messages );
                if (messages[ messages.length-1 ].equals("END")) {
                    indexOfCustomer = 0;
                }

            } else {
                System.out.println(getName() + ": is busy.");
            }

        } catch ( Exception e ) {
            System.out.println( getName() + ": strange message.");
        }
    }

    public void receivedData( String[] messages ) {
        System.out.println(getName() + String.join("", messages));
    }


    public boolean canSendMessages() {
        if ( indexOfCustomer == 0 ) {
            indexOfCustomer = ownIndex;
        }
    }

    public void stopSending() {
        indexOfCustomer = 0;
    }

    public static void main( String[] args ) {
        Customer customer = new Customer( Long.parseLong(args[1]) );
        customer.start();

        Random rand = new Random();

        while( true ) {

            try {
                Thread.sleep(3000);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }

            int index = rand.nextInt(5) + 1;
            if ( customer.canSendMessages() ) {
                String[] message = { "user", "password", "investement", "BLABLA", "END" };
                customer.sendMessage("CUSTOMER" + Integer.toString( index ), message );
                customer.stopSending();
            }
        }
    }
}
