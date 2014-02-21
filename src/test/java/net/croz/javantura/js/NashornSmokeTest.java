package net.croz.javantura.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

public class NashornSmokeTest {

	@Test
	public void smokeTestNashorn() {
		final ScriptEngineManager manager = new ScriptEngineManager();
		final ScriptEngine engine = manager.getEngineByName("nashorn");
		try {
			engine.eval("function hello(who) { return 'Hello ' + who }");
			final String returnValue = (String) engine.eval("hello('world')");
			Assert.assertEquals("Hello world", returnValue);
		} catch (ScriptException se) {
			se.printStackTrace();
			Assert.fail("Nashorn misbehaved.");
		}
	}

}