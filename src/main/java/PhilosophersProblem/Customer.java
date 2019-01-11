
package PhilosophersProblem;

import Communication.LocalService;
import java.util.Random;

public class Customer extends LocalService {

    enum StatusFork {
        NONE,
        DIRTY,
        CLEAN
    }

    //Own Fork
    private StatusFork ownFork;

    //Neighbour depend Fork
    private StatusFork dependFork;

    //Own Index
    private int ownIndex;

    //Neighbour depend Index
    private int dependIndex;

    private boolean isPreparingToEat;
    private int wantedIndex;


    Customer( int index, int dependIndex ) {
        super("Customer","CUSTOMER" + Integer.toString(index));
        this.ownIndex = index;
        this.wantedIndex = -1;
        this.dependIndex = dependIndex;
        this.ownFork = StatusFork.DIRTY;
        this.dependFork = StatusFork.NONE;
        this.isPreparingToEat = false;
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        try {

            int index = Integer.parseInt(request.substring("CUSTOMER".length(), request.length()));

            switch (messages[0]) {
                case "GIVE_BACK_FORK":
                    //System.out.println( "GIVE_BACK_FORK" );
                    ownFork = StatusFork.CLEAN;
                    if ( ownFork.equals(StatusFork.CLEAN) && !dependFork.equals(StatusFork.NONE) ) {
                        eatTask();
                    }
                    break;
                case "GIVE_FORK":
                    //System.out.println( "GIVE_FORK" );
                    if ( index == dependIndex ) {
                        dependFork = StatusFork.CLEAN;
                        if ( dependFork.equals(StatusFork.CLEAN) && !ownFork.equals(StatusFork.NONE) ) {
                            eatTask();
                        }
                    } else {
                        System.out.println( getName() + ": strange index.");
                    }
                    break;
                case "GIVE_ME_FORK":
                    //System.out.println( "GIVE_ME_FORK" );
                    if (ownFork.equals(StatusFork.DIRTY) ) {
                        this.ownFork = StatusFork.NONE;
                        String[] message = { "GIVE_FORK" };
                        sendMessage("CUSTOMER" + Integer.toString( index ), message );
                    } else {
                        wantedIndex = index;
                    }
                    break;
                default:
                    System.out.println( getName() + ": strange command.");
            }

        } catch ( Exception e ) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println( getName() + ": strange message >> " + request + " >> " + String.join(", ", messages));
        }
    }

    private void eatTask() {

        System.out.println( getName() + ": Eating");
        //TODO

        dependFork = StatusFork.DIRTY;

        if ( wantedIndex != -1 ) {
            ownFork = StatusFork.NONE;
            String[] secondMessage = { "GIVE_FORK" };
            sendMessage("CUSTOMER" + Integer.toString( wantedIndex ), secondMessage );
            wantedIndex = -1;
        } else {
            ownFork = StatusFork.DIRTY;
        }

        //isPreparingToEat = false;
    }

    public void periodTask() {
        try {
            Thread.sleep(2000);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println( "ownFork: " + ownFork + " dependFork: " + dependFork );

        //Want to eat
        String[] message = { "GIVE_ME_FORK" };
        sendMessage("CUSTOMER" + Integer.toString( dependIndex ), message );
        if ( ownFork.equals( StatusFork.NONE ) ) {
            String[] secondMessage = { "GIVE_BACK_FORK" };
            sendMessage("CUSTOMER" + Integer.toString( wantedIndex ), secondMessage );
        }
        //isPreparingToEat = true;
    }

    public static void main( String[] args ) {
        Customer customer = new Customer(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
        customer.start();

        while( true ) {
            customer.periodTask();
        }
    }
}
