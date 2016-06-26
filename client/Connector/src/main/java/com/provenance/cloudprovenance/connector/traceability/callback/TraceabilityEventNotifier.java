package com.provenance.cloudprovenance.connector.traceability.callback;

@Deprecated 
public class TraceabilityEventNotifier {

	public TraceabilityNotificationEvent<String> tne;
	
	public TraceabilityEventNotifier (TraceabilityNotificationEvent<String> event){
		tne = event;
	}
	
	public String doSomeWork(){
		
		for (int i=0; i<10; i++){
			
			if (i%2==0){
				return tne.storeNotification();
			}	
		}
		return null;
		
	}
	
}
