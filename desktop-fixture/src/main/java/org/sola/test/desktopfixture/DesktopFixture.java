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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.sola.clients.desktop.DesktopApplication;
import org.sola.common.DateUtility;
import org.sola.common.messaging.LocalizedMessage;
import org.sola.common.messaging.MessageResponder;
import org.sola.common.messaging.MessageUtility;
import org.uispec4j.Button;
import org.uispec4j.CheckBox;
import org.uispec4j.ComboBox;
import org.uispec4j.Table;
import org.uispec4j.ItemNotFoundException;
import org.uispec4j.ListBox;
import org.uispec4j.MenuItem;
import org.uispec4j.TabGroup;
import org.uispec4j.TextBox;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

/**
 * This class contains the FitNesse Fixture methods that can be used to control the SOLA Desktop GUI.
 * @author Neil Pullar
 */
public class DesktopFixture {

    private final Window mainWin[] = new Window[1];

    /** 
     * Constructor. Configures dialog suppression on the 
     * {@linkplain org.sola.common.messaging.MessageUtility} using the {@linkplain MessageResponder}. 
     */
    public DesktopFixture() {
        MessageUtility.suppressDialog(new FixtureMessageResponder());
    }

    /** 
     * Required by UISpec4J to ensure the UISpec toolkit is initialized before any other
     * swing toolkit component.
     */
    static {
        System.setProperty("uispec4j.test.library", "junit");
        UISpec4J.init();
    }
public void log(String message) {
        try {
            if (message != null) {
                FileWriter out = new FileWriter("fixture.log", true);
                BufferedWriter writer = new BufferedWriter(out);
                writer.write(Calendar.getInstance().getTime().toString() + ":  " + message);
                writer.newLine();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TestManager getTestMan() {
        return TestManager.getInstance();
    }

    /**
     * Flag that indicates if the test has been aborted due to an exception. Once a test is aborted
     * all calls to subsequent fixture methods within the test will exit immediately and returning
     * the the default value. 
     * @return 
     */
    public boolean isAbortTest() {
        Boolean abort = getTestMan().getTestObject("SOLA.AbortTest", Boolean.class);
        abort = abort == null ? Boolean.FALSE : abort;

        return abort;
    }

    private void setAbortTest(Boolean abortTest) {
        getTestMan().loadTestObject("SOLA.AbortTest", abortTest);
    }

    private void setCurrentWindow(Window cw) {
        getTestMan().loadTestObject("SOLA.CurrentWindow", cw);


    }

    private Window getCurrentWindow() {
        return getTestMan().getTestObject("SOLA.CurrentWindow", Window.class);
    }

    /**
     * Maintains a handle to the Dashboard window throughout the test so that menu items can be 
     * accessed. 
     * @return 
     */
    private Window getDashboard() {
        return getTestMan().getTestObject("SOLA.Dashboard", Window.class);
    }

    private void setDashboard(Window cw) {
        getTestMan().loadTestObject("SOLA.Dashboard", cw);
    }

    /**
     * Prints the exception to standard output and sets the abortTest flag to prevent 
     * any further execution of the test. 
     * @param ex The exception that was raised.
     * @return Always false.
     */
    private boolean ProcessException(Exception ex) {
        System.out.println(ex);
        setAbortTest(true);
        return false;
    }

    /**
     * Attempts to obtain the display value for an item. Uses reflection to try and obtain the
     * appropriate display text for the item by checking for various methods on the object. 
     * @param item The item to obtain the display value for. 
     * @return The display value for the item or an empty string. 
     * @throws Exception 
     */
    private String getDisplayValue(Object item) throws Exception {
        String result = "";


        if (item != null) {
            // Basic String list so get the value for display
            if (String.class.isAssignableFrom(item.getClass())) {
                result = item.toString();
            } else {
                // Complex type - see if the type has a getDisplayValue or a getName 
                // method and try to get the value of that method using java reflection
                for (Method m : item.getClass().getMethods()) {
                    if (m.getName().equals("getDisplayValue")) {
                        result = m.invoke(item).toString();
                        break;
                    } else if (m.getName().equals("getName")) {
                        result = m.invoke(item).toString();
                        break;
                    }
                }
            }
        }
        return result;
    }

    /** 
     * Returns the test data item matching the dataName or null
     * @param dataName The name of the test data item to find. 
     * @return The item if found or null.
     */
    private TestDataItem getDataItem(String dataName) {
        TestDataItem result = null;
        result = getTestMan().getTestDataItem(dataName);
        return result;
    }

    /**
     * Returns the dataNameOrDefault value if getData does not obtain a test data item matching
     * the dataNameOrDefault value. 
     * @param dataNameOrDefault A text value or a test data item name. 
     * @return the textOrDataName value or the value from the test data item. 
     * @throws Exception 
     */
    private String getDataOrDefault(String dataNameOrDefault) {
        String result = getData(dataNameOrDefault);
        result = result == null ? dataNameOrDefault : result;
        return result;
    }

    /**
     * Gets the data value for the specified dataName. Returns null if no test data item matching
     * the data name exists. Also processes common actions on the test data item. For example
     * the current datetime will be returned if the Action it TODAY.
     * @param dataName Name of test data item to find. 
     * @return The test data item if found or null. 
     */
    public String getData(String dataName) {
        String result = null;
        if (isAbortTest()) {
            return result;
        }
        TestDataItem data = this.getDataItem(dataName);
        if (data != null) {
            // Default the result value
            result = data.getValue();
            String action = getAction(data);

            if (action.equals("TODAY")) {
                // Calculate a date based on todays date
                int offset = 0;
                Integer actionExt = getActionExtension(data, Integer.class);

                if (actionExt
                        != null) {
                    offset = actionExt;
                }
                Date date = DateUtility.addDays(offset, false);
                result = DateUtility.simpleFormat(date, "dd/mm/yyyy");
            }
        }
        return result;
    }

    /**
     * @return Returns the Action for the test data item or null if there is no matching 
     * test data item or the Action for the test data item is null.
     * @see #getAction(TestDataItem) 
     */
    private String getAction(String dataNameOrText) {
        return getAction(getDataItem(dataNameOrText));
    }

    /**
     * @return Returns the Action for the test data item or null if there is no matching test data
     * item or the Action for the test data item is null.
     * @see #getAction(String)
     */
    private String getAction(TestDataItem data) {
        String result = "";
        result = data != null && data.getAction() != null ? data.getAction() : result;
        return result;
    }

    /**
     * @return Returns the ActionExtension for the test data item cast to the type of {@code <T>} 
     * or null if there is no matching test data item or the ActionExtension for the test 
     * data item is null. 
     * @throws RuntimeException If the ActionExtension cannot be cast to type {@code <T>}
     * @see #getActionExtension(TestDataItem, Class) 
     */
    private <T> T getActionExtension(String dataNameOrText, Class<T> dataType) {
        return getActionExtension(getDataItem(dataNameOrText), dataType);
    }

        /**
     * @return Returns the ActionExtension for the test data item cast to the type of {@code <T>} 
     * or null if there is no matching test data item or the ActionExtension for the test 
     * data item is null. 
     * @throws RuntimeException If the ActionExtension cannot be cast to type {@code <T>}
     * @see #getActionExtension(String, Class) 
     */
    private <T> T getActionExtension(TestDataItem data, Class<T> dataType) {
        T result = null;
        String actionExt = null;
        actionExt = data != null && data.getActionExtension() != null
                ? data.getActionExtension() : actionExt;
        if (actionExt != null) {
            try {
                result = dataType.cast(actionExt);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to cast action extension for " + data.getName(), ex);
            }
        }
        return result;
    }

    /**
     * Starts the SOLA Desktop Application with the default Locale set to English. The default 
     * user name and password used to login to the application are test, test. To provide alternative
     * user credentials, configure setup data for {@code Login: User Name} and {@code Login: Password}. 
     * e.g. 
      * <pre>
     * |script|Setup Test Data                          |
     * |Field |Login: User Name |Value|myusername       |
     * |Field |Login: Password  |Value|mysecretpassword |
     * </pre>
     * @return True if the login succeeds. 
     * @throws Exception If an error occurs. 
     */
    public boolean startSola() throws Exception {
        boolean result = false;
        try {
            // Force the default locale for the application to be English
            Locale.setDefault(Locale.ENGLISH);
            WindowInterceptor.init(new Trigger() {

                public void run() { // Start the DesktopApplication by triggering the "main"
                    DesktopApplication.main(new String[0]);
                }
            }).processTransientWindow() // Process the Splash Screen
                    .process(new WindowHandler() { // Proces the Logon Screen

                public Trigger process(Window logon) throws Exception {
                    // Login to the application
                    String userName = getData("Login:UserName");
                    String pass = getData("Login:Password");
                    userName = userName == null ? "test" : userName;
                    pass = pass == null ? "test" : pass;
                    System.out.println("Logging into SOLA with username " + userName
                            + ", password " + pass);
                    logon.getTextBox("txtUserName").setText(userName);
                    logon.getPasswordField("txtPassword").setPassword(pass);
                    return logon.getButton("Login").triggerClick();
                }
            }).process(new WindowHandler() { // Get a handle to the main form

                public Trigger process(Window win) throws Exception {
                    mainWin[0] = win; // Get the handle
                    return Trigger.DO_NOTHING; // Dont do any other action
                }
            }).run();

            if (mainWin[0] != null) {
                System.out.println("Login successful");
                result = true;
                this.setCurrentWindow(mainWin[0]);
                this.setDashboard(mainWin[0]);
            }

        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;
    }

    /** 
     * Exits the SOLA application using the {@code System.exit(0)} command. To avoid an exception
     * being raised by FitNesse due to the unexpected termination of the JVM before all of the 
     * test results can be collected, a kill thread is spawned to issue the exit command after a
     * delay of 500ms. Also clears all test objects and test data items from the 
     * {@linkplain TestManager}.
     * @throws Exception 
     */
    public void exitSola() throws Exception {
        try {
            getTestMan().clear();
            Thread killThread = new Thread(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {
                    }
                    System.exit(0);
                }
            });
            killThread.start();
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
    }

    /** 
     * Sets the text on the specified text control on the current window. 
     * @param dataNameOrText The text to set or the name of a test data item to obtain the text from. 
     * @param dataNameOrLabel The label for the text control or the name of a test data item to
     * obtain the label from. 
     * @return True if the text is successfully set on the control. 
     * @throws Exception 
     */
    public boolean typeInto(String dataNameOrText, String dataNameOrLabel) throws Exception {
        boolean result = false;
        try {
            String label = getDataOrDefault(dataNameOrLabel);
            String text = getDataOrDefault(dataNameOrText);
            TextBox tb = getCurrentWindow().getTextBox(label);
            if (tb != null) {
                System.out.println("Setting " + label + " to " + text);
                tb.setText(text);
                result = tb.textEquals(text).isTrue();
            }
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;

    }

    /**
     * Triggers the click action on the specified button on the current window. 
     * @param dataNameOrLabel The label of the button to click or the name of a test data item to 
     * obtain the label from
     * @return True if the button is found and clicked
     * @throws Exception If the button is not found on the current window. 
     */
    public boolean click(String dataNameOrLabel) throws Exception {
        boolean result = false;
        if (isAbortTest()) {
            return result;
        }
        try {
            String buttonLabel = getDataOrDefault(dataNameOrLabel);
            Button button = getCurrentWindow().getButton(buttonLabel);
            if (button != null) {
                System.out.println("Clicking button " + buttonLabel);
                button.triggerClick().run();
                result = true;
            }
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;
    }

    /**
     * Opens a window using a button click and resets the current window to the newly opened window.
     * Checks the new window title with the window title specified in dataNameOrTitle. 
     * @param dataNameOrTitle The title of the window to open or the name of a test data item to 
     * obtain the window title from. 
     * @param dataNameOrLabel The label of the button to click or the name of a test data item to 
     * obtain the label from. 
     * @return True if the window is successfully opened. 
     * @throws Exception If the window was not opened. 
     */
    public boolean openWindowClick(String dataNameOrTitle, String dataNameOrLabel) throws Exception {
        boolean result = false;
        if (isAbortTest()) {
            return result;
        }
        try {
            String windowTitle = getDataOrDefault(dataNameOrTitle);
            String errorMsg = "Failed to open window " + windowTitle;
            Button button = getCurrentWindow().getButton(getDataOrDefault(dataNameOrLabel));
            if (button != null) {
                System.out.println("Opening window " + windowTitle);
                Window newWin = WindowInterceptor.run(button.triggerClick());
                if (newWin != null) {
                    this.setCurrentWindow(newWin);
                    if (this.getCurrentWindow().getTitle().equalsIgnoreCase(windowTitle)) {
                        result = true;
                    } else {
                        errorMsg = errorMsg + ". Opened window " + this.getCurrentWindow().getTitle()
                                + " instead.";
                    }
                }
            }
            if (!result) {
                throw new Exception(errorMsg);
            }
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;
    }

    /** 
     * @return The xml description for the current window. Note that if the window includes tabs, 
     * only the tab with focus will have its details returned. 
     * @throws Exception If current window is not set. 
     */
    public String getDescription() throws Exception {
        String result = "!";
        if (isAbortTest()) {
            return result;
        }
        try {
            result = "!<" + this.getCurrentWindow().getDescription() + ">!";
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;
    }

    /**
     * @return The title for the current window. 
     * @throws Exception If the current window is not set. 
     */
    public String getWindowTitle() throws Exception {
        String result = null;
        if (isAbortTest()) {
            return result;
        }
        try {
            result = this.getCurrentWindow().getTitle();
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;
    }

    /**
     * Obtains the list of options from the specified combo box or list box on the current window. 
     * @param dataNameOrLabel The name of the combo box or list box to get the items list 
     * from or the name of a test data item to obtain the label from. 
     * @return The list of items in the combo box or list box. These are returned as one string, 
     * with each option delimited by a pipe (i.e. |). If the combo box / list box contains 
     * no options, ! is returned. 
     **/
    public String optionsOf(String dataNameOrLabel) throws Exception {
        String result = "|";
        if (isAbortTest()) {
            return result;
        }
        try {
            String controlLabel = getDataOrDefault(dataNameOrLabel);
//            log ("Start options of " + controlLabel);
            ComboBox comboBox = null;
            try {
                comboBox = getCurrentWindow().getComboBox(controlLabel);
            } catch (ItemNotFoundException ex) {
                // Ignore the exception as this might be a list box. 
            }
            if (comboBox != null) {
                System.out.println("Found combo box for " + controlLabel + ". Listing "
                        + comboBox.getAwtComponent().getItemCount() + "options.");
                for (int i = 0; i < comboBox.getAwtComponent().getItemCount(); i++) {
                    Object item = comboBox.getAwtComponent().getItemAt(i);
                    result = result + getDisplayValue(item) + "|";
                }
            } else {
                ListBox listBox = null;
                try {
                    listBox = getCurrentWindow().getListBox(controlLabel);
                } catch (ItemNotFoundException ex) {
                    // Ignore the exception as this might be a table. 
                }
                if (listBox != null) {
                    System.out.println("Found list box for " + controlLabel + ". Listing "
                            + listBox.getSize() + " options.");
                    for (int i = 0; i < listBox.getSize(); i++) {
                        Object item = listBox.getAwtComponent().getModel().getElementAt(i);
                        result = result + getDisplayValue(item) + "|";
                    }
                } else {
                    Table table = null;
                    try {
                        log ("options of " + controlLabel);
                        table = getCurrentWindow().getTable(controlLabel);
                    }  catch (ItemNotFoundException ex) {
                    // Ignore the exception as this might be another control. 
                    }

                    if (table != null) {
                        log(("Found table for " + controlLabel + ". Listing in optionsOf"
                                + table.getColumnCount() + " options."));
                        System.out.println("Found table for " + controlLabel + ". Listing "
                                + table.getRowCount() + " options.");
                            for (int i = 0; i < table.getColumnCount(); i++) {
//                            Object item = table.getContentAt(0, i);
//                            result = result + getDisplayValue(item) + "|";
                        }
                    }
                    log("Display Value: " + result);
                }
            }

        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }

        if (result.equals("|")) {
            result = "!";
        }

        return result;
    }

    /**
     * Selects an option from the specified combo box or list box on the current window. 
     * @param dataNameOrText The text of the option to select or the name of a test data item that
     * contains the text of the option to select. 
     * @param dataNameOrLabel The label of the combo box or list box to select from or the name of 
     * a test data item to obtain the label from. 
     * @return True if the option is successfully selected otherwise false. 
     * @throws Exception 
     */
    public boolean selectFrom(String dataNameOrText, String dataNameOrLabel) throws Exception {
        boolean result = false;
        if (isAbortTest()) {
            return result;
        }
        try {
            String itemToSelect = getDataOrDefault(dataNameOrText);
            String action = getAction(dataNameOrText);
            String controlLabel = getDataOrDefault(dataNameOrLabel);
            ComboBox comboBox = null;
            try {
                comboBox = getCurrentWindow().getComboBox(controlLabel);               
            } catch (ItemNotFoundException ex) {
                // Log the message for the user
                System.out.println(ex.getMessage() + ". Looking ComboBox.");
            }
            if (comboBox != null) {
                System.out.println("Found combo box for " + controlLabel);
                 if (action.equals("FIRST")) {
                    if (comboBox.getAwtComponent().getItemCount() > 0) {
                        // Select the first value in the combo
                        comboBox.getAwtComponent().setSelectedIndex(0);
                       result = true;
                    } else {
                        System.out.println("FIRST item not selected as combo box did not contain"
                                + " any items.");
                    }
                    } else if (action.equals("LAST")) {
                    if (comboBox.getAwtComponent().getItemCount() > 0) {
                        // Select the first value in the combo
                        comboBox.getAwtComponent().setSelectedIndex(comboBox.getAwtComponent().getItemCount() - 1);
                        result = true;
                    } else {
                        System.out.println("LAST item not selected as combo box did not contain"
                                + " any items.");
                    }
                } else {
                    // Try to select the item indicated by the data value and check if the
                    // appropriate value was selected
                    comboBox.select(itemToSelect);
                     result = comboBox.selectionEquals(itemToSelect).isTrue();
                 }

            } else {
                ListBox listBox = null;
                try {
                    listBox = getCurrentWindow().getListBox(controlLabel);
                } catch (ItemNotFoundException ex) {
                    // Log the message for the user
                 System.out.println(ex.getMessage() + ". Looking ListBox.");

                }
                if (listBox != null) {
                    System.out.println("Found list box for " + controlLabel);
                    if (action.equals("FIRST")) {
                        if (listBox.getSize() > 0) {
                            listBox.getAwtComponent().setSelectedIndex(0);
                            result = !listBox.selectionIsEmpty().isTrue();
                        } else {
                            System.out.println("FIRST item not selected as list box did not contain"
                                    + " any items.");
                        }
                   } else if (action.equals("LAST")) {
                         if (listBox.getSize() > 0) {
                             //not sure if this gets last value or last potential value
                            listBox.getAwtComponent().setSelectedIndex(listBox.getAwtComponent().getMaxSelectionIndex());
                            result = !listBox.selectionIsEmpty().isTrue();
                        } else {
                            System.out.println("LAST item not selected as list box did not contain"
                                    + " any items.");
                        }
                    } else {
                        // Try to select the item indicated by the data value and check if the
                        // appropriate value was selected
                        listBox.select(itemToSelect);
                        result = listBox.selectionEquals(itemToSelect).isTrue();
                    }
                } else {
                    Table table = null;
                    try {
//                    log("Control is " + controlLabel + " looking for " + dataNameOrText + " or " + dataNameOrLabel);
                        table = getCurrentWindow().getTable(controlLabel);
                    } catch (ItemNotFoundException ex) {
                        // Log the message for the user
                     System.out.println(ex.getMessage() + ". Looking Table.");
//                    log("22Control is " + controlLabel + " looking for " + dataNameOrText + " or " + dataNameOrLabel);
                    }
//                    log("333Control is " + controlLabel + " looking for " + dataNameOrText + " or " + dataNameOrLabel);
                    if (table != null) {
                        System.out.println("Found table for " + controlLabel);
               log("Found table (selectFrom) for " + controlLabel+ " " + table.getRowCount() + " rows |" + table.getColumnCount() + " columns");
                           for (int i = 0; i < table.getRowCount(); i++) {
                           for (int j = 0; i < table.getColumnCount(); j++) {
                              Object item = table.getContentAt(i, j);
                              log("i=" + i + "j=" + j + "|" + item);
                             
                           }
                               
                           }
//                            Object item = table.getContentAt(0, i);
//                            result = result + getDisplayValue(item) + "|";
                            // Try to select the item indicated by the data value and check if the
                            // appropriate value was selected
                            table.selectRowsWithText(0, dataNameOrText);
                            result = !table.selectionIsEmpty().isTrue();
                         }
                    }
            }
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;
    }

    /** 
     * Brings the specified tab into focus on the current window. This method assumes the window
     * will only have one tab group. 
     * @param dataNameOrTabLabel The label of the tab to select or the name of a test data item to
     * obtain the label from. 
     * @return True if the tab is successfully selected. 
     * @throws Exception 
     */
    public boolean selectTab(String dataNameOrTabLabel) throws Exception {
        boolean result = false;
        if (isAbortTest()) {
            return result;
        }
        try {
            // Assume there is only one tab group displayed on the page...
            TabGroup tg = getCurrentWindow().getTabGroup();
            String label = getDataOrDefault(dataNameOrTabLabel);
            if (tg != null) {
                System.out.println("Selecting tab " + label);
                tg.selectTab(label);
                result = true;
            }
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }
        return result;

    }

  
    /**
     * Returns the text for a message captured by the {@linkplain MessageResponder}. Note that
     * only messages that have been configured with explicit response values using setup data
     * can be queried with this method. This method can be used to confirm a specific message 
     * has displayed the expected information (e.g. That a validation message has listed all 
     * invalid fields, etc)
     * @param messageCode The code of the message to check the text for.
     * @return The text matching the message code or ! if the message has not been captured. 
     * @throws Exception 
     */
    public String getMessageText(String messageCode) throws Exception {
        String result = "!";
        if (isAbortTest()) {
            return result;
        }
        LocalizedMessage msg = getTestMan().getTestObject(messageCode.toUpperCase(),
                LocalizedMessage.class);
        if (msg != null) {
            result = msg.getMessage();
        }
        return result;
    }

    /**
     * @return The list of all messages that have been captured by the {@linkplain MessageResponder}
     * This list contains the message codes only and is pipe (i.e. |) delimited. 
     * e.g. {@code |CLIGNRL001|SEREXCP004|}. If no messages have been captured ! is returned. This
     * can be used to confirm the correct sequence of messages has been displayed for a given 
     * action E.g. Lodge Application. To clear this list, use {@linkplain #clearMessages()}
     * @throws Exception 
     */
    public String getMessages() throws Exception {
        String result = "!";
        if (isAbortTest()) {
            return result;
        }
        String msgs = getTestMan().getTestObject("Messages", String.class);
        if (msgs != null) {
            result = msgs;
        }
        return result;
    }

    /** 
     * Clears the list of messages captured by the {@linkplain MessageResponder}. Can be used in
     * combination with {@linkplain #getMessages()} to ensure the correct sequence of messages are 
     * displayed for an action. 
     * */
    public boolean clearMessages() throws Exception {
        boolean result = false;
        if (isAbortTest()) {
            return result;
        }
        getTestMan().loadTestObject("Messages", null);
        result = true;
        return result;
    }

    /**
     * Checks the specified check box. Note that {@linkplain #selectFor(String, String)}
     * should be used in preference to {@linkplain #tick(String)} as it will allow the state of 
     * checkbox to be set to either true of false though test data configuration. 
     * @param dataNameOrLabel The label of the checkbox to check or the name of a data item to 
     * obtain the label from. 
     * @return True if the checkbox is successfully checked, otherwise false. 
     * @throws Exception 
     * @see #selectFor(String, String) 
     */
    public boolean tick(String dataNameOrLabel) throws Exception {
        return selectFor("true", dataNameOrLabel);
    }

    /** 
     * Checks or unchecks the specified checkbox depending on the dataNameOrBoolean. 
     * @param dataNameOrBoolean The boolean value (true or false) to use to set the state of
     * the checkbox or the name of the data item to obtain the boolean value from. Note this value
     * is treated as "true" unless it is set to the value "false". 
     * @param dataNameOrLabel The label of the checkbox to check or the name of a data item to 
     * obtain the label from. 
     * @return True if the checkbox state is successfully changed. 
     * @throws Exception 
     * @see #tick(String) 
     */
    public boolean selectFor(String dataNameOrBoolean, String dataNameOrLabel) throws Exception {
        boolean result = false;
        if (isAbortTest()) {
            return result;
        }
        try {
            boolean selectState = true;
            String controlLabel = getDataOrDefault(dataNameOrLabel);
            String selectStateStr = getDataOrDefault(dataNameOrBoolean);
            if (selectStateStr != null && selectStateStr.toLowerCase().equals("false")) {
                selectState = false;
            }
            CheckBox checkBox = null;
            try {
                checkBox = getCurrentWindow().getCheckBox(controlLabel);
            } catch (ItemNotFoundException ex) {
                // Log the message for the user
                System.out.println(ex.getMessage());
            }
            if (checkBox != null) {
                if (selectState) {
                    System.out.println("Checking check box " + controlLabel);
                    checkBox.select();
                    result = checkBox.isSelected().isTrue();
                } else {
                    System.out.println("De-selecting check box " + controlLabel);
                    checkBox.unselect();
                    result = !checkBox.isSelected().isTrue();
                }
            }
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }

        return result;
    }

    /**
     * Opens a window using the a menu option from the Dashboard. 
     * @param dataNameOrTitle The title of the window to open or the name of a test data item to 
     * obtain the window title from. 
     * @param dataNameOrMenu The name of the main menu option to select or the name of a  test 
     * data item to obtain the menu name from. 
     * @param dataNameOrSubMenu The name of the sub menu option to select or the name of the data
     * item to obtain the sub menu name from. Can be null. If null, the main menu option is clicked. 
     * @return True if the window is successfully opened. 
     * @throws Exception If the window is not successfully opened. 
     */
    public boolean openWindowMenuSubmenu(String dataNameOrTitle,
            String dataNameOrMenu, String dataNameOrSubMenu) throws Exception {
        boolean result = false;
        if (isAbortTest()) {
            return result;
        }
        try {
            String windowTitle = getDataOrDefault(dataNameOrTitle);
            String menuName = getDataOrDefault(dataNameOrMenu);
            String subMenuName = getDataOrDefault(dataNameOrSubMenu);
            MenuItem menu = getDashboard().getMenuBar().getMenu(menuName);
            String errorMsg = "Failed to open window " + windowTitle;
            if (menu != null) {
                if (subMenuName != null) {
                    menu = menu.getSubMenu(subMenuName);
                }
            }
            if (menu != null) {
                System.out.println("Selecting meun item " + menuName + " > " + subMenuName
                        + " to open window " + windowTitle);
                Window newWin = WindowInterceptor.run(menu.triggerClick());
                if (newWin != null) {
                    this.setCurrentWindow(newWin);
                    if (this.getCurrentWindow().getTitle().equalsIgnoreCase(windowTitle)) {
                        result = true;
                    } else {
                        errorMsg = errorMsg + ". Opened window " + this.getCurrentWindow().getTitle()
                                + " instead.";
                    }
                }
            } else {
                System.out.println("Unable to locate the menu " + menuName
                        + " or its submenu " + subMenuName);
            }
            if (!result) {
                throw new Exception(errorMsg);
            }
        } catch (Exception ex) {
            ProcessException(ex);
            throw ex;
        }

        return result;
    }
}