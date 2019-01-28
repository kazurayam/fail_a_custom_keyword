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

I was not aware of the reported problem. **I wanted to reproduce the problem on my PC**.

I made a *main* test case like the following psuedo code:
```
try {
    WebUI.callTestCase(findTestCase('sub'), [:])
} catch (Exception ex) {}
```

And I a *sub* test case named `sub`, which is called the *main*, like the following psuedo code:
```
import com.kms.katalon.core.util.KeywordUtil
KeywordUtil.markFailureAndStop("so long")
```

**Expected behavior**:

The *main* test case is expected to pass. Even if the *sub* test case failed and threw StepFailedException, the `try` block in the *main* is expected to catch it.

**Actual behavior to be preproduced**:

The [issue](https://github.com/katalon-studio/katalon-studio/issues/79) reports that KS 5.10.1 will actually behave different. The *main* test case will actually fail in KS 5.10.1. When the *sub* test case threw StepFailedException, the `try` block in the *main* is unable to catch it.

### a custom keyword that always fail

I wanted to create a comprehensive suite of test cases, which should include:
1. a caller that calls a sub test case without `try-catch` block; the sub test case *does not* log failure, *does not* throw `StepFailedException`.
2. a caller that calls a sub test case without `try-catch` block; the sub test case **does** log failure, but does not throw `StepFailedException`.
3. a caller that calls a sub test case without `try-catch` block; the sub test case **does** log failure, and **does** throw `StepFailedException`.
4. a caller that calls a sub test case **with** `try-catch` block; the sub test case does not log failure, does not throw `StepFailedException`.
5. a caller that calls a sub test case **with** `try-catch` block; the sub test case does log failure, but does not throw `StepFailedException`.
6. a caller that calls a sub test case **with** `try-catch` block; the sub test case does log failure, and does throw `StepFailedException`.

A long list of test cases. I wanted to make my test case code concise. I needed a custom keyword which enables me to express those cases in 1 line of code per each 6 cases above.

## Solution

## Description

### a primiteve keyword: fail


###
