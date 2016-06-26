package com.provenance.cloudprovenance.connector.traceability.callback;

@Deprecated 
public class TestCallBack {

	public static void main(String[] args) {

		TraceabilityNotificationEvent<String> cal = new CallMe();
		
		TraceabilityEventNotifier  tsv = new TraceabilityEventNotifier (cal);
		
		System.out.println("waiting .....");
		System.out.println(tsv.doSomeWork());
		
		
		System.out.println ("Output complete ....");
		
	}

}
