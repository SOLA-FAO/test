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

/**
 *
 * @author Neil Pullar 16 May 2011
 */
    /// <summary>
    /// Contains information for one data item to be used during the test. 
    /// <para>This class is used internally by the Fixture classes and should not be referenced from FitNesse tests. 
    /// It has been marked as excluded to prevent it being added to the Help file.</para>
    /// </summary>
    /// <exclude />
public class TestDataItem {
 
    public String Name;
        /// <summary>
        /// The name of the test data item. Typically of the form PageName:FieldName.
        /// </summary>
        /// <example>SetupPage:UserId, SetupPage:Password</example> 
        /// <remarks>
        /// FitNesse assumes values in camel case are references to new wiki pages. 
        /// If FitNesse is being used for testing, spaces can be used in the Name 
        /// to avoid automatic page creation. e.g. Setup Page: User Id. 
        /// </remarks>

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
  
    public String Value;
      /// <summary>
        /// The value to use for the test data item. 
        /// </summary>
    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }
    public String Action;
        /// <summary>
        /// An optional value that can be used to control some functional aspect of the
        /// of the application being tested. 
        /// </summary>
        /// <example>An Action of FIRST could indicate that the first value in a drop down combo box should 
        /// be selected regardless of the actual data value. </example>
        /// <remarks>
        /// The test harness must have the necessary functionailty to recognise and
        /// act on an action.  
        /// </remarks>

    public String getAction() {
        return Action;
    }

    public void setAction(String Action) {
        this.Action = Action;
    }
   
    public String ActionExtension;
       /// <summary>
        /// Provides additional data that may be relevant to the Action.
        /// </summary>
        /// <example> An Action of SELECTITEM with an ActionExtension of 5 could mean select the 5th item
        /// in the drop down combo box.</example> 
    public void setActionExtension(String ActionExtension) {
        this.ActionExtension = ActionExtension;
    }

    public String getActionExtension() {
        return ActionExtension;
    }
  public boolean Locked;
        /// <summary>
        /// Indicates that the data item is locked in for the test and cannot be overriden by any other test script.
        /// </summary>
        /// <remarks>
        /// This is relevant when a test needs to execute other test scripts as part of its setup. The other test
        /// scripts may involve screen navigation and data entry and setup their own test data accordingly. To ensure
        /// the data required by the current test script is correctly setup, it is possible to lock the necessary value 
        /// in the test being executed so it is not replaced by another test script. 
        /// </remarks> 

    public boolean isLocked() {
        return Locked;
    }

    public void setLocked(boolean Locked) {
        this.Locked = Locked;
    }


}