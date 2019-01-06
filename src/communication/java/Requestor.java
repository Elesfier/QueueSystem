
package Communication;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class Requestor implements Runnable {

    private Thread t;
    private String threadName;

    Requestor( String name){
        threadName = name;
        System.out.println("Creating Thread:" +  threadName );
    }

    public void run() {
        JmsConnectionFactory cf = null;
        Connection connection = null;
        Session session = null;
        Destination reqQ = null;
        Destination repQ = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;

        try {

            // Create a connection factory
            JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
            cf = ff.createConnectionFactory();

            // Set the properties
            cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_BINDINGS);
            cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, "QM2");

            // Create JMS objects
            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create destination to send requests
            reqQ = session.createQueue("queue:///REQUESTQ");
            // Create destination to read replies
            repQ = session.createQueue("queue:///REPLYQ");

            // Create producer
            producer = session.createProducer(reqQ);

            // Create a request message
            Message requestMessage = session.createTextMessage("Requesting a service");
            // Tell the responder where to put replies.
            requestMessage.setJMSReplyTo(repQ);
            // Send it off
            producer.send(requestMessage);

            // Get only that reply that matches my request message id.
            String selector = "JMSCorrelationID='" + requestMessage.getJMSMessageID()+"'";

            // Create consumer with selector
            consumer = session.createConsumer(repQ, selector);

            // Start the connection
            connection.start();

            // Get the message
            Message receivedMessage = consumer.receive(35000);
            if(receivedMessage != null)
                System.out.println("\nRequestor received message:\n" + receivedMessage);
            else
                System.out.println("No message received");

        } catch(Exception ex) {
            System.out.println(threadName);
            System.out.println(ex);
        }
    }

    // Start thread
    public void start ()
    {
        System.out.println("Starting " +  threadName );
        if (t == null)
        {
            t = new Thread (this, threadName);
            t.start ();
        }
    }
}
