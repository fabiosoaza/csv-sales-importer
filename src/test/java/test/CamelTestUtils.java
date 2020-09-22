package test;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;

public class CamelTestUtils {

	public static final DefaultExchange createDefaultExchange() {
		DefaultCamelContext context = new DefaultCamelContext();
		DefaultExchange exchange = new DefaultExchange(context);
		DefaultMessage messageIn = new DefaultMessage(context);
		exchange.setIn(messageIn);
		DefaultMessage messageOut = new DefaultMessage(context);
		exchange.setOut(messageOut);
		return exchange;
	}
	
}
