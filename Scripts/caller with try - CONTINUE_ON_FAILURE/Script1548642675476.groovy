import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling

try {
	WebUI.callTestCase(findTestCase('sub - CONTINUE_ON_FAILURE'),
		["message":">>> Intentional failure. Caller can continue"])
	
	WebUI.comment(">>> callTestCase() failed")
	WebUI.comment(">>> you will see this message")
} catch (Exception ex) {
	WebUI.comment(">>> caught an Exception: " + ex.getMessage())
}