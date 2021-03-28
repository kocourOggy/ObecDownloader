/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.workassignment.obecdownloader;

import java.util.Map;

/**
 *
 * @author stepaiv3
 */
public final class XMLMapper <T> {
    public Map<String, String> XMLFieldsToObjectFields;
    public String nameOfXMLClass;
    
    public XMLMapper(String nameOfXML, Map<String, String> fieldsXMLToObjectFields)
    {
        this.nameOfXMLClass = nameOfXML;
        this.XMLFieldsToObjectFields = fieldsXMLToObjectFields;
    }
    
    public T createObjectFromStringValues(T templateObject, Map<String, String> fieldsXMLToStringValues) throws Exception {
        var objectToCreate = ClassProperty.createInstance(templateObject);
        
        for (var entry1 : fieldsXMLToStringValues.entrySet()) {
            String xmlField = entry1.getKey();
            String xmlValue = entry1.getValue();            
            String classField = this.XMLFieldsToObjectFields.get(xmlField);
            
            if (classField != null){
                ClassProperty.setFromString(objectToCreate, classField, xmlValue);
            }
        }
        return objectToCreate;
    }
}
