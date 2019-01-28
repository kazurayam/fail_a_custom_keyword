import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

WebUI.callTestCase(findTestCase('sub - STOP_ON_FAILURE'),
	["message":">>> Intentional failure. Caller should stop immediately"])

WebUI.comment(">>> callTestCase() failed, and you will not seed this message as FailureHandling.STOP_ON_FALURE specified")