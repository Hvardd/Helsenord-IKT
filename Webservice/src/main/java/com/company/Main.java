package com.company;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Patient;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        // Testing: http://localhost:4567/patient/:id   f.eks http://localhost:4567/patient/example

        // starter localhost med get route
          get("/patient/:id", (req, res) -> method(req.params(":id")));

          System.out.println(method("003b89e6-c7df-459a-83db-3a28db042c71"));

     }

    public static String method(String id) {

        // Opprettelse av FIHR
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR4");
        Patient patient;

        // Sjekker om id eksisterer
        try {
            patient = client.read().resource(Patient.class).withId(id).execute();
        } catch (ResourceNotFoundException e) {
            return "ID ikke funnet";
        }

        // Henter fornavn og etternavn og legger dem i variabler
        String fornavn = patient.getName().get(0).getGiven().toString();
        String etternavn = patient.getName().get(0).getFamily().toString();

        String fødselsdato;

        //  Sjekker om fødselsdato eksisterer hvis den gjør så settes den inn i variabel
        try {
            fødselsdato = patient.getBirthDate().toString();
        } catch (NullPointerException exception) {
            fødselsdato = "";
        }

        String string = fornavn + "\n" + etternavn + "\n" + fødselsdato;

        return string;
    }





}