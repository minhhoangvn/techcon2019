import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.RestRequestObjectBuilder
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS


def builder = new RestRequestObjectBuilder()
RequestObject requestObject =  builder
.withRestRequestMethod("POST")
.withRestUrl("http://localhost:5000/calculate/subtraction")
.withHttpHeaders([
	new TestObjectProperty("Content-Type", ConditionType.EQUALS, "application/json")
])
.withTextBodyContent("{ \"number-a\": 2, \"number-b\": 1 }")
.build()

'Send a request'
ResponseObject response = WS.sendRequest(requestObject)

println response.getResponseText()