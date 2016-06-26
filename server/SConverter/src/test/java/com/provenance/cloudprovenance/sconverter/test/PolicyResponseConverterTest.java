package com.provenance.cloudprovenance.sconverter.test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.provenance.cloudprovenance.sconverter.PolicyResponseConverter;
import com.provenance.cloudprovenance.sconverter.translate.ResourceTranslator;


@ContextConfiguration("classpath:beans.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class PolicyResponseConverterTest {

	@Autowired
	PolicyResponseConverter prc;
	
	static Logger logger = Logger.getLogger(PolicyResponseConverter.class);
	
	@Test 
	public void convertXACMLresponseTocProvlResponseTest(){
		
		String outcome = prc.XMLpolicyResponse(this.tempData());
		
		logger.info(outcome);
		Assert.notNull(outcome);		
	}
	
	
	public String tempData(){
		
		String XACMLdata = "<Response xmlns=\"urn:oasis:names:tc:xacml:3.0:core:schema:wd-17\"> \n"+
		"<Result>\n"+
		"<Decision>Deny</Decision>\n"+
		"<Status>\n"+
		"<StatusCode Value=\"urn:oasis:names:tc:xacml:1.0:status:ok\"/>\n"+
		"</Status>\n"+
		"<PolicyIdentifierList>\n"+
		"<PolicyIdReference>urn:oasis:names:tc:xacml:3.0:cprovl:confidenshare:policy5</PolicyIdReference>\n"+
		"</PolicyIdentifierList>\n"+
		"<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">"+
		"<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:resource-id\"\n"+
		 "IncludeInResult=\"true\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">confidenshare:document1</AttributeValue>\n"+
		"</Attribute>\n"+
		"</Attributes>\n"+
		"<Attributes Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\">\n"+
		"<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:subject-id\"\n"+
		 "IncludeInResult=\"true\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">confidenshare:Bob;confidenshare:Phil</AttributeValue>\n"+
		"</Attribute>\n"+
		"</Attributes>\n"+
		"<Attributes Category=\"urn:oasis:names:tc:xacml:3.0:attribute-category:action\">\n"+
		"<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:action-id\"\n"+
		"IncludeInResult=\"true\"><AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">confidenshare:share</AttributeValue>\n"+
		"</Attribute>\n"+
		"</Attributes>\n"+
		"</Result>\n"+
		"</Response>\n";

		return XACMLdata;
	}
	
}
