package com.mycompany.xml.json.converter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.io.ByteArrayInputStream;

public class XmlToJson {

    // Conversion depuis un fichier XML
    public static String convertXmlFileToJson(String filePath) {
        try {
            // Charger le fichier XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filePath);

            // Récupérer la racine
            Element root = doc.getDocumentElement();

            // Appeler la fonction récursive
            return elementToJson(root);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // JSON vide en cas d’erreur
        }
    }

    // ✅ Conversion directe depuis une chaîne XML (zone de texte)
    public static String convertXmlStringToJson(String xmlContent) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Charger le XML depuis une chaîne
            Document doc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

            // Récupérer la racine
            Element root = doc.getDocumentElement();

            // Appeler la fonction récursive
            return elementToJson(root);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // JSON vide en cas d’erreur
        }
    }

    // Fonction récursive qui transforme un élément XML en JSON
    private static String elementToJson(Element element) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        NodeList children = element.getChildNodes();
        boolean first = true;

        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node instanceof Element) {
                if (!first) sb.append(",");
                sb.append("\"").append(node.getNodeName()).append("\":");

                // Si l’enfant a lui-même des sous-éléments → récursion
                if (hasElementChild(node)) {
                    sb.append(elementToJson((Element) node));
                } else {
                    sb.append("\"").append(node.getTextContent().trim()).append("\"");
                }
                first = false;
            }
        }
        sb.append("}");
        return sb.toString();
    }

    // Vérifie si un nœud contient des sous-éléments
    private static boolean hasElementChild(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) return true;
        }
        return false;
    }
}
