import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

class ApplicationManager {
	private static final String COVERAGE_SUITE_NAME = "Test Suites/Get Test Coverage"
	private static testSuiteName = "";
	private static Process applicationProcess = null;
	private static StringWriter outStream = new StringWriter();
	private static List<String> generateTestImpactMappingCommand = [
		"/Users/minhhoang/virtualenvs/techcon2019/bin/python",
		"/Users/minhhoang/Desktop/techcon2019/demo-app/til.py"
	]
	private static List<String> startCollectCoverageCommamnd = [
		"/Users/minhhoang/virtualenvs/techcon2019/bin/python" ,
		"/Users/minhhoang/Desktop/techcon2019/demo-app/run.py"
	]
	private static List<String> stopCollectCoverageCommand = [
		"curl",
		"http://localhost:5000/shutdown"
	]
	private static List<String> generateCoverageCommand = [
		"/Users/minhhoang/virtualenvs/techcon2019/bin/coverage",
		"xml",
		"-o"
	]
	/**
	 * Executes before every test case starts.
	 * @param testCaseContext related information of the executed test case.
	 */
	@BeforeTestCase
	def sampleBeforeTestCase(TestCaseContext testCaseContext) {
		if (isCoverageTestSuite()) {
			println "Start application coverage command"
			startCollectCoverageCommamnd.join(" ").execute()
			delayForBackgroundProcess(5000)
		}
	}

	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def sampleAfterTestCase(TestCaseContext testCaseContext) {
		if (isCoverageTestSuite()) {
			println testCaseContext.getTestCaseId()
			String testCaseId = testCaseContext.getTestCaseId()
			testCaseId = testCaseId.replaceAll(" ", "_").replaceAll("/", "_").replaceAll("\\W", "_")
			println "Stop coverage command"
			stopCollectCoverageCommand.join(" ").execute()
			delayForBackgroundProcess(3000)
			println outStream.toString()
			println "Generate coverage command"
			generateCoverageCommand.add("./xmlcov/"+testCaseId+".xml")
			println generateCoverageCommand.join(" ")
			generateCoverageCommand.join(" ").execute()
			delayForBackgroundProcess(3000)
			println "Generate mapping impact coverage command"
			generateTestImpactMappingCommand.add("/Users/minhhoang/Desktop/techcon2019/test-impact-demo/xmlcov/${testCaseId}.xml")
			generateTestImpactMappingCommand.add("${testCaseContext.getTestCaseId().replaceAll(" ", "_")}")
			println generateTestImpactMappingCommand.join(" ")
			applicationProcess = generateTestImpactMappingCommand.join(" ").execute()
			applicationProcess.consumeProcessOutputStream(outStream)
			println (outStream.toString())
			generateTestImpactMappingCommand.remove("${testCaseContext.getTestCaseId().replaceAll(" ", "_")}")
			generateTestImpactMappingCommand.remove("/Users/minhhoang/Desktop/techcon2019/test-impact-demo/xmlcov/${testCaseId}.xml")
			generateCoverageCommand.remove("./xmlcov/"+testCaseId+".xml")
		}
	}
	/**
	 * Executes before every test suite starts.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@BeforeTestSuite
	def sampleBeforeTestSuite(TestSuiteContext testSuiteContext) {
		testSuiteName = testSuiteContext.getTestSuiteId()
	}

	/**
	 * Executes after every test suite ends.
	 * @param testSuiteContext: related information of the executed test suite.
	 */
	@AfterTestSuite
	def sampleAfterTestSuite(TestSuiteContext testSuiteContext) {
	}

	def delayForBackgroundProcess(time){
		Thread.sleep(time)
	}

	def isCoverageTestSuite(){
		return testSuiteName.toString().equalsIgnoreCase(COVERAGE_SUITE_NAME)
	}
}