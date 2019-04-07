Traceability Enforcement Cloud Framework
========================================

Traceability Enforcement Cloud Framework (TECF) is designed to support cloud applications or services with provenance capabilities, such as modelling, capturing, encoding and sotrage of provenance data. The framework provides two stacks (server side and Client side) to facilitate ease of provenance integration with services via APIs. It hides all the underlying complex mechanisms of modelling and ecoding & storage of provenance data. On top of that, it provides facilities for enabling provenance-based access control, based on the novel cProvl policy language.

## What is Provenance?

Provenance defines the origin of something. In painting, artifacts such as the year, location, painter and other contextual information are used to determine the value or worthiness of the painting. provenance in software can be used for:

* __Data Quality__ - Check the source of origin to determine the quality of the data
* __Audit trails__ - Check for the satisfaction of necessary SLAs
* __Replication__ - Reproduce an experiment based on the provenance data
* __Attribution__ - Determine various contributors for attributions
* __Lineage__ - Determine the origin of a piece of data based on its lineage

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

## POLICY-ENGINE  
Provenance-aware XACML Policy Engine, based on the extension of the open-source Balana XACML 3.0 engine.

## POLICY-HANDLER-WS 
Web service that exposes a number of APIs for interfacing with the policy engine. 

## SCONVERTER 
Module designed to handle the server side conversion requirements for Java Objects to XML elements and vice-versa.

## CONVERTER 
Module designed to handle the client side conversion of Java Objects to XML elements. 

## CONNECTOR 
Designed to transfer the traceability elements and policy request from the client to the server end.

## SERVICE-API 
APIs to integrate provenance capabilities to a service, also allows a service to construct a policy request.  

## EVENT-HANDLER
Handles multiple generation of a provenance statements (uses JMS)

## TRACEABILITY-MODEL
Represents the traceability model, it defines the schema that presents the traceable nodes, edges, and associated properties.  

## TRACEABILIY-LANGUAGE
Represents the policy language, it defines the schema that provides the structure of a policy, rules and other constraints.

## TRACEABILITY-STORE-WS 
Web service that exposes a number of APIs for interacting with the Traceability Store.

## STORAGE-CONTROLLER
Module design to handle the interaction with the ExistDB 

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
