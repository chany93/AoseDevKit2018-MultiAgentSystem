package unitn.adk2018.event;

public abstract class RequestMessage extends Message {

//	public static enum DeliveryStatus { READY, SENT, RECEIVED };
//	public static enum ResponseStatus { WAITING, ACCEPTED, REFUSED };
//	public static enum ExecutionStatus { WAITING, SUCCESS, FAILURE };
//	
//	public static enum Response { READY, SENT, RECEIVED, ACCEPTED, REFUSED, SUCCESS, FAILURE };
//	
//	private Response response;
	
	

	public RequestMessage(String _from, String _to) {
		super(_from, _to);
	}
	
	
	
//	public void setResponse (Response response) {
//		this.response = response;
//	}
//	
//	public Response getResponse () {
//		return response;
//	}
	
	@Override
	public String getTitle() {
		return "REQUEST";
	}
	
}
