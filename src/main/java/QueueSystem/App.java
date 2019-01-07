
package QueueSystem;

import Communication.LocalService;

class BaBu extends LocalService {

    public BaBu() {
        super("Babu");
        addReplier("BabuRep", "TEST.FOO");
        addRequestor("BabuReq", "TEST.FOO");
        start();
    }

    @Override
    public void onReceivedMessage(String request, String[] messages) {
        System.out.println( "Received form " + request + " : " + messages[0] );
    }
}

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {

        LocalService service = new BaBu();

        service.sendMessage("BabuReq", "laaaa");

    }

}
