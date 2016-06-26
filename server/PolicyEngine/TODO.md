- Made changes to the StandardFunctionFactory, EqualFunction. 

-- Store the policies in the Policy store

-- Delete all entries from the eXist DB collection (Add a method to the provenance store) -- clearCollection

-- “urn:oasis:names:tc:xacml:1.0:function:string-one-and-1132 only” is used - a bag containing exactly one value.  In the future, expand to use multiple values.


-- It should match only with few parameters from the policy with the values from the store


-- Include the request and response directories in the deployment WAR 


-- Use the same IP address (i/e localhost or 127.0.0.1 for the service, otherwise the session may not work


**** Policy controller Interface update:  storeInvokePostURI = "http://" + getIP() + ":" + getPort()
				+ "/PolicyController" + "/" + serviceId + "/" + policyRequestId + "/"+ policyRequest + "/" + policyId; 

TODO - ---------------- Need to add the meeting traceability from the desktop.... address this !!!!!

