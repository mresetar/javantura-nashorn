package net.croz.javantura.js;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class QUnitTest {
	private ScriptEngine engine;
	public static final Logger log = LoggerFactory.getLogger(QUnitTest.class);
	
	@Before
	public void setup() throws IOException, ScriptException {
		final File qunitFile = new ClassPathResource("static/js/qunit-1.14.0.js").getFile();
		final String qUnitSetupJs = 
				"var log = Java.type('net.croz.javantura.js.QUnitTest').log;"
				+ "QUnit.init();"
				+ "QUnit.config.blocking = false;"
				+ "QUnit.config.autorun = true;"
				+ "QUnit.config.updateRate = 0;"
				+ "QUnit.log = function(result, message) {"
				+ "		if(result.result) {"
				//+ "			print('PASS ' + result.message); "
				+ "			log.debug('PASS ' + result.message);"
				+ "		} "
				+ "		else {"
				+ "			log.error('FAIL ' + result.message);"
				+ "			org.junit.Assert.fail(result.message);"
				+ "		}"
				//+ "		print(result.result ? 'PASS' : 'FAIL', result.message)"
				+ "};";
		
		final ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("nashorn");
		engine.eval(new FileReader(qunitFile));
		engine.eval(qUnitSetupJs);
	}
	
	@Test
	public void testQUnitWorking() throws ScriptException {
		engine.eval("test('QUnit is working', function(){expect(0)});");
	}
	
	@Test
	public void testQUnitTestNewAdditionFun() throws FileNotFoundException, ScriptException {
		final String newAdditionFun = 
				"function newAddition(x, y) {"
				+ "		return x + y;"
				+ "}";
		final String newAdditionFunTest = 
				"test('Adding numbers works', function() {"
						+ "expect(3);"  
						+ "ok(newAddition, 'function exists');" 
						+ "equal(4, newAddition(2, 2), '2 + 2 = 4');" 
						+ "equal(100, newAddition(100, 0), 'zero is zero');}"
						+ ");";
		engine.eval(newAdditionFun);
		engine.eval(newAdditionFunTest);
	}

}
