<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:cprov="http://labs.orange.com/uk/cprov#"
	xmlns:cprovl="http://labs.orange.com/uk/cprovl#" xmlns:prov="http://www.w3.org/ns/prov#"
	xmlns:cprovd="http://labs.orange.com/uk/cprovd#" xmlns:r="http://labs.orange.com/uk/r#"
	xmlns:cu="http://www.w3.org/1999/xhtml/datatypes/" xmlns:ex="http://labs.orange.com/uk/r#"
	xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" exclude-result-prefixes="xs xd"
	version="2.0">
	<xd:doc scope="stylesheet">
		<xd:desc>
			<xd:p>
				<xd:b>Updated on:</xd:b>
				Feb 12, 2013
			</xd:p>
			<xd:p>
				<xd:b>Author:</xd:b>
				Mufajjul Ali
			</xd:p>
			<xd:p />
		</xd:desc>
	</xd:doc>

	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<xsl:template match="/">
		<Request xmlns="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="urn:oasis:names:tc:xacml:3.0:core:schema:wd-17 http://docs.oasis-open.org/xacml/3.0/xacml-core-v3-schema-wd-17.xsd"
			ReturnPolicyIdList="true" CombinedDecision="false">

			<xsl:for-each select="cprovl:PolicyRequest/cprovl:Agent">
				<xsl:variable name="fieldRef">
					<xsl:value-of select=" current()/@isRef" />
				</xsl:variable>
				<xsl:choose>
					<!-- Check if it is reference variable -->
					<xsl:when test="$fieldRef='true'">
						<Attributes
							Category="urn:oasis:names:tc:xacml:1.0:subject-category:access-subject">
							<Attribute IncludeInResult="true"
								AttributeId="urn:oasis:names:tc:xacml:1.0:subject:subject-id">
								<xsl:attribute name="AttributeId">
                                    <xsl:text>urn:oasis:names:tc:xacml:3.0:subject-id</xsl:text>
                                </xsl:attribute>
								<!-- <xsl:for-each select="cprovl:PolicyRequest/cprovl:Agent/cprovl:categoryValue"> -->
								<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
									<xsl:value-of select="@prov:id" />
								</AttributeValue>
							</Attribute>
							<!-- </xsl:for-each> -->
						</Attributes>
					</xsl:when>
					<xsl:otherwise>
						<Attributes
							Category="urn:oasis:names:tc:xacml:1.0:subject-category:access-subject">
							<Attribute IncludeInResult="true"
								AttributeId="urn:oasis:names:tc:xacml:1.0:subject:subject-id">
								<xsl:attribute name="AttributeId">
                                    <xsl:text>urn:oasis:names:tc:xacml:3.0:subject-id</xsl:text>
                                </xsl:attribute>

								<AttributeValue
									DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
									XPathCategory="urn:oasis:names:tc:xacml:1.0:subject-category:access-subject">
									<xsl:value-of select="@prov:id" />
								</AttributeValue>
							</Attribute>
						</Attributes>
					</xsl:otherwise>
				</xsl:choose>

			</xsl:for-each>
			<xsl:for-each select="cprovl:PolicyRequest/cprovl:Entity">
				<xsl:variable name="requestField">
					<xsl:value-of select="cprovl:categoryType/text()" />
				</xsl:variable>

				<xsl:variable name="fieldRef">
					<xsl:value-of select="cprovl:categoryValue/@isRef" />
				</xsl:variable>


				<xsl:if test="$requestField='cprovd:Action'">

					<xsl:choose>
						<!-- Check if it is reference variable -->
						<xsl:when test="$fieldRef='true'">

							<Attributes
								Category="urn:oasis:names:tc:xacml:3.0:attribute-category:action">
								<Attribute IncludeInResult="true"
									AttributeId="urn:oasis:names:tc:xacml:3.0:action-id">
									<xsl:attribute name="AttributeId">
                                        <xsl:text>urn:oasis:names:tc:xacml:3.0:action-id</xsl:text>
                                    </xsl:attribute>
									<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string"
										Category="urn:oasis:names:tc:xacml:3.0:attribute-category:action">
										<xsl:value-of select="cprovl:categoryValue/text()" />
									</AttributeValue>
								</Attribute>
							</Attributes>
						</xsl:when>
						<xsl:otherwise>
							<!-- Set it as a xpath value -->
							<Attributes
								Category="urn:oasis:names:tc:xacml:3.0:attribute-category:action">
								<Attribute IncludeInResult="true"
									AttributeId="urn:oasis:names:tc:xacml:3.0:action-id">
									<xsl:attribute name="AttributeId">
                                        <xsl:text>urn:oasis:names:tc:xacml:3.0:action-id</xsl:text>
                                    </xsl:attribute>
									<AttributeValue
										DataType="urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression"
										XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:action">
										<xsl:value-of select="cprovl:categoryValue/text()" />
									</AttributeValue>
								</Attribute>
							</Attributes>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:if>

				<xsl:if test="$requestField='cprovd:Resource'">

					<xsl:variable name="fieldRef">
						<xsl:value-of select="cprovl:categoryValue/@isRef" />
					</xsl:variable>

					<xsl:choose>
						<!-- Check if it is reference variable -->
						<xsl:when test="$fieldRef='true'">

							<Attributes
								Category="urn:oasis:names:tc:xacml:3.0:attribute-category:resource">
								<Attribute IncludeInResult="true"
									AttributeId="urn:oasis:names:tc:xacml:3.0:resource-id">
									<xsl:attribute name="AttributeId">
                                        <xsl:text>urn:oasis:names:tc:xacml:3.0:resource-id</xsl:text>
                                    </xsl:attribute>
									<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string"
										Category="urn:oasis:names:tc:xacml:3.0:attribute-category:resource">
										<xsl:value-of select="cprovl:categoryValue/text()" />
									</AttributeValue>
								</Attribute>
							</Attributes>

						</xsl:when>
						<xsl:otherwise>

							<Attributes
								Category="urn:oasis:names:tc:xacml:3.0:attribute-category:resource">
								<Attribute IncludeInResult="true"
									AttributeId="urn:oasis:names:tc:xacml:3.0:resource-id">
									<xsl:attribute name="AttributeId">
                                        <xsl:text>urn:oasis:names:tc:xacml:3.0:resource-id</xsl:text>
                                    </xsl:attribute>
									<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string"
										XPathCategory="urn:oasis:names:tc:xacml:3.0:attribute-category:resource">
										<xsl:value-of select="cprovl:categoryValue/text()" />
									</AttributeValue>
								</Attribute>
							</Attributes>

						</xsl:otherwise>

					</xsl:choose>


				</xsl:if>

				<!-- TODO: - Update this with ref and normal category -->
				<xsl:if test="$requestField='cprovd:Service'">
					<Attributes
						Category="urn:com:provenance:cloudprovenance:cprovl:1.0:attribute-category:service">
						<Attribute IncludeInResult="true"
							AttributeId="urn:com:provenance:cloudprovenance:cprovl:1.0:service:">
							<xsl:attribute name="AttributeId">
                                <xsl:text>urn:com:provenance:cloudprovenance:cprovl:1.0:service:service-id</xsl:text>
                            </xsl:attribute>
							<AttributeValue DataType="http://www.w3.org/2001/XMLSchema#string">
								<xsl:value-of select="cprovl:categoryValue/text()" />
							</AttributeValue>
						</Attribute>
					</Attributes>
				</xsl:if>
			</xsl:for-each>

		</Request>
	</xsl:template>
</xsl:stylesheet>