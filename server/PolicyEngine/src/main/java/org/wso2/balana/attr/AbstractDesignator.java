package org.wso2.balana.attr;

import java.net.URI;

import org.wso2.balana.cond.Evaluatable;

/**
 *
 */
public abstract class AbstractDesignator implements Evaluatable {

	public abstract URI getId();

}
