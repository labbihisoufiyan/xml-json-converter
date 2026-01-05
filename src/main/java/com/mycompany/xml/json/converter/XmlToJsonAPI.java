package com.mycompany.xml.json.converter;

import org.json.JSONObject;
import org.json.XML;

import java.nio.file.Files;
import java.nio.file.Paths;

public class XmlToJsonAPI {

    // Conversion depuis un fichier XML
    public static String convertXmlFileToJson(String xmlPath) {
        try {
            // Lire le contenu du fichier XML
            String xmlContent = new String(Files.readAllBytes(Paths.get(xmlPath))).trim();

            // Nettoyer les caractères invisibles (BOM)
            if (xmlContent.startsWith("\uFEFF")) {
                xmlContent = xmlContent.substring(1);
            }

            // Convertir XML → JSON avec org.json
            JSONObject jsonObject = XML.toJSONObject(xmlContent);

            // Retourner le JSON formaté
            return jsonObject.toString(4); // indentation 4 espaces
        } catch (Exception e) {
            return "❌ Erreur lors de la conversion XML → JSON avec API : " + e.getMessage();
        }
    }

    // Conversion directe depuis une chaîne XML (zone de texte)
    public static String convertXmlStringToJson(String xmlContent) {
        try {
            // Nettoyer les caractères invisibles (BOM)
            if (xmlContent.startsWith("\uFEFF")) {
                xmlContent = xmlContent.substring(1);
            }

            // Convertir XML → JSON avec org.json
            JSONObject jsonObject = XML.toJSONObject(xmlContent);

            // Retourner le JSON formaté
            return jsonObject.toString(4); // indentation 4 espaces
        } catch (Exception e) {
            return "❌ Erreur lors de la conversion XML → JSON avec API : " + e.getMessage();
        }
    }
}
