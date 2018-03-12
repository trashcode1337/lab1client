package com.drrufus.lab1client.soap;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Utils {
    
    private static transient Logger logger = LoggerFactory.getLogger(Utils.class);
    
    private static void createSoapEnvelope(SOAPMessage soapMessage, Map<String, String> params) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "gs";
        String myNamespaceURI = "http://drrufus.com/autogenerated";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("getUserRequest", myNamespace);
        
        for (Map.Entry<String, String> entry : params.entrySet()) {
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement(entry.getKey(), myNamespace);
            soapBodyElem1.addTextNode(entry.getValue());
        }
    }

    public static SOAPMessage callSoapWebService(String soapEndpointUrl, String soapAction, Map<String, String> params) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, params), soapEndpointUrl);

            logger.info("Response SOAP Message:\n" + SOAPtoString(soapResponse));

            soapConnection.close();
            return soapResponse;
        } catch (Exception e) {
            logger.error("Error occurred while sending SOAP Request to Server!"
                    + "Make sure you have the correct endpoint URL and SOAPAction!"
                    + "\n{}", e.getMessage());
            return null;
        }
    }

    private static SOAPMessage createSOAPRequest(String soapAction, Map<String, String> params) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, params);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        logger.info("Request SOAP Message:\n" + SOAPtoString(soapMessage));

        return soapMessage;
    }
    
    private static String SOAPtoString(SOAPMessage message) {
        ByteArrayOutputStream baos = null;
        try 
        {
            baos = new ByteArrayOutputStream();
            message.writeTo(baos); 
            return baos.toString();
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
