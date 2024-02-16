package com.servico.prueba.constants;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataInput;

public class JsonPersona {

    public String writeValueAsString(Persona requestBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Persona person = objectMapper.readValue((DataInput) requestBody, Persona.class);

            String newPersona = person.toString();
            System.out.println("Person details: " + newPersona);
            return newPersona;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
