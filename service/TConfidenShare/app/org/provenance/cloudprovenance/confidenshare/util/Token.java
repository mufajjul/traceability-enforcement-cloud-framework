package org.provenance.cloudprovenance.confidenshare.util;

import java.util.Date;

import javax.persistence.OneToOne;

import play.db.jpa.Model;

/**
 * @author guillaumevalverde
 *
 */
@javax.persistence.Entity
public class Token extends Model{
	private final Long TOKENTIME = (long) (1000*30*30*24); // one day
	
	/**
	 * date when the Token has benn created
	 */
	public Date dateCreated;
	
	
	/**
	 * unique random ID for the token
	 */
	public String idRandom;
	
    @OneToOne
    public Device device;
	
	/**
	 * Constructor,
	 * set up the date end the unique id
	 */
	public Token(Device d){
		this.device = d;
		dateCreated = new Date();
		idRandom = org.provenance.cloudprovenance.confidenshare.util.OneTimeUrlFactory.getInstance().generateOneTimeId();
	}
	
	
	
	/**
	 * Check the token is not out of date, after 1 day it is
	 * @return true or false
	 */
	private boolean isOutOfDate(){
		Date dateTmp = new Date();
		if((dateTmp.getTime()-dateCreated.getTime())>TOKENTIME)
			return false;
		else return true;
	}
	
	/**
	 * Check the Token
	 * @param tokenTmp : id given by the client
	 * @return true or false
	 */
	public static boolean CheckToken(String tokenTmp){
		
		Token tok = find("byidRandom", tokenTmp).first();
		if(tok!=null && !tok.isOutOfDate())
			return true;
		else 
			return false;
	}
}
