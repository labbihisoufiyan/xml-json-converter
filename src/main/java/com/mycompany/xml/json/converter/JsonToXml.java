package com.mycompany.xml.json.converter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonToXml {

    // Conversion depuis un fichier JSON vers un fichier XML
    public static boolean convertJsonFileToXml(String jsonPath, String xmlOutputPath) {
        try {
            // Lire le contenu du fichier JSON
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonPath))).trim();

            // Nettoyer les caractères invisibles (BOM)
            if (jsonContent.startsWith("\uFEFF")) {
                jsonContent = jsonContent.substring(1);
            }

            // Vérifier que le JSON commence par '{'
            if (!jsonContent.startsWith("{")) {
                System.err.println("❌ Le fichier JSON doit commencer par '{'.");
                return false;
            }

            // Supprimer les accolades extérieures
            jsonContent = jsonContent.substring(1, jsonContent.length() - 1).trim();

            // Créer le document XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Élément racine
            Element root = doc.createElement("root");
            doc.appendChild(root);

            // Séparer les paires clé:valeur
            String[] pairs = jsonContent.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
                    String value = keyValue[1].trim().replaceAll("^\"|\"$", "");
                    Element child = doc.createElement(key);
                    child.setTextContent(value);
                    root.appendChild(child);
                }
            }

            // Convertir le document XML en texte
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xmlBuilder.append("<root>\n");
            for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                Element el = (Element) root.getChildNodes().item(i);
                xmlBuilder.append("  <").append(el.getTagName()).append(">")
                          .append(el.getTextContent())
                          .append("</").append(el.getTagName()).append(">\n");
            }
            xmlBuilder.append("</root>");

            // Créer le dossier si nécessaire
            File xmlFile = new File(xmlOutputPath);
            File parentDir = xmlFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Écrire dans le fichier XML
            try (FileWriter writer = new FileWriter(xmlFile)) {
                writer.write(xmlBuilder.toString());
            }

            System.out.println("✅ Fichier XML généré : " + xmlOutputPath);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la conversion : " + e.getMessage());
            return false;
        }
    }

    // ✅ Conversion directe depuis une chaîne JSON (zone de texte)
    public static String convertJsonStringToXml(String jsonContent) {
        try {
            // Nettoyer BOM
            if (jsonContent.startsWith("\uFEFF")) {
                jsonContent = jsonContent.substring(1);
            }

            if (!jsonContent.startsWith("{")) {
                return "❌ Le texte JSON doit commencer par '{'.";
            }

            // Supprimer les accolades extérieures
            jsonContent = jsonContent.substring(1, jsonContent.length() - 1).trim();

            // Construire XML manuellement
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xmlBuilder.append("<root>\n");

            String[] pairs = jsonContent.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
                    String value = keyValue[1].trim().replaceAll("^\"|\"$", "");
                    xmlBuilder.append("  <").append(key).append(">")
                              .append(value)
                              .append("</").append(key).append(">\n");
                }
            }

            xmlBuilder.append("</root>");
            return xmlBuilder.toString();

        } catch (Exception e) {
            return "❌ Erreur lors de la conversion JSON → XML : " + e.getMessage();
        }
    }
}
