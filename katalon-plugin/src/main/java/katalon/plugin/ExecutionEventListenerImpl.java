package katalon.plugin;

import org.osgi.service.event.Event;

import com.katalon.platform.api.event.EventListener;
import com.katalon.platform.api.extension.EventListenerInitializer;

public class ExecutionEventListenerImpl implements EventListenerInitializer {

	@Override
	public void registerListener(EventListener listener) {
		listener.on(Event.class, event -> {
			System.out.println("CoE EventListenerInitializer: " + event.getTopic());
		});

	}

}
