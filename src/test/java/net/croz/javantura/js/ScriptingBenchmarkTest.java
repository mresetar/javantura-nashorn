package net.croz.javantura.js;

import static org.junit.Assert.assertEquals;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;

@AxisRange(min = 0, max = 5)
@BenchmarkMethodChart(filePrefix = "benchmark-fibonnaci")
@BenchmarkOptions(callgc = false, benchmarkRounds = 10, warmupRounds = 3)
@BenchmarkHistoryChart(labelWith = LabelType.CUSTOM_KEY, maxRuns = 2)
public class ScriptingBenchmarkTest {
	private static final int COMPUTE_FIBONACCI_OF = 30;
	private static final int FIBONNACI_RESULT = 1346269;//14930352;
	
	@Rule
	public TestRule benchmarkRun = new BenchmarkRule();

	@Test
	public void testFibJavaScript() throws ScriptException, NoSuchMethodException {
		String fiboJs = "function fibo(n) { return n < 2 ? 1 : fibo(n-2) + fibo(n-1)}";
		Object result = invokeScriptingMethod(fiboJs, "js", COMPUTE_FIBONACCI_OF);
		if (result instanceof Double) {
			result = ((Double) result).intValue();
		}
		assertEquals(FIBONNACI_RESULT, result);
	}

	@Test
	public void testFibGroovy() throws ScriptException, NoSuchMethodException {
		String fiboJs = "def fibo(n) { n < 2 ? 1 : fibo(n-2) + fibo(n-1) }";
		Object result = invokeScriptingMethod(fiboJs, "groovy", COMPUTE_FIBONACCI_OF);
		assertEquals(FIBONNACI_RESULT, result);
	}

	@Test
	public void testFibJava() {
		int result = fibo(COMPUTE_FIBONACCI_OF);
		assertEquals(FIBONNACI_RESULT, result);

	}

	private Object invokeScriptingMethod(String fact, String engineName,
			Object... params) throws ScriptException, NoSuchMethodException {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName(engineName);
		engine.eval(fact);
		Invocable inv = (Invocable) engine;
		Object result = inv.invokeFunction("fibo", params);
		return result;
	}

	private static int fibo(int n) {
		return n < 2 ? 1 : (fibo(n - 2) + fibo(n - 1));
	}
}
