package com.mycompany.xml.json.converter;

import org.json.JSONObject;
import org.json.XML;

import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonToXmlAPI {

    public static String convertJsonFileToXml(String jsonPath) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonPath))).trim();
            return convertJsonStringToXml(jsonContent);
        } catch (Exception e) {
            return "❌ Erreur lors de la lecture du fichier JSON : " + e.getMessage();
        }
    }

    public static String convertJsonStringToXml(String jsonContent) {
        try {
            if (jsonContent.startsWith("\uFEFF")) {
                jsonContent = jsonContent.substring(1);
            }

            if (!jsonContent.startsWith("{")) {
                return "❌ Le texte JSON doit commencer par '{'.";
            }

            JSONObject jsonObject = new JSONObject(jsonContent);
            String xmlBody = XML.toString(jsonObject);

            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n" + xmlBody + "\n</root>";
        } catch (org.json.JSONException je) {
            return "❌ JSON invalide ou doublons de clés : " + je.getMessage();
        } catch (Exception e) {
            return "❌ Erreur ";
                    }
    }
}