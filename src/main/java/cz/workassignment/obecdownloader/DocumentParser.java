/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.workassignment.obecdownloader;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author stepaiv3
 */
public class DocumentParser {

    static Document createDocument(Path pathToFile) {
        Document doc;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(pathToFile.toFile());
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
            doc = null;
        }
        return doc;
    }

    static <T> ArrayList<T> parseObjects(Document doc, T templateObject, XMLMapper<T> xmlMapper) {
        var listOfObjects = new ArrayList<T>();
        try {
            var listOfObjectsXML = doc.getElementsByTagName(xmlMapper.nameOfXMLClass);

            for (int i = 0; i < listOfObjectsXML.getLength(); ++i) {
                Element objectXML = (Element) listOfObjectsXML.item(i);
                var fieldsXMLToStringValues = new HashMap<String, String>();

                for (var entry : xmlMapper.XMLFieldsToObjectFields.entrySet()) {
                    String xmlField = entry.getKey();

                    var elementXMLField = objectXML.getElementsByTagName(xmlField).item(0);
                    String elementXMLFieldValue = elementXMLField.getTextContent();
                    fieldsXMLToStringValues.put(xmlField, elementXMLFieldValue);
                }
                var parsedObject = xmlMapper.createObjectFromStringValues(templateObject, fieldsXMLToStringValues);
                listOfObjects.add(parsedObject);
            }
        } catch (Exception e) {
            System.out.println("Unable to parse objects succesfully.");
            e.printStackTrace();
        }
        return listOfObjects;
    }
}
