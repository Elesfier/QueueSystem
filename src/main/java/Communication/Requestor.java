
package Communication;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.util.PriorityQueue;

public class Requestor implements Runnable {

    boolean isRunning;
    boolean canSend;
    private Thread thread;
    private String threadName;
    private String queuePathFrom;
    private Connection connection;
    private PriorityQueue<String> queue;

    private PriorityQueue<String> getQueue() {
        synchronized (this) {
            return queue;
        }
    }

    public Requestor(String threadName, String queuePathFrom, String brokerUrl) throws JMSException {
        this.threadName = threadName;
        this.queuePathFrom = queuePathFrom;
        this.isRunning = false;
        this.canSend = true;
        queue = new PriorityQueue<String>();
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        connection = factory.createConnection();
        System.out.println("Creating Thread: " + threadName);
    }

    public void start() throws JMSException {
        System.out.println("Starting Thread: " + threadName );
        if (thread == null) {
            thread = new Thread (this, threadName);
            isRunning = true;
            connection.start();
            thread.start();
        }
    }

    public void close() throws JMSException {
        isRunning = false;
        getQueue().clear();
        connection.close();
    }

    public void sendMessage( String queuePath, String message ) {
        if ( isRunning ) {
            if ( queuePath.indexOf("|") != -1 ) return;
            getQueue().add( queuePath + "|<" + queuePathFrom + "><" + message + ">");
        }
    }

    public void sendMessage( String queuePath, String[] messages ) {
        if ( isRunning ) {
            if ( queuePath.indexOf("|") != -1 ) return;
            getQueue().add( queuePath + "|<" + queuePathFrom + "><" + String.join("><", messages ) + ">");
        }
    }

    public int getQueueSize() {
        return getQueue().size();
    }

    public void setCanSend( boolean newCanSend ) {
        if ( isRunning ) {
            canSend = newCanSend;
        }
    }

    public void run() {

        try {
            while ( isRunning ) {
                if ( canSend && (getQueue().size() > 0) ) {

                    // Create a Session
                    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                    // Get MessageStr
                    String messageStr = getQueue().poll();

                    // Queue Path create
                    String queuePath = messageStr.substring(0, messageStr.indexOf("|"));

                    // Create the destination (Topic or Queue)
                    Destination destination = session.createQueue(queuePath);

                    // Create a MessageProducer from the Session to the Topic or Queue
                    MessageProducer producer = session.createProducer(destination);
                    producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                    // Create a messages
                    TextMessage message = session.createTextMessage(messageStr.substring(messageStr.indexOf("|")+1, messageStr.length()));

                    // Send the message
                    producer.send(message);

                    // Clean up
                    session.close();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }

    }
}
