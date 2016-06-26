package com.provenance.cloudprovenance.connector.traceability.callback;

@Deprecated 
public class CallMe implements TraceabilityNotificationEvent<String>{

	@Override
	public String storeNotification() {

		for (long i=0; i <10000000000l; i++){
			;;
		}
		
		return "The outcome is ";
	}

	
}
