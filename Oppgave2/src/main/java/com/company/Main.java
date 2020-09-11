package com.company;

//import ca.uhn.fhir.context.FhirContext;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;

public class Main {

    public static void main(String[] args) {

        // Variabel for argument
        String arg1 = "";

            if (args.length == 1) {
                arg1 += args[0];
            }
            else {
                System.out.println("Usage: input id, 1 argument");
                System.exit(0);
            }

            // Opprettelse av FIHR
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR4");
        Patient patient;

        try {
            // test id "example" "1214524" "003b89e6-c7df-459a-83db-3a28db042c71" "952975"
            patient = client.read().resource(Patient.class).withId(arg1).execute();
        } catch (ResourceNotFoundException e) {
            System.out.println("Resource not found!");
            return;
        }

        // Samling av string variabler
        String name = patient.getName().get(0).getGiven().toString();
        String familyName = patient.getName().get(0).getFamily();
        String gender = patient.getGender().toString();
        String birthdate;

        // Printing av string variabler
        System.out.println("Fornavn: " + name);
        System.out.println("Etternavn: " + familyName);
        System.out.println("Kjønn: " + gender.substring(0,1));

        // Sjekker om fødelsdato eksisterer, hvis den gjør så legges den i en variabel
        try {
            birthdate = patient.getBirthDate().toString();
            System.out.println("Fødselsdato: " + birthdate);

        } catch (NullPointerException exception) {
            System.out.println("Ingen fødselsdato funnet");
        }
    }
}
