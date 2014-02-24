/**
 * ******************************************************************************************
 * Copyright (C) 2014 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.test.desktopfixture;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Neil Pullar 16 May 2011
 */
/// <summary>
/// Singleton class to manage data for the test as well as other relevant global test variables.
/// <para>This class is used internally by the Fixture classes and should not be referenced from FitNesse tests. 
/// It has been marked as excluded to prevent it being added to the Help file.</para>
/// </summary>
/// <remarks>To get a valid instance of TestManager, use the TestManager.GetInstance() method.</remarks>
/// <exclude />
/// <summary>
/// Singleton class so private constructor
//<summary>
// Will create a TestManager if one does not already exist and return it
//</summary>
public class TestManager {
    // Constants
    /// <summary>
    /// Constant for the VALUE action. 
    /// </summary>

    public static final String ACTION_VALUE = "VALUE";
    // Static properties
    //private static volatile TestManager INSTANCE = null;
    // Properties
    /// <summary>
    /// Backing variable for TestData
    /// </summary>
    /// <remarks>
    /// If the test suite will execute a number of concurrent threads during the test
    /// (e.g. Performance Testing), then mark the testData property as [ThreadStatic]. This
    /// will ensure each running thread will have its own set of data rather than sharing the
    /// data across all running threads. ThreadStatic has a minor performance impact, so avoid 
    /// using it unless necessary. 
    /// </remarks>
    //[ThreadStatic] 
    private Map<String, TestDataItem> testData = null;

    public Map<String, TestDataItem> getTestData() {
        if (testData == null){
            testData = new HashMap<String, TestDataItem>(); 
        }
        return testData;
    }
    /// <summary>
    /// Backing variable for TestObjects
    /// </summary>
    /// <remarks>
    /// If the test suite will execute a number of concurrent threads during the test
    /// (e.g. Performance Testing), then mark the testObjects property as [ThreadStatic]. This
    /// will ensure each running thread will have its own set of objects rather than sharing the
    /// objects across all running threads. ThreadStatic has a minor performance impact, so avoid 
    /// using it unless necessary. 
    /// </remarks>
    //[ThreadStatic] 
    private Map<String, Object> testObjects = null;

    /// <summary>
    /// The Map (list) containing the test objects for the test run. 
    /// </summary>
    /// <remarks> A test object can be a handle to a window element or any other object 
    /// that the test must manage from a global perspective. 
    /// </remarks>
    public Map getTestObjects() {

        if (testObjects == null) {
            testObjects = new HashMap<String, Object>();
        }
        return testObjects;

    }

    private TestManager() {
    }

    private static class TestManagerHolder {

        public static final TestManager INSTANCE = new TestManager();
    }

    public static TestManager getInstance() {
        return TestManagerHolder.INSTANCE;
    }
//        <summary>
//        Loads a test data item into the test data dictionary
//        </summary>
//        <param name="dataName">The name of the data item</param>
//        <param name="value">The value for the data item</param>
//        <param name="locked">Flag to indicate if the data item should be locked.</param>
//        <param name="action">Data item action. Default is VALUE.</param>
//        <param name="actionExt">Action extenstion of the data item. Default is null. </param>

    public void loadTestDataItem(String name, String value, boolean locked, String action, String actionExt) {

        TestDataItem fieldData = new TestDataItem();
        name = name.replaceAll(" ", "").toLowerCase();
        fieldData.Name = name; 
        fieldData.Value = value;
        fieldData.Locked = locked;
        fieldData.Action = action == null ? ACTION_VALUE : action.toUpperCase();
        fieldData.ActionExtension = actionExt;

        if (getTestData().containsKey(name)) {
            // Make sure the field is not locked before replacing it
            if (!getTestData().get(name).isLocked()) {
                getTestData().put(name, fieldData);
            }
        } else {
            getTestData().put(name, fieldData);
        }
    }
//    / <summary>
//    / Gets the test data item based on the data name
//    / </summary>
//    / <param name="dataName">The name of the data item</param>
//    / <returns>Data item or null</returns>
    public TestDataItem getTestDataItem(String dataName) {
        dataName = dataName.replace(" ", "").toLowerCase();
        TestDataItem fieldData = null;
        if (getTestData().containsKey(dataName)) {
            fieldData = new TestDataItem(); 
            fieldData.Value = getTestData().get(dataName).getValue();
            fieldData.Action = getTestData().get(dataName).getAction();
            fieldData.ActionExtension = getTestData().get(dataName).getActionExtension();
            if (!getTestData().get(dataName).isLocked()) {
                fieldData.Locked = true;
            } else {
                fieldData.Locked = false;
            }
        }
        return fieldData;
    }
    /// <summary>
    /// Returns the data item value. 
    /// </summary>
    /// <param name="dataName">The name of the data item</param>
    /// <returns>The data item data value or null</returns>
        public String getTestDataItemValue(String dataName)
        {
            TestDataItem fieldData = getTestDataItem(dataName);
            return fieldData == null ? null : fieldData.Value;
        }
    /// <summary>
    /// Clears the Test Data dictionary.
    /// </summary>
        public void clearTestData()
        {
            if (testData != null)
            {
                testData.clear();
                testData = null; 
            }
        }
    /// <summary>
    /// Clears the Test Objects ListDictionary.  
    /// </summary>
        public void clearTestObjects()
        {
            if (testObjects != null)
            {
                testObjects.clear();
                testObjects = null;
            } 
        }
    /// <summary>
    /// Clears all dictionaries managed by the TestManager. Should be called prior to the start of a test. 
    /// </summary>
    public void clear() {
        clearTestData();
        clearTestObjects();
    }
     /// <summary>
    /// Loads a test object so that it can be managed globally for the test. 
    /// </summary>
    /// <typeparam name="<T>"The type of the T object.</typeparam>
    /// <param name="objName">The identifier to use for the T object.</param>
    /// <param name="T">The test object.</param>
      public <T> void loadTestObject(String objName, T obj) {
        if (obj == null) {
            if (getTestObjects().containsKey(objName)) {
                // Remove objects that have been reset to null from the dictionary
                getTestObjects().remove(objName);
            }
        } else {
            if (getTestObjects().containsKey(objName)) {
                getTestObjects().remove(objName);
//                    TestObjects[objName] = obj;
            }
            getTestObjects().put(objName, obj);
//                    TestObjects.Add(objName, obj);

        }
    }
    /// <summary>
    /// Retrieves the named test object. 
    /// If the object is not in the test objects dictionary, the default value
    /// of the generic type is returned (usually null unless the type is non-nullable). 
    /// </summary>
    /// <typeparam name="TObjectType">The type of object to retrieve.</typeparam>
    /// <param name="objName">The name of the object to retrieve. </param>
    /// <returns>The object or the default value for the generic type.</returns>
        public <T> T getTestObject(String objName, Class<T> classType)
        {
            T obj = null;
            if (getTestObjects().containsKey(objName))
            {
                    obj = classType.cast(getTestObjects().get(objName));
            }
            return obj; 
        }        
}
