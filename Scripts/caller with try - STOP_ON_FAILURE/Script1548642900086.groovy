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