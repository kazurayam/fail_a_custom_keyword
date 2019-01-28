import com.kms.katalon.core.model.FailureHandling as FailureHandling

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

CustomKeywords.'com.kazurayam.ksbackyard.PrimitiveKeywords.fail'(message, FailureHandling.STOP_ON_FAILURE)

WebUI.comment("callTestCase() failed and you well not see this message")