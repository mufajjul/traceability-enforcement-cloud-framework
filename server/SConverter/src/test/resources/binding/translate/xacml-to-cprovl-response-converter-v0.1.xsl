<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xpath-default-namespace = "urn:oasis:names:tc:xacml:3.0:core:schema:wd-17"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:cprov="http://labs.orange.com/uk/cprov#"
        xmlns:cprovl="http://labs.orange.com/uk/cprovl#" xmlns:prov="http://www.w3.org/ns/prov#"
        xmlns:cprovd="http://labs.orange.com/uk/cprovd#" xmlns:r="http://labs.orange.com/uk/r#"
        xmlns:cu="http://www.w3.org/1999/xhtml/datatypes/" xmlns:ex="http://labs.orange.com/uk/r#"
        xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" exclude-result-prefixes="xs xd" version="2.0">
        <xd:doc scope="stylesheet">
            <xd:desc>
                <xd:p><xd:b>Updated on:</xd:b> Feb 12, 2013</xd:p>
                <xd:p><xd:b>Author:</xd:b> Mufajjul Ali </xd:p>
                <xd:p/>
            </xd:desc>
        </xd:doc>
        
        <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
        
        <xsl:template match="/">
            <cprovl:policyResponse xmlns:prov="http://www.w3.org/ns/prov#"
                xmlns:cprov="http://labs.orange.com/uk/cprov#"
                xmlns:opmx="http://openprovenance.org/model/opmx#"
                xmlns:cprovd="http://labs.orange.com/uk/cprovd#"
                xmlns:cu="http://www.w3.org/1999/xhtml/datatypes/"
                xmlns:cprovl="http://labs.orange.com/uk/cprovl#"
                xmlns:r="http://labs.orange.com/uk/r#"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://labs.orange.com/uk/cprovl# file:/Users/mufy/Dropbox/EngD/year3/schema/cProvl/cProvl-v1.1.xsd">
                
                <!-- request Id -->
                <cprovl:Agent/>
                
                <!-- decision -->
                
                <xsl:for-each select="Response/Result">
                    <cprovl:Entity>
                        <cprovl:service>cprovl:confidenshare</cprovl:service>
                        <cprovl:decision><xsl:value-of select="Decision"/></cprovl:decision>
                        <cprovl:description><xsl:value-of select="Status/StatusCode/@Value"/></cprovl:description>
                    </cprovl:Entity>
                </xsl:for-each>
                
                <!-- processes for target and conditional statements -->
                 <cprov:cProcess/>                 
                <cprov:transition/>               
                <!-- edges -->
                <cprov:hadOwnership/>                                 
                <cprov:wasReferenceOf/>
                <cprov:wasImplicitlyCalledBy/>
                <cprov:wasImplicitlyCalledBy/>
                <cprov:wasVirtualizedBy/>
                <cprov:wasVirtualizedBy/>
                <cprov:wasInitiallyCalledBy/>
                <prov:used/>
                <cprov:hadTransitionState_c/>
                       
            </cprovl:policyResponse>
            
        </xsl:template>
    
</xsl:stylesheet>



