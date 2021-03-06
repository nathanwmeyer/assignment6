package business;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import beans.*;
import data.OrdersDataService;

/**
 * Session Bean implementation class OrdersBusinessService
 */
@Alternative
@Stateless
@Local(OrdersBusinessInterface.class)
@LocalBean
public class OrdersBusinessService implements OrdersBusinessInterface {
	
	@Resource(mappedName="java:/ConnectionFactory")
	private ConnectionFactory connectionFactory;

	@Resource(mappedName="java:/jms/queue/Order")
	private Queue queue;

	List<Order> orders;
	
	@EJB
	OrdersDataService service;
    /**
     * Default constructor. 
     */
    public OrdersBusinessService() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see OrdersBusinessInterface#test()
     */
    public void test() {//console test message
    	System.out.println("Hello from the OrdersBusinessService");
    }

	@Override
	public List<Order> getOrders() {//return all orders in database
		// TODO Auto-generated method stub
		
		return service.findAll();
	}

	@Override
	public void setOrders(List<Order> orders) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void sendOrder(Order order) {
		//send a Message for an Order
		try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer messageProducer = session.createProducer(queue);
			
			ObjectMessage message2 = session.createObjectMessage();
			message2.setObject((Serializable) order);
			
			messageProducer.send(message2);
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
