import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.callTestCase(findTestCase('sub - OPTIONAL'), ["message":">>> Intentional failure. Caller can safely ignore this"])

WebUI.comment(">>> callTestCase() failed but we ignored it as FailureHandling.OPTIONAL specfied")