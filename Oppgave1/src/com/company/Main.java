package com.company;

import org.w3c.dom.NodeList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.xml.soap.*;


public class Main {



    public static void main(String[] args) throws IOException {

        // Linker til dataaccess
        String soapEndpointUrl = "https://www.dataaccess.com/webservicesserver/TextCasing.wso";
        String soapAction = "https://www.dataaccess.com/webservicesserver/TextCasing.wso";

        // Samling av argument variabler
        String arg1 = "";
        if (args.length > 0) {
            for (String s: args) {
                arg1 += s + " ";
            }
        }
        else {
            System.out.println("Usage: input text you want to convert");
            System.exit(0);
        }

        callSoapWebService(soapEndpointUrl, soapAction, arg1);

    }
    private static void createSoapEnvelope(SOAPMessage soapMessage, String arg) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "myNamespace";
        String myNamespaceURI = "http://www.dataaccess.com/webservicesserver/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
         SOAPElement soapBodyElem = soapBody.addChildElement("InvertStringCase", myNamespace);
         SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("sAString", myNamespace);
        soapBodyElem1.addTextNode(arg);
     }

    private static void callSoapWebService(String soapEndpointUrl, String soapAction, String arg) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Start av tidtaking
            long startTime = System.currentTimeMillis();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, arg), soapEndpointUrl);
            SOAPBody sBody = soapResponse.getSOAPBody();
            NodeList test = sBody.getElementsByTagName("m:InvertStringCaseResult");

            // Konvertert tekst
            System.out.println(test.item(0).getFirstChild().getTextContent());
            System.out.println();

            // Slutt av tidtaking
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(elapsedTime + " milliseconds");

            soapConnection.close();

        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }
    private static SOAPMessage createSOAPRequest(String soapAction, String arg) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage, arg);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);
        soapMessage.saveChanges();

        return soapMessage;
    }

}
