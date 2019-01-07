
package QueueSystem;

import Communication.Replier;
import Communication.Requestor;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws Exception {

        Requestor requestor = new Requestor("Requestor1",  "TEST.FOO");
        Replier replier = new Replier("Replier1",  "TEST.FOO");

        requestor.start();
        requestor.sendMessage( "hueueueueue" );
        requestor.sendMessage( "aaaaaaaaaaa" );
        Thread.sleep(1000);
        replier.start();
        Thread.sleep(5000);
        replier.receiveMessage();
    }

}
