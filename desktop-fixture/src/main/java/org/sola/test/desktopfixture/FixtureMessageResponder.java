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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.test.desktopfixture;

import org.sola.common.messaging.LocalizedMessage;
import org.sola.common.messaging.MessageResponder;


/**
 * Used by the {@linkplain DesktopFixture} to suppress the display of messages sent to 
 * the {@linkplain org.sola.common.messaging.MessageUtility}. Also provides the ability to 
 * override the default response for each message using setup data. See
 * {@linkplain #getResponse(LocalizedMessage, String, int)} for an example. 
 * @author soladev
 */
public class FixtureMessageResponder implements MessageResponder  {

    private TestManager getTestMan() {
        return TestManager.getInstance();
    }

    private String getData(String dataName) {
        String result = null;
        TestDataItem data = getTestMan().getTestDataItem(dataName);
        if (data != null) {
            result = data.getValue();
        }
        return result;
    }

    /**
     * Logs the message to standard output and checks the setup data to determine if the
     * user has indicated a specific response for the message based on the message code. 
     * e.g. To provide the response "No" to message CLIAPP004 from FitNesse, 
     * use the following setup. Note that the Value must match exactly the text displayed 
     * on the button to select. 
     * <pre>
     * |script|Setup Test Data               |
     * |Field |CLIAPP004        |Value|No    |
     * </pre>
     * <p>If no customized response is configured for a message, the default response will
     * be used unless the message is an Error. Error messages that do not have a specific
     * response configured are assumed to be unexpected errors and will cause an Exception to be
     * raised to stop execution of the test.</p>
     * @param msg The localized message details
     * @param errorNumber The error number generated for the message or null if an error number
     * is not applicable. 
     * @param defaultButton The number of the default button for the message. 
     * @return The number of the button to use as the response for the message. 
     */
    @Override
    public int getResponse(LocalizedMessage msg, String errorNumber, int defaultButton) {
        int result = defaultButton;
        String messageResponse = getData(msg.getMessageCode());
        if (messageResponse != null) {
            // The user has specified the text of the button, so match this to the
            // list of dialog options to determine the approriate number of the button
            for (int i = 0; i < msg.getDialogOptions().length; i++) {
                if (messageResponse.equalsIgnoreCase(msg.getDialogOptions()[i])) {
                    result = i;
                    break;
                }
            }
            // Load the details of the specific message so that the user can check the
            // messge details if necesary. 
            getTestMan().loadTestObject(msg.getMessageCode().toUpperCase(), msg);
        } else if (msg.getType() == LocalizedMessage.Type.ERROR) {
            // Error message that hasn't been trapped - throw exception
            String error = "Desktop Fixture: Error message with no response "
                    + "identified. Assumed to be an unexpected error! Message "
                    + msg.getMessageCode().toUpperCase() + " [" + msg.getMessage()
                    + "].";
            throw new RuntimeException(error);
        }
        // Capture the message code so that it is possible for the user to check the messages that
        // have been processed. 
        String messages = getTestMan().getTestObject("Messages", String.class);
        messages = messages == null ? "|" + msg.getMessageCode().toUpperCase() + "|"
                : messages + msg.getMessageCode().toUpperCase() + "|";
        getTestMan().loadTestObject("Messages", messages);

        String options = "";
        for (int i = 0; i < msg.getDialogOptions().length; i++) {
            if (!options.equals("")) {
                options = options + ", ";
            }
            options = options + msg.getDialogOptions()[i];
        }
        System.out.println("Responding to message " + msg.getMessageCode().toUpperCase() + " ["
                + msg.getMessage() + "] with option " + msg.getDialogOptions()[result]
                + ". Available options: " + options + ".");
        return result;
    }
}
