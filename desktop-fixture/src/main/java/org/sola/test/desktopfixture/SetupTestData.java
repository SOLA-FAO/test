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

///**
// * Exposes methods that can be used to load data items for the test.
// * <p> The FieldValue... methods should be used when setting up data in a FitNesse Setup page.</p>
// * <p>The LockFieldValue... methods should be used when specifying data from a test page. The Lock methods
// * ensure the test data cannot be overridden if the same data item is used by a test script 
// * executed as part of the current tests setup steps.</p>
// * <p>A test data item contains a number of attributes. These attributes include:
// * <list type="bullet">
// * <item>Name - The name of the test data item. Typically of the form PageName:FieldName  
// * e.g. <c>SetupPage:UserId</c><para>FitNesse assumes values in camel case are references to new wiki pages. 
// * If FitNesse is being used for testing, spaces can be used in the Name 
//     to avoid automatic page creation e.g. <c>|Field|Setup Page: User Id|Value|usr001|</c>. Any spaces in the Name will be removed when the data item 
//     is added to the test data collection.</para></item>
//    <item>Value - The value to use for the test data item. All values are treated as string by the test harness.</item>
//     <item>Action - An optional value that can be used to control some functional aspect of the
//    of the application being tested. <para>For example an Action of FIRST could indicate that the first value in a drop down combo box should 
//     be selected regardless of the actual data value. TODAY could be used to set the field to todays date, etc.</para><para>Note that the test harness must have the necessary functionailty to recognise and
//     act on an action.</para></item>
//     <item>ActionExtension - Provides additional data that may be relevant to the Action. <para>For example an Action of SELECTITEM with an ActionExtension of 5 could mean select the 5th item
//     in the drop down combo box. An Action of ADDDAYS with ActionExtension 5 could mean add 5 days to the current date, etc.</para></item>
//     <item>Locked - Indicates that the data item is locked in for the test and cannot be overriden by any other test script.
//   <para>This is relevant when a test needs to execute other test scripts as part of its setup. The other test
//     scripts may involve window navigation and data entry and setup their own test data accordingly. To ensure
//     the data required by the current test script is correctly setup, it is possible to lock the necessary value 
//     in the test being executed so it is not replaced by another test script.</para></item>
//     </list>
//     </para>
// * @author Neil Pullar 16 May 2011
// */

//    / </remarks>
//    / <example>Example SetUp page in Fitnesse:
//    / <code>
//    / !|import|
//    / |IAG.Polisy.Test.AxisTestHarness|
//    / |IAG.Polisy.Test.Utilities|
//    / 
//    / |script|Setup Test Data                                                            |
//    / |Field |Utility: Start Up Script Location|Value|C:\\apps\\axis\\polisyw\\Polisy.cmd|
//    / |Field |Utility: Default Wait Time       |Value|500                                |
//    / |Field |Startup: Security Warning        |Value|true                               |
//    / |Field |Startup: Read Only Warning       |Value|true                               |
//    / |Field |Setup Page: Connection Method    |Value|C DLL                              |
//    / |Field |Setup Page: Password             |Value|dv1_usr00001                       |
//    / |Field |Setup Page: Server               |Value|NZAX1D1                            |
//    / |Field |Setup Page: Title                |Value|2\.54.*_Setup                      |
//    / |Field |Setup Page: User Id              |Value|dv1_usr00001                       |
//    / |Field |Master Menu: Your Choice         |Value|UNDWRITE                           |</code>
//    / <para>
//    / Example Test page in FitNesse using Lock Field and Lock Field Action:
//    / <code>
//    / |script    |Setup Test Data                        |
//    / |Lock Field|Master Menu: Your Choice|Value|UNDWRITE|
//    / |Lock Field|Policy Brands: Date|Value||Action|TODAY|</code>
//    / </para></example>
public class SetupTestData {

    private TestManager testMan = TestManager.getInstance();
    /// <summary>
    ///  Loads a value and action for a test data item. 
    /// </summary>
    /// <param name="field">The name to use for the data item.</param>
    /// <param name="value">The value to set for the data item.</param>
    /// <param name="action">The action to set for the data item.</param>
    /// <example>Fitnesse Usage: <c>|Field|Setup Page: Password|value|letmein|action|NOW|</c></example>
    /// <remarks>Data values added using this method can be replaced (i.e. they are not locked).
    /// This method should be used when setting data in a FitNesse Setup page.</remarks>

    public boolean fieldValue(String field, String value) {
        testMan.loadTestDataItem(field, value, false, "", "");
        return true; 
    }

    public boolean fieldValueAction(String field, String value, String action) {
        testMan.loadTestDataItem(field, value, false, action, "");
        return true; 
    }

    /// <summary>
    /// Loads a value with an action and an action extenstion for a test data item.
    /// </summary>
    /// <param name="field">The name to use for the data item.</param>
    /// <param name="value">The value to set for the data item.</param>
    /// <param name="action">The action to set for the data item.</param>
    /// <param name="actionExt">The action extension to set for the data item</param>
    /// <example>Fitnesse Usage: <c>|Field|Setup Page: Password|value|letmein|action|WAIT|Extension|5|</c></example>
    /// <remarks>Data values added using this method can be replaced (i.e. they are not locked).
    /// This method should be used when setting data in a FitNesse Setup page.</remarks>
    public boolean fieldValueActionExtension(String field, String value, String action, String actionExt) {
        testMan.loadTestDataItem(field, value, false, action, actionExt);
        return true; 
    }

    /// <summary>
    /// Loads a value for a test data item and locks the data item so it can't be overridden. 
    /// </summary>
    /// <param name="field">The name to use for the data item.</param>
    /// <param name="value">The value to set for the data item.</param>
    /// <example>Fitnesse Usage: <c>|Lock field|Setup Page: Password|value|letmein|</c></example>
    /// <remarks>Data values added using this method cannot be replaced (i.e. they are locked).
    /// This method should be used when setting data in a FitNesse Test page.</remarks>
    public boolean lockFieldValue(String field, String value) {
        testMan.loadTestDataItem(field, value, true, "", "");
       return true; 
    }

    /// <summary>
    /// Loads a value and action for a test data item and locks the data item so it can't be overridden. 
    /// </summary>
    /// <param name="field">The name to use for the data item.</param>
    /// <param name="value">The value to set for the data item.</param>
    /// <param name="action">The action to set for the data item.</param>
    /// <example>Fitnesse Usage: <c>|Lock field|Setup Page: Password|value|letmein|action|NOW|</c></example>
    /// <remarks>Data values added using this method cannot be replaced (i.e. they are locked).
    /// This method should be used when setting data in a FitNesse Test page.</remarks>
    public boolean lockFieldValueAction(String field, String value, String action) {
        testMan.loadTestDataItem(field, value, true, action, "");
        return true; 
    }

    /// <summary>
    /// Loads a value with an action and an action extenstion for a test data item 
    /// and locks the data item so it can't be overridden.
    /// </summary>
    /// <param name="field">The name to use for the data item.</param>
    /// <param name="value">The value to set for the data item.</param>
    /// <param name="action">The action to set for the data item.</param>
    /// <param name="actionExt">The action extension to set for the data item</param>
    /// <example>Fitnesse Usage: <c>|Lock field|Setup Page: Password|value|letmein|action|WAIT|Extension|5|</c></example>    
    /// <remarks>Data values added using this method cannot be replaced (i.e. they are locked).
    /// This method should be used when setting data in a FitNesse Test page.</remarks>
    public boolean lockFieldValueActionExtension(String field, String value, String action, String actionExt) {
        testMan.loadTestDataItem(field, value, true, action, actionExt);
        return true; 
    }
}
