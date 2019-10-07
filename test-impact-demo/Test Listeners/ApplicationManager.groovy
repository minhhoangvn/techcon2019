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
		"/Users/minhhoang/Desktop/techcon2019/demo-app/til.py",
		"/Users/minhhoang/Desktop/techcon2019/test-impact-demo/xmlcov"
	]
	private static List<String> startCollectCoverageCommamnd = [
		"/Users/minhhoang/virtualenvs/techcon2019/bin/coverage" ,
		"run" ,
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
			applicationProcess = startCollectCoverageCommamnd.join(" ").execute()
			delayForBackgroundProcess(3000)
			applicationProcess.consumeProcessOutputStream(outStream)
		}
	}

	/**
	 * Executes after every test case ends.
	 * @param testCaseContext related information of the executed test case.
	 */
	@AfterTestCase
	def sampleAfterTestCase(TestCaseContext testCaseContext) {
		if (isCoverageTestSuite()) {
			String testCaseId = testCaseContext.getTestCaseId()
			testCaseId = testCaseId.replaceAll(" ", "_").replaceAll("/", "_").replaceAll("\\W", "_")
			applicationProcess = stopCollectCoverageCommand.join(" ").execute()
			applicationProcess.consumeProcessOutputStream(outStream)
			println outStream.toString()
			generateCoverageCommand.add('./xmlcov/'+testCaseId+".xml")
			delayForBackgroundProcess(3000)
			applicationProcess = generateCoverageCommand.join(" ").execute()
			applicationProcess.consumeProcessOutputStream(outStream)
			delayForBackgroundProcess(1000)
			println outStream.toString()
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
		applicationProcess = generateTestImpactMappingCommand.join(" ").execute()
		applicationProcess.consumeProcessOutputStream(outStream)
		delayForBackgroundProcess(1000)
		println outStream.toString()
	}

	def delayForBackgroundProcess(time){
		Thread.sleep(time)
	}

	def isCoverageTestSuite(){
		return testSuiteName.toString().equalsIgnoreCase(COVERAGE_SUITE_NAME)
	}
}