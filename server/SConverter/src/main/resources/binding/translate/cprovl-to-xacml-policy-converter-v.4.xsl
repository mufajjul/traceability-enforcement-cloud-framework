<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:cprov="http://labs.orange.com/uk/cprov#" xmlns:cprovl="http://labs.orange.com/uk/cprovl#"
    xmlns:prov="http://www.w3.org/ns/prov#" xmlns:cprovd="http://labs.orange.com/uk/cprovd#"
    xmlns:r="http://labs.orange.com/uk/r#" xmlns:cu="http://www.w3.org/1999/xhtml/datatypes/"
    xmlns:ex="http://labs.orange.com/uk/r#" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    exclude-result-prefixes="xs xd" version="2.0">
    <xd:doc scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Updated on:</xd:b> Feb 12, 2013</xd:p>
            <xd:p><xd:b>Author:</xd:b> Mufajjul Ali </xd:p>
            <xd:p/>
        </xd:desc>
    </xd:doc>

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">

        <Policy xmlns="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 file:/C:/Users/lbwm7009/Dropbox/EngD/year3/schema/cProvl/xacml/xacml-core-v3-schema-wd-17.xsd"
            PolicyId="urn:oasis:names:tc:xacml:3.0:example:SimplePolicy1" Version="1.0"
            RuleCombiningAlgId="urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:first-applicable">

            <xsl:attribute name="PolicyId">
                <xsl:text>urn:oasis:names:tc:xacml:3.0:cprovl:</xsl:text>
                <xsl:value-of select="cprovl:POLICY/cprovl:entity/@prov:id"/>
            </xsl:attribute>

            <Description>
                <xsl:value-of select="cprovl:POLICY/cprovl:entity/cprovl:description"/>
            </Description>

            <PolicyDefaults>
                <XPathVersion>http://www.w3.org/TR/1999/Rec-xpath-19991116</XPathVersion>
            </PolicyDefaults>

            <Target/>
            <xsl:for-each select="cprovl:POLICY/cprovl:RULE">
                <Rule RuleId="urn:oasis:names:tc:xacml:3.0:example:SimpleRule1" Effect="Permit">
                    <xsl:attribute name="RuleId">
                        <xsl:text>urn:oasis:names:tc:xacml:3.0:</xsl:text>
                        <xsl:value-of select="cprovl:entity/@prov:id "/>
                    </xsl:attribute>
                    <xsl:attribute name="Effect">
                        <xsl:variable name="temp"
                            select="cprovl:THEN/cprovl:entity/cprovl:actionType"/>
                        <xsl:value-of select=" tokenize($temp, ':')[position()=2] "/>
                    </xsl:attribute>

                    <Description>
                        <xsl:value-of select="cprovl:entity/cprovl:description"/>
                    </Description>

                    <xsl:if test="count(cprovl:IF)!=0">
                        <Target>
                            <!-- converting cProvl target to XACML target -->

                            <!-- Find all the IDs, and check if they are variables or elements -->
                            <xsl:for-each select="cprovl:IF/cprovl:target">
                                <xsl:variable name="fieldType"
                                    select="  child::cprovl:ID/@cprovl:category_type"/>

                                <xsl:variable name="varType"
                                    select="child::cprovl:ID/@cprovl:var_type"/>

                                <!--   <xsl:value-of select = "$varType" /> -->

                                <xsl:if test="$varType='cprovd:new'">

                                    <AnyOf>
                                        <AllOf>
                                            <Match
                                                MatchId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-d-id-match">
                                                <AttributeValue
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
                                                  XPathCategory="urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov:var">
                                                  <!-- get the identifier and element -->
                                                  <!-- ="child::cprovl:ID/@cprovl:var_type"/> -->
                                                  <!--<xsl:text></xsl:text>-->

                                                  <xsl:value-of select="child::cprovl:ID"/>
                                                </AttributeValue>

                                                <AttributeDesignator MustBePresent="false"
                                                  AttributeId="urn:oasis:names:tc:xacml:1.0:subject:subject-id"
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression">

                                                  <!-- Check to see the type of resource -->

                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov:var</xsl:text>
                                                  </xsl:attribute>
                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:cprov-var-id</xsl:text>
                                                  </xsl:attribute>
                                                </AttributeDesignator>
                                            </Match>
                                        </AllOf>
                                    </AnyOf>
                                </xsl:if>

                                <xsl:if test="not(boolean($varType))">
                                    <AnyOf>
                                        <AllOf>
                                            <Match
                                                MatchId="urn:oasis:names:tc:xacml:1.0:function:string-equal">

                                                <AttributeValue
                                                  DataType="http://www.w3.org/2001/XMLSchema#string">
                                                  <!-- get the identifier and element -->

                                                  <!-- <xsl:value-of
                                                  select="preceding::cprovl:var[position()=1]/@type"/>
                                                  <xsl:text></xsl:text>
                                                   -->
                                                  <xsl:value-of select="child::cprovl:ID"/>
                                                </AttributeValue>
                                                <!--
                                                        <xsl:variable name="fieldType"
                                                            select="preceding::cprovl:var[position()=1]/@cprovl:category_type"/>
                                                        -->

                                                <AttributeDesignator MustBePresent="false"
                                                  AttributeId="urn:oasis:names:tc:xacml:1.0:subject:subject-id"
                                                  DataType="http://www.w3.org/2001/XMLSchema#string">

                                                  <!-- Check to see the type of resource -->
                                                  <xsl:if test="$fieldType='cprovd:Action'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:action</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:action-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Service'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:service</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:service-id</xsl:text>
                                                  </xsl:attribute>
                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Resource'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:resource</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:resource-id</xsl:text>
                                                  </xsl:attribute>
                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Subject'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:1.0:subject-category:access-subject</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:subject-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="not(boolean($fieldType))">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:cprov-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <!-- assing the attribute Id -->
                                                  <!--
                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:</xsl:text>
                                                  <xsl:value-of select="current()"/>
                                                  </xsl:attribute>
                                                  -->
                                                </AttributeDesignator>
                                            </Match>
                                        </AllOf>
                                    </AnyOf>
                                </xsl:if>

                                <!-- Variable type is missing -->
                                <xsl:if test=" $varType='cprovd:d-ref'">
                                    <!-- Get resouce element which are not referenced as variables -->
                                    <AnyOf>
                                        <AllOf>
                                            <Match
                                                MatchId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-d-id-match">
                                                <AttributeValue
                                                  XPathCategory="urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov"
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression">

                                                  <!--  <xsl:attribute name="AttributeId">
                                                            <xsl:text>urn:oasis:names:tc:xacml:3.0:</xsl:text>
                                                            <xsl:value-of select="current()"/>
                                                        </xsl:attribute>
                                                        -->
                                            
                                                  <xsl:if test="$fieldType='cprovd:Action'">
                                                  <xsl:attribute name="XPathCategory">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:action</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:action-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Service'">
                                                  <xsl:attribute name="XPathCategory">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:service</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:service-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Resource'">
                                                  <xsl:attribute name="XPathCategory">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:resource</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:resource-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Subject'">
                                                  <xsl:attribute name="XPathCategory">
                                                  <xsl:text>urn:oasis:names:tc:xacml:1.0:subject-category:access-subject</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:subject-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="not(boolean($fieldType))">
                                                  <xsl:attribute name="XPathCategory">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:cprov-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>
                                                    
                                                    <xsl:value-of select=" child::cprovl:ID"/>
                                                    
                                                </AttributeValue>

                                                <AttributeDesignator MustBePresent="false"
                                                  AttributeId="urn:oasis:names:tc:xacml:1.0:subject:subject-id"
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression">


                                                  <!-- Check to see the type of resource -->
                                                  <xsl:if test="$fieldType='cprovd:Action'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:action</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:action-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Service'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:service</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:service-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Resource'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:resource</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:resource-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Subject'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:1.0:subject-category:access-subject</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:subject-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="not(boolean($fieldType))">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:cprov-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>
                                                  <!-- assing the attribute Id -->

                                                  <!-- assing the attribute Id -->
                                                  <!--
                                                            <xsl:attribute name="AttributeId">
                                                                <xsl:text>urn:oasis:names:tc:xacml:3.0:</xsl:text>
                                                                <xsl:value-of select="current()"/>
                                                            </xsl:attribute>
                                                            -->
                                                </AttributeDesignator>
                                            </Match>
                                        </AllOf>
                                    </AnyOf>
                                </xsl:if>

                                <xsl:if test=" $varType='cprovd:s-ref'">
                                    <!-- Get resouce element which are not referenced as variables -->
                                    <AnyOf>
                                        <AllOf>
                                            <Match
                                                MatchId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-s-id-match">
                                                <AttributeValue
                                                  XPathCategory="urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov"
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression">

                                                  <!--  <xsl:attribute name="AttributeId">
                                                            <xsl:text>urn:oasis:names:tc:xacml:3.0:</xsl:text>
                                                            <xsl:value-of select="current()"/>
                                                        </xsl:attribute>
                                                        -->
                                                  <xsl:value-of select=" child::cprovl:ID"/>

                                                </AttributeValue>

                                                <AttributeDesignator MustBePresent="false"
                                                  AttributeId="urn:oasis:names:tc:xacml:1.0:subject:subject-id"
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression">


                                                  <!-- Check to see the type of resource -->
                                                  <xsl:if test="$fieldType='cprovd:Action'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:action</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:action-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Service'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:service</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:service-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Resource'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:attribute-category:resource</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:resource-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="$fieldType='cprovd:Subject'">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:oasis:names:tc:xacml:1.0:subject-category:access-subject</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:subject-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>

                                                  <xsl:if test="not(boolean($fieldType))">
                                                  <xsl:attribute name="Category">
                                                  <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:cprov</xsl:text>
                                                  </xsl:attribute>

                                                  <xsl:attribute name="AttributeId">
                                                  <xsl:text>urn:oasis:names:tc:xacml:3.0:cprov-id</xsl:text>
                                                  </xsl:attribute>

                                                  </xsl:if>
                                                  <!-- assing the attribute Id -->

                                                  <!-- assing the attribute Id -->
                                                  <!--
                                                            <xsl:attribute name="AttributeId">
                                                                <xsl:text>urn:oasis:names:tc:xacml:3.0:</xsl:text>
                                                                <xsl:value-of select="current()"/>
                                                            </xsl:attribute>
                                                            -->
                                                </AttributeDesignator>
                                            </Match>
                                        </AllOf>
                                    </AnyOf>
                                </xsl:if>

                            </xsl:for-each>
                        </Target>
                    </xsl:if>
                    <!-- Converting cProvl condition to XACML condition -->
                    <xsl:if test="count(cprovl:SUCH-THAT)!=0">
                        <Condition>

                            <!-- <xsl:template match="cprovl:grouping"/> -->

                            <xsl:for-each select="cprovl:SUCH-THAT/cprovl:statements">
                                <!--   <xsl:call-template name="grouping"></xsl:call-template> -->


                                <xsl:choose>
                                    <xsl:when test="count(cprovl:grouping)=0">

                                        <Apply
                                            FunctionId="urn:oasis:names:tc:xacml:1.0:function:and">
                                            <xsl:for-each select="cprovl:statement">
                                                <!-- Check if negate for each statement set to true -->
                                                <xsl:if test="@cprovl:negate='true'">
                                                  <Apply
                                                  FunctionId="urn:oasis:names:tc:xacml:1.0:function:not">

                                                  <Apply
                                                  FunctionId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-node-match">
                                                  <AttributeValue
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
                                                  XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:cprov">
                                                  <!--AttributeId="urn:oasis:names:tc:xacml:3.0:content-selector"-->

                                                  <!--    <xsl:value-of select="(child::*)[position()=1]/name()"/> -->
                                                  <xsl:value-of select="child::node()/@prov:id"/>
                                                  </AttributeValue>

                                                  </Apply>
                                                  </Apply>
                                                </xsl:if>
                                                <!-- Check if the negate is set to false, or the attribute is missing -->
                                                <xsl:if
                                                  test="@cprovl:negate='false' or not(boolean(@cprovl:negate))">
                                                  <Apply
                                                  FunctionId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-node-match">
                                                  <AttributeValue
                                                  DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
                                                  XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:cprov">
                                                  <!--AttributeId="urn:oasis:names:tc:xacml:3.0:content-selector"-->

                                                  <!--    <xsl:value-of select="(child::*)[position()=1]/name()"/> -->
                                                  <xsl:value-of select="child::node()/@prov:id"/>
                                                  </AttributeValue>

                                                  </Apply>
                                                </xsl:if>
                                            </xsl:for-each>
                                        </Apply>
                                    </xsl:when>

                                    <xsl:otherwise>
                                        <xsl:apply-templates select="cprovl:grouping" mode="loop"/>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                        </Condition>
                    </xsl:if>
                </Rule>
            </xsl:for-each>
            <!--
            <Rule RuleId="rule-default" Effect="Deny">
                <Description> : All other requests are denied</Description>        
            </Rule>
            -->
        </Policy>

    </xsl:template>

    <xsl:template name="grouping" match="cprovl:grouping" mode="loop">

        <!--  <xsl:apply-templates select="cprovl:grouping"/>  -->

        <xsl:if test="@cprovl:l_operator='cprovd:And'">
            <Apply FunctionId="urn:oasis:names:tc:xacml:1.0:function:and">

                <xsl:for-each select="cprovl:statement">

                    <Apply
                        FunctionId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-node-match">
                        <AttributeValue
                            DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
                            XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:cprov">
                            <!--AttributeId="urn:oasis:names:tc:xacml:3.0:content-selector"-->
                            <!--    <xsl:value-of select="(child::*)[position()=1]/name()"/> -->
                            <xsl:value-of select="child::node()/@prov:id"/>
                        </AttributeValue>

                    </Apply>

                </xsl:for-each>
                <xsl:for-each select="cprovl:grouping">
                    <xsl:apply-templates select="." mode="loop"/>
                </xsl:for-each>

            </Apply>

        </xsl:if>

        <xsl:if test="@cprovl:l_operator='cprovd:Or'">
            <Apply FunctionId="urn:oasis:names:tc:xacml:1.0:function:or">
                <xsl:for-each select="cprovl:statement">
                    <Apply
                        FunctionId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-node-match">
                        <AttributeValue
                            DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
                            XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:cprov">
                            <!--AttributeId="urn:oasis:names:tc:xacml:3.0:content-selector"-->
                            <!--    <xsl:value-of select="(child::*)[position()=1]/name()"/> -->
                            <xsl:value-of select="child::node()/@prov:id"/>
                        </AttributeValue>
                    </Apply>
                </xsl:for-each>
                <xsl:for-each select="cprovl:grouping">
                    <xsl:apply-templates select="." mode="loop"/>
                </xsl:for-each>

            </Apply>
        </xsl:if>

        <xsl:if test="@cprovl:l_operator='cprovd:Not'">
            <Apply FunctionId="urn:oasis:names:tc:xacml:1.0:function:not">
                <xsl:for-each select="cprovl:statement">
                    <Apply
                        FunctionId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-node-match">
                        <AttributeValue
                            DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
                            XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:cprov">
                            <!--AttributeId="urn:oasis:names:tc:xacml:3.0:content-selector"-->
                            <!--    <xsl:value-of select="(child::*)[position()=1]/name()"/> -->
                            <xsl:value-of select="child::node()/@prov:id"/>
                        </AttributeValue>

                    </Apply>
                </xsl:for-each>
                <xsl:for-each select="cprovl:grouping">
                    <xsl:apply-templates select="." mode="loop"/>
                </xsl:for-each>
            </Apply>
        </xsl:if>

        <xsl:if test="not (boolean(@cprovl:l_operator))">
            <Apply FunctionId="urn:oasis:names:tc:xacml:1.0:function:and">
                <xsl:for-each select="cprovl:statement">
                    <Apply
                        FunctionId="urn:oasis:names:tc:xacml:3.0:function:ext:xpath-provenance-node-match">
                        <AttributeValue
                            DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
                            XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:cprov">
                            <!--AttributeId="urn:oasis:names:tc:xacml:3.0:content-selector"-->
                            <!--    <xsl:value-of select="(child::*)[position()=1]/name()"/> -->
                            <xsl:value-of select="child::node()/@prov:id"/>
                        </AttributeValue>
                    </Apply>
                </xsl:for-each>
                <xsl:for-each select="cprovl:grouping">
                    <xsl:apply-templates select="." mode="loop"/>
                </xsl:for-each>
            </Apply>
        </xsl:if>

    </xsl:template>

</xsl:stylesheet>