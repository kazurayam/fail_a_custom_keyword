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

I made a *main* test case like this:
```
...
```

And I a *sub* test case named `sub - STOP_ON_FAILURE`, which is called the *main*, like this:
```
...
```

The `fail` keyword is my custom keyword. I will describe it later.

The *main* test case is expected to pass. Even if the *sub* test case failed and threw StepFailedException, the `try` block in the *main* is expected to catch it.

However the [issue](https://github.com/katalon-studio/katalon-studio/issues/79) reports that KS 5.10.1 will actually behave different. The *main* test case fails in KS 5.10.1. Even if the *sub* test case failed and threw StepFailedException, the `try` block in the *main* will FAIL to catch it in KS 5.10.1.

###

## Solution

## Description
