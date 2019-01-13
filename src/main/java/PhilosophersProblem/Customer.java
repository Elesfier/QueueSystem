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
    private int wantedBackIndex;
    private int wantedIndexBack;

    private boolean dependIndexWantBack;
    private boolean backIndexWant;
    private boolean wantEat;

    private final boolean logON = false;

    private final String CustomerQueueName = "CUSTOMER30";

    Customer( int backIndex, int index, int dependIndex, String brokerUrl ) {
        super("Customer" + Integer.toString(index),"CUSTOMER30" + Integer.toString(index), brokerUrl);
        this.ownIndex = index;
        this.wantedIndex = -1;
        this.wantedBackIndex = -1;
        this.wantedIndexBack = backIndex;
        this.dependIndex = dependIndex;
        this.ownFork = StatusFork.DIRTY;
        this.dependFork = StatusFork.NONE;
        this.isPreparingToEat = false;

        this.dependIndexWantBack = false;
        this.backIndexWant = false;
        this.wantEat = false;
    }

    public synchronized void tryToEat() {
        if ( !ownFork.equals(StatusFork.NONE) && !dependFork.equals(StatusFork.NONE) ) {
            System.out.println( "> " + getName() + ": pobiera dane dzięki pozyskanym kluczom.");
            ownFork = StatusFork.DIRTY;
            dependFork = StatusFork.DIRTY;
        }
    }

    public void sendDependFork() {
        if ( dependFork.equals(StatusFork.DIRTY) ) {
            if(logON)System.out.println("> " + getName() + ": zwraca klucz.");
            dependFork = StatusFork.NONE;
            String[] message = {"GIVE_BACK_FORK"};
            sendMessage(CustomerQueueName + Integer.toString(dependIndex), message);
            dependIndexWantBack = false;
        }
    }

    public void sendOwnFork() {
        if ( ownFork.equals(StatusFork.DIRTY) ) {
            if(logON)System.out.println( "> " + getName() + ": wysyla swoj klucz do " + "Customer" + Integer.toString(wantedIndexBack) + ".");
            ownFork = StatusFork.NONE;
            String[] message = { "GIVE_FORK" };
            sendMessage(CustomerQueueName + Integer.toString( wantedIndexBack ), message );
            backIndexWant = false;
        }
    }

    public void getDependFork() {
        if ( dependFork.equals( StatusFork.NONE ) ) {
            dependFork = StatusFork.CLEAN;
            if(logON)System.out.println( "> " + getName() + ": pozyskuje klucz od " + "Customer" + Integer.toString(dependIndex) + ".");
        }
    }

    public void getOwnFork() {
        if ( ownFork.equals( StatusFork.NONE ) ) {
            ownFork = StatusFork.CLEAN;
            if(logON)System.out.println( "> " + getName() + ": odzyskuje z powrotem swoj klucz.");
        }
    }

    public void sendRequestOfDepend() {
        if ( dependFork.equals( StatusFork.NONE ) ) {
            if(logON)System.out.println("> " + getName() + ": wysyla prosbe o danie klucza " + "Customer" + Integer.toString( dependIndex ) + ".");
            String[] message = {"GIVE_ME_YOUR_FORK"};
            sendMessage(CustomerQueueName + Integer.toString(dependIndex), message);
        } else {
            tryToEat();
        }
    }

    public void sendRequestOfOwn() {
        if ( ownFork.equals( StatusFork.NONE ) ) {
            if(logON)System.out.println("> " + getName() + ": wysyla prosbe o danie z powrotem klucza od" + "Customer" + Integer.toString( wantedIndexBack ) + ".");
            String[] secondMessage = {"GIVE_BACK_MY_FORK"};
            sendMessage(CustomerQueueName + Integer.toString(wantedIndexBack), secondMessage);
        } else {
            tryToEat();
        }
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        try {

            int index = Integer.parseInt(request.substring(CustomerQueueName.length(), request.length()));

            switch (messages[0]) {

                case "GIVE_BACK_FORK": //Zdobywasz fork own
                    getOwnFork();
                    break;

                case "GIVE_FORK": //Zdobywasz fork depend
                    getDependFork();
                    break;

                case "GIVE_BACK_MY_FORK": //Tracisz fork depend
                    sendDependFork();
                    break;

                case "GIVE_ME_YOUR_FORK": //Tracisz fork own
                    wantedIndexBack = index;
                    sendOwnFork();
                    break;

                default:
                    System.out.println( getName() + ": strange command.");
            }

        } catch ( Exception e ) {
            System.out.println("Exception: " + e.getMessage());
            System.out.println( getName() + ": strange message >> " + request + " >> " + String.join(", ", messages));
        }
    }

    private synchronized void eatTask() {

        //if ( wantEat ) {
            System.out.println( "> " + getName() + ": pobiera dane dzięki pozyskanym kluczom.");
        //}

        if ( backIndexWant ) {
            sendOwnFork();
        } else {
            ownFork = StatusFork.DIRTY;
        }

        if ( dependIndexWantBack ) {
            sendDependFork();
        } else {
            dependFork = StatusFork.DIRTY;
        }

        wantEat = false;
    }

    public void periodTask() {

        try {
            Thread.sleep(2000);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println( "Wlany klucz: " + ownFork + " Klucz sasiada: " + dependFork );
        sendRequestOfDepend();
        sendRequestOfOwn();
    }

    public static void main( String[] args ) {
        if ( args.length < 4 ) return;
        Customer customer = new Customer(Integer.parseInt(args[0]),Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
        customer.start();

        while( true ) {
            customer.periodTask();
        }

    }
}
