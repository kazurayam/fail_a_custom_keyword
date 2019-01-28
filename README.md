fail() - a primitive keyword
============================

## What is this?

This is a small [Katalon Studio](https://www.katalon.com/) project for demonstration
purpose. The project's zip file is available at the
[Releases](https://github.com/kazurayam/fail_a_primitive_keyword/releases) page.
You can download a zip file, unzip it, open the project with your local Katalon Studio.

This project was developed using Katalon Studio 5.10.1.

## Problem to solve

### Reproducing a problem on try-catch block in a test using callTestCase()

duyluong, a Katalon Developer, made an issue
["CallTestCase using in try-catch block throws StepFailedException"](https://github.com/katalon-studio/katalon-studio/issues/79).

>Expected result:
>If users use callTestCase in try-catch block, KS should NOT throw StepFailedException. Main Test Case should pass if nothing throws in the catch or finally block.
>Actual result:
>If users use callTestCase in try-catch block, KS throws StepFailedException and marks the main test case failed.

I was not aware of the reported problem. I wanted to reproduce the problem on my PC. I made a *main* test case like the following psuedo code:
```
try {
    WebUI.callTestCase(findTestCase('sub'), [:])
} catch (Exception ex) {}
```

And I a test case named `sub`, which is called the *main*, like the following psuedo code:
```
import com.kms.katalon.core.util.KeywordUtil
KeywordUtil.markFailureAndStop("so long")
```

**Expected behavior**:

The *main* test case is expected to pass. Even if the *sub* test case failed and threw StepFailedException, the `try` block in the *main* is expected to catch it.

**Actual behavior to be preproduced**:

The [issue](https://github.com/katalon-studio/katalon-studio/issues/79) reports that KS 5.10.1 will actually behave different. The *main* test case will actually fail in KS 5.10.1. When the *sub* test case threw StepFailedException, the `try` block in the *main* is unable to catch it.

I want to witness the behavior.

### a custom keyword that always fail

In order to investigate the reported issue, I wanted to create a comprehensive suite of test cases, which should include:

1. a caller that calls a sub test case without `try-catch` block; the sub test case *does not* log failure, *does not* throw `StepFailedException`.
2. a caller that calls a sub test case without `try-catch` block; the sub test case **does** log failure, but does not throw `StepFailedException`.
3. a caller that calls a sub test case without `try-catch` block; the sub test case **does** log failure, and **does** throw `StepFailedException`.
4. a caller that calls a sub test case **with** `try-catch` block; the sub test case does not log failure, does not throw `StepFailedException`.
5. a caller that calls a sub test case **with** `try-catch` block; the sub test case does log failure, but does not throw `StepFailedException`.
6. a caller that calls a sub test case **with** `try-catch` block; the sub test case does log failure, and does throw `StepFailedException`.

A long list of test cases. I wanted to make my test case codes concise. Therefore I needed a custom keyword which enables me to encapsulate the detail behavior (to log failure or not; to throw exeption or not;) in 1 line of code per each 6 test cases above.

## Solution

## Description

### a primitive keyword: fail

I made a custom keyword  [`com.kazurayam.ksbackyard.PrimitiveKeywords#fail`](Keywords/com/kazurayam/ksbackyard/PrimitiveKeywords.groovy):
```
@Keyword
static void fail(String message, FailureHandling flowControl)
        throws StepFailedException {
    switch (flowControl) {
        case FailureHandling.OPTIONAL:
            KeywordUtil.logInfo(message)
            break
        case FailureHandling.CONTINUE_ON_FAILURE:
            KeywordUtil.markFailed(message)
            break
        case FailureHandling.STOP_ON_FAILURE:
            KeywordUtil.markFailedAndStop(message)
            break
    }
```

How it works?

1. `fail` does no more than emiting the given message into the Katalon Log, just like `WebUI.comment` does.  
2. if `FailureHandling.CONTINUE_ON_FAILURE` is specified as the 2nd arg, it emits further stack trace message, but does **NOT** throw any StepFailedException.
3. if `FailureHandling.STOP_ON_FAILURE` is specified as the 2nd arg, it emits further stack trace message, and throw new StepFailedExcption which is raised up to the caller

Let's try to see.

When I run [`Test Cases/caller - OPTIONAL`](Scripts/caller%20-%20OPTIONAL/Script1548640756567.groovy), it will emit messages as follows:
```
>>> Intentional failure. Caller can safely ignore this
...
>>> callTestCase() failed but we ignored it as FailureHandling.OPTIONAL specfied
```

When I run [`Test Cases/caller - CONTINUE_ON_FAILURE`](Scripts/caller%20-%20CONTINUE_ON_FAILURE/Script1548640743447.groovy), it will emit messages as follows:
```
2019-01-28 15:04:17.961 ERROR c.k.katalon.core.main.TestCaseExecutor   - ❌ com.kazurayam.ksbackyard.PrimitiveKeywords.fail(message, CONTINUE_ON_FAILURE) FAILED.
Reason: com.kms.katalon.core.exception.StepFailedException:
>>> Intentional failure. Caller can continue
	at com.kms.katalon.core.util.KeywordUtil.markFailed(KeywordUtil.java:18)
	at com.kms.katalon.core.util.KeywordUtil$markFailed.call(Unknown Source)
	at com.kazurayam.ksbackyard.PrimitiveKeywords.fail(PrimitiveKeywords.groovy:25)
	at com.kazurayam.ksbackyard.PrimitiveKeywords.invokeMethod(PrimitiveKeywords.groovy)
	at com.kms.katalon.core.main.CustomKeywordDelegatingMetaClass.invokeStaticMethod(CustomKeywordDelegatingMetaClass.java:49)
	at sub - CONTINUE_ON_FAILURE.run(sub - CONTINUE_ON_FAILURE:3)
...
```
>>I expected to see a message of ">>> callTestCase() failed but Caller continued as FailureHandling.CONTINUE_ON_FAILURE specified" here. But actually I could not see it. I think `Test Cases/caller - CONTINUE_ON_FAILURE` should not throw a StepFailedException but it actually does. I think it is a bug in Katalon Stdudio 5.10.1.

[`Test Cases/caller - STOP_ON_FAILURE`](Scripts/caller%20-%20STOP_ON_FAILURE/Script1548640769956.groovy) will emit messages is similar to `Test Cases/caller - CONTINUE_ON_FAILURE`

### Reproducing the problem of `try-catch`

I made a test case [`Test Cases/caller with try - STOP_ON_FAILURE`](Scripts/caller%20with%20try%20-%20STOP_ON_FAILURE/Script1548642900086.groovy)
```
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

try {
	WebUI.callTestCase(findTestCase('sub - STOP_ON_FAILURE'),
		["message":">>> Intentional failure. Caller should stop immediately"])

	WebUI.comment(">>> callTestCase() failed")
	WebUI.comment(">>> but you will not seed this message")
} catch (Exception ex) {
	WebUI.comment(">>> caught an Exception: " + ex.getMessage())
}
```

**Expected behavior**
```
>>> Internal failure. Caller should stop immediately
...
>>> caught an Exception: ...
```

**Reproduced behavior**
```
2019-01-28 15:27:15.420 ERROR k.k.c.m.CustomKeywordDelegatingMetaClass - ❌ >>> Intentional failure. Caller should stop immediately
2019-01-28 15:27:15.439 ERROR c.k.katalon.core.main.TestCaseExecutor   - ❌ com.kazurayam.ksbackyard.PrimitiveKeywords.fail(message, STOP_ON_FAILURE) FAILED.
Reason:
com.kms.katalon.core.exception.StepFailedException: >>> Intentional failure. Caller should stop immediately
	at com.kms.katalon.core.util.KeywordUtil.markFailedAndStop(KeywordUtil.java:27)
	at com.kms.katalon.core.util.KeywordUtil$markFailedAndStop.call(Unknown Source)
	at com.kazurayam.ksbackyard.PrimitiveKeywords.fail(PrimitiveKeywords.groovy:28)
	at com.kazurayam.ksbackyard.PrimitiveKeywords.invokeMethod(PrimitiveKeywords.groovy)
	at com.kms.katalon.core.main.CustomKeywordDelegatingMetaClass.invokeStaticMethod(CustomKeywordDelegatingMetaClass.java:49)
	at sub - STOP_ON_FAILURE.run(sub - STOP_ON_FAILURE:5)
```

Here I could see:
- The `try-catch` block in the caller is expected to catch the StepFailedException raised by the callee test case, but it does not. Rather the test case entirely failed and the full stack trace was printed in the log.
