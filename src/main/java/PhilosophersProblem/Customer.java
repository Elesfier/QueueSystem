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

    public boolean logON;

    private boolean backForkOwn;
    private boolean backForkDepend;

    private int isOkeyForkDependAproved;
    private int isOkeyForkOwnAproved;

    private final String CustomerQueueName = "CUSTOMER";

    Customer( int backIndex, int index, int dependIndex, String brokerUrl, boolean logON ) {
        super("Customer" + Integer.toString(index),"CUSTOMER" + Integer.toString(index), brokerUrl);
        this.ownIndex = index;
        this.wantedIndex = -1;
        this.wantedBackIndex = -1;
        this.wantedIndexBack = backIndex;
        this.dependIndex = dependIndex;
        this.ownFork = StatusFork.DIRTY;
        this.dependFork = StatusFork.NONE;
        this.isPreparingToEat = false;
        this.logON = logON;

        this.dependIndexWantBack = false;
        this.backIndexWant = false;
        this.wantEat = false;

        this.backForkDepend = false;
        this.backForkOwn = false;

        this.isOkeyForkDependAproved = 0;
        this.isOkeyForkOwnAproved = 0;
    }

    public synchronized void tryToEat() {
        if ( !ownFork.equals(StatusFork.NONE) && !dependFork.equals(StatusFork.NONE) ) {
            System.out.println( "> " + getName() + ": pobiera dane dzięki pozyskanym kluczom.");
            ownFork = StatusFork.DIRTY;
            dependFork = StatusFork.DIRTY;
            if (backForkDepend) sendDependFork();
            if (backForkOwn) sendOwnFork();
        }
    }

    public void sendDependFork() {
        isOkeyForkDependAproved++;
        if ( dependFork.equals(StatusFork.DIRTY) || (isOkeyForkDependAproved >= 5) ) {
            if(logON)System.out.println("> " + getName() + ": zwraca klucz.");
            dependFork = StatusFork.NONE;
            String[] message = {"GIVE_BACK_FORK"};
            sendMessage(CustomerQueueName + Integer.toString(dependIndex), message);
            isOkeyForkDependAproved = 0;
            backForkDepend = false;
        } else if ( dependFork.equals(StatusFork.CLEAN) ) {
            backForkDepend = true;
        }
    }

    public void sendOwnFork() {
        isOkeyForkOwnAproved++;
        if ( ownFork.equals(StatusFork.DIRTY) || (isOkeyForkOwnAproved >= 5) ) {
            if(logON)System.out.println( "> " + getName() + ": wysyla swoj klucz do " + "Customer" + Integer.toString(wantedIndexBack) + ".");
            ownFork = StatusFork.NONE;
            String[] message = { "GIVE_FORK" };
            sendMessage(CustomerQueueName + Integer.toString( wantedIndexBack ), message );
            isOkeyForkOwnAproved = 0;
            backForkOwn = false;
        } else if ( ownFork.equals(StatusFork.CLEAN) ) {
            backForkOwn = true;
        }
    }

    public void getDependFork() {
        if ( dependFork.equals( StatusFork.NONE ) ) {

            dependFork = StatusFork.CLEAN;
            if(logON)System.out.println( "> " + getName() + ": pozyskuje klucz od " + "Customer" + Integer.toString(dependIndex) + ".");
        }

        String[] message2 = {"GIVE_FORK_APROVED"};
        sendMessage(CustomerQueueName + Integer.toString(dependIndex), message2);
    }

    public void getOwnFork() {
        if ( ownFork.equals( StatusFork.NONE ) ) {

            ownFork = StatusFork.CLEAN;
            if(logON)System.out.println( "> " + getName() + ": odzyskuje z powrotem swoj klucz.");
        }

        String[] message2 = {"GIVE_BACK_FORK_APROVED"};
        sendMessage(CustomerQueueName + Integer.toString(wantedIndexBack), message2);
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

                case "GIVE_BACK_FORK_APROVED": //Potwierdzenie odebrania forka own
                    isOkeyForkOwnAproved = 0;
                    break;

                case "GIVE_FORK_APROVED": //Potwierdzenie odebrania forka depend
                    isOkeyForkOwnAproved = 0;
                    break;

                case "GIVE_BACK_MY_FORK": //Tracisz fork depend
                    sendDependFork();
                    break;

                case "GIVE_ME_YOUR_FORK": //Tracisz fork own
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
            Thread.sleep(1500);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println( "Wlany klucz: " + ownFork + " (" + isOkeyForkOwnAproved + ") Klucz sasiada: " + dependFork + " (" + isOkeyForkDependAproved + ")" );
        sendRequestOfDepend();
        sendRequestOfOwn();
    }

    public static void main( String[] args ) {
        if ( args.length < 4 ) return;
        Customer customer = new Customer(Integer.parseInt(args[0]),Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3], (args.length == 5)?(true):(false));
        customer.start();

        while( true ) {
            customer.periodTask();
        }

    }
}
