package bgu.spl.mics;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private ConcurrentHashMap <MicroService, LinkedBlockingQueue<Message>> microserviceMap;
	private ConcurrentHashMap <Class<?extends Message>, ConcurrentLinkedQueue<MicroService>> messageMap;
	private ConcurrentHashMap <Event, Future> EventsSolutions;


	/**
	 * messageBus will be a singleton object
	 */
	private static class SingletonHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl(){
		microserviceMap = new ConcurrentHashMap<>();
		messageMap = new ConcurrentHashMap<>();
		EventsSolutions = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance(){
		return SingletonHolder.instance;
	}


	@Override
	/**
	 * Add the microService to the queue of this event type.
	 */
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		synchronized (type) {
			if (!messageMap.containsKey(type)) {
				ConcurrentLinkedQueue<MicroService> subscribe = new ConcurrentLinkedQueue<>();
				messageMap.put(type, subscribe);
			}
		}
		ConcurrentLinkedQueue <MicroService> temp = messageMap.get(type);
		temp.add(m);

	}

	@Override
	/**
	 * Add the microService to the queue of this broadcast type.
	 */
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (type) {
			if (!messageMap.containsKey(type)) {
				ConcurrentLinkedQueue<MicroService> subscribe = new ConcurrentLinkedQueue<>();
				messageMap.put(type, subscribe);
			}
		}
		ConcurrentLinkedQueue <MicroService> temp = messageMap.get(type);
		temp.add(m);
    }

	@Override @SuppressWarnings("unchecked")
	/**
	 * This method associate the result to the future object of the event e.
	 */
	public <T> void complete(Event<T> e, T result) {
		Future<T> solution = EventsSolutions.get(e);
		solution.resolve(result);
	}

	@Override
	/**
	 * get the MicroService queue for broadcast b from the messageMap and send b to all the
	 * microservices in the queue.
 	 */
	public void sendBroadcast(Broadcast b) {
		synchronized (b.getClass()) {
			ConcurrentLinkedQueue<MicroService> sendTo = messageMap.get(b.getClass());
			if (!sendTo.isEmpty()) {
				for(MicroService m: sendTo){
					LinkedBlockingQueue<Message> mMessages = microserviceMap.get(m);
					if (mMessages!=null){
						mMessages.add(b);
					}
				}
			}
		}

	}


	@Override
	/**
	 * find the first microService (by the Round Robin Manner) and enqueue the event to the right
	 *  microservice message queue.
	 */
	public <T> Future<T> sendEvent(Event<T> e) {
		synchronized (e.getClass()) {
			if (messageMap.containsKey(e.getClass())) {
				ConcurrentLinkedQueue<MicroService> eMicroService = messageMap.get(e.getClass());
				if (!eMicroService.isEmpty()) {
					MicroService fighter = eMicroService.remove();
					eMicroService.add(fighter);
					LinkedBlockingQueue<Message> fighterMessages = microserviceMap.get(fighter);
					Future<T> eSolution = new Future<>();
					EventsSolutions.put(e, eSolution);
					fighterMessages.add(e);
					return EventsSolutions.get(e);
				}
			}
			return null;
		}
	}

	@Override
	/**
	 * check if the microService already have a queue, and if not create one for him.
	 */
	public void register(MicroService m) {
		if(!microserviceMap.containsKey(m))
		{
			LinkedBlockingQueue<Message> q = new LinkedBlockingQueue<Message>() ;
			microserviceMap.put(m,q);
		}
	}

	@Override
	/**
	 * check if a microService exist, if it does delete his queue from the microserviceMap and his reference from
	 * the messageMap.
	 */
	public void unregister(MicroService m) {
		if(microserviceMap.containsKey(m)) {
			microserviceMap.remove(m);
			for (ConcurrentLinkedQueue<MicroService>  Q : messageMap.values()){
				Q.remove(m);
			}
		}
	}

	@Override
	/**
	 * dequeue the next message in the message queue of the microservice m.
	 */
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if (!microserviceMap.containsKey(m))
			throw new IllegalArgumentException();
		LinkedBlockingQueue <Message> microserviceQueue = microserviceMap.get(m);
		return microserviceQueue.take();
	}
}
