/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.workassignment.obecdownloader;

/**
 *
 * @author stepaiv3
 * @param <T>
 */
public class ClassProperty {

    static public <T> T createInstance(T templateObject) throws Exception {
        var objectToCreate = templateObject.getClass();
        var defaultConstructor = objectToCreate.getConstructor();
        @SuppressWarnings("unchecked")
        T newInstance = (T) defaultConstructor.newInstance();
        return newInstance;
    }

    static public <T> void setFromString(T objectToModify, String fieldName, String fieldValue) {
        try {
            var objectToModifyClass = objectToModify.getClass();
            var objectToModifyField = objectToModifyClass.getField(fieldName);

            var objectToModifyFieldType = objectToModifyField.getType();

            if (objectToModifyFieldType == String.class) {
                objectToModifyField.set(objectToModify, fieldValue);
            } else if (objectToModifyFieldType == Integer.class) {
                Integer fieldValueInt = Integer.parseInt(fieldValue);
                objectToModifyField.set(objectToModify, fieldValueInt);
            }
        } catch (Exception e) {
            System.out.println("Unable to set object via string field from string value.");
            e.printStackTrace();
        }
    }

    static public <T, U> void set(T objectToModify, String fieldName, U fieldValue) {
        try {
            var objectToModifyClass = objectToModify.getClass();
            var objectToModifyField = objectToModifyClass.getField(fieldName);

            var objectToModifyFieldType = objectToModifyField.getType();
            var fieldValueType = fieldValue.getClass();

            if (fieldValueType == objectToModifyFieldType) {
                objectToModifyField.set(objectToModify, fieldValue);
            }
        } catch (Exception e) {
            System.out.println("Unable to set object via string field.");
            e.printStackTrace();
        }
    }
}
