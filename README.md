Traceability Enforcement Cloud Framework
========================================

Welcome to the Traceability Enforcement Cloud Framework. This framework is designed to aid the development of provenance-aware cloud services with enforcement capabilities. 
It provides necessary APIs for a service to enable capturing of its traceability data, processing capabilities, and assertions on the traceability data via the cProvl policy language.  

Key Features
============

* cProv Traceability Model 
* cProvl Policy Language
* XACML 3.0 engine support for provenance/traceability data 
* cProvl to XACML 3.0 policy translation
* Dynamic generation of cProvl authorisation request
* cProvl request to XACML request translation
* XACML single to cProvl multi-value mapping
* Static and dynamic variable holding 
* XML-DB based traceability & policy store

MODULES
==============================================

POLICY-ENGINE -  Provenance-aware XACML Policy Engine, based on the extension of the open-source Balana XACML 3.0 engine.
POLICY-HANDLER-WS - Web service that exposes a number of APIs for interfacing with the policy engine. 
SCONVERTER - Module designed to handle the server side conversion requirements for Java Objects to XML elements and vice-versa.
CONVERTER - Module designed to handle the client side conversion of Java Objects to XML elements. 
CONNECTOR - Designed to transfer the traceability elements and policy request from the client to the server end.
SERVICE-API - APIs to integrate provenance capabilities to a service, also allows a service to construct a policy request.  
EVENT-HANDLER - Handles multiple generation of a provenance statements (uses JMS)
TRACEABILITY-MODEL - Represents the traceability model, it defines the schema that presents the traceable nodes, edges, and associated properties.  
TRACEABILIY-LANGUAGE - Represents the policy language, it defines the schema that provides the structure of a policy, rules and other constraints.
TRACEABILITY-STORE-WS - Web service that exposes a number of APIs for interacting with the Traceability Store.
STORAGE-CONTROLLER - Module design to handle the interaction with the ExistDB 

System Requirements
===================
1. Minimum memory - 1 GB
2. Processor      - Pentium 800MHz or equivalent at minimum
3. JRE 1.6 or higher
4. EXIST-XML DB
5. TOMCAT 7.0

Installation and Running
========================
TODO

Known Issues
============
TODO

Issues Fixed In This Release
============================
TODO