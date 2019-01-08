
package Communication;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.util.PriorityQueue;

public class Replier implements Runnable, ExceptionListener {

    boolean isRunning;
    boolean canSend;
    private String queuePath;
    private Thread thread;
    private String threadName;
    private Connection connection;
    private PriorityQueue<String> queue;

    private synchronized PriorityQueue<String> getQueue() {
        return queue;
    }

    public Replier(String threadName, String queuePath, String brokerUrl) throws JMSException {
        this.threadName = threadName;
        this.queuePath = queuePath;
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
            connection.setExceptionListener(this);
            thread.start();
        }
    }

    public void close() throws JMSException {
        isRunning = false;
        getQueue().clear();
        connection.close();
    }

    public int getQueueSize() {
        return getQueue().size();
    }

    public String receiveMessage() {
        return getQueue().poll();
    }

    public void run() {
        try {
            while ( isRunning ) {

                // Create a Session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create the destination (Topic or Queue)
                Destination destination = session.createQueue(queuePath);

                // Create a MessageConsumer from the Session to the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                // Wait for a message
                Message message = consumer.receive();

                // Received messages
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    getQueue().add( textMessage.getText() );
                } else {
                    System.out.println("Received: " + message);
                }

                // Clean up
                consumer.close();
                session.close();

            }
        } catch (Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured. Shutting down replier.");
    }
}
