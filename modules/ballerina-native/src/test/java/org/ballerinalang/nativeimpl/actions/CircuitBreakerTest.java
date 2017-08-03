/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.nativeimpl.actions;

import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.util.BTestUtils;
import org.ballerinalang.natives.BuiltInNativeConstructLoader;
import org.ballerinalang.util.codegen.ProgramFile;
import org.ballerinalang.util.program.BLangFunctions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;


/**
 * Test class for testing the functionality of the Circuit Breaker
 */
public class CircuitBreakerTest {

    private ProgramFile programFile;

    @BeforeClass
    public void setup() {
        programFile = BTestUtils.getProgramFile("samples/circuitBreakerTest.bal");
        BuiltInNativeConstructLoader.loadConstructs();
    }

    //@Test
    public void testCallingAction() {
        BValue[] args = {new BString("WSO2")};
        BValue[] returns = BLangFunctions.invokeNew(programFile, "testArgumentPassing", args);
        Assert.assertEquals(returns.length, 1);
        BInteger actionReturned = (BInteger) returns[0];
        int expected = 500;
        Assert.assertEquals(actionReturned.intValue(), expected);
    }

    //@Test
    public void testHTTP() {
        BValue[] args = {new BString("WSO2")};
        BValue[] returns = BLangFunctions.invokeNew(programFile, "testHTTP", args);
        Assert.assertEquals(returns.length, 1);
        BInteger actionReturned = (BInteger) returns[0];
        int expected = 500;
        Assert.assertEquals(actionReturned.intValue(), expected);
    }
}
