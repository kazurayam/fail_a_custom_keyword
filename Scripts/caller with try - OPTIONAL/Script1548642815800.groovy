import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

try {
	WebUI.callTestCase(findTestCase('sub - OPTIONAL'),
		["message":">>> Intentional failure. Caller can safely ignore this"])
	
	WebUI.comment(">>> callTestCase() passed")
} catch (Exception ex) {
	WebUI.comment(">>> caught an Exception: " + ex.getMessage())
}