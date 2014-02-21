/**
 * 
 */
package net.croz.javantura.js;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import sample.ui.Message;

/**
 * @author Miroslav
 *
 */
public class MustacheTest {
	public static final Logger log = LoggerFactory.getLogger(MustacheTest.class);
	
	private ScriptEngine engine;
	private Object mustache; 
	
	@Before
	public void setupMustache() throws IOException, ScriptException {
		final ScriptEngineManager engineManager = new ScriptEngineManager();
		engine = engineManager.getEngineByName("nashorn");
		final File mustachejsFile = new ClassPathResource("static/js/mustache.js").getFile();
		engine.eval(new FileReader(mustachejsFile));
		mustache = engine.eval("Mustache");
	}
	
	@Test
	public void testMustacheHelloWorld() throws NoSuchMethodException, ScriptException {
		final String template = "Hello {{world}}";
		Map<String, String> data = new HashMap<String, String>();
		data.put("world", "Javantura");
		final String result = (String) ((Invocable) engine).invokeMethod(mustache, "render", template, data);
		Assert.assertEquals("Hello Javantura", result);
	}
	
	@Test
	public void testView() throws IOException, NoSuchMethodException, ScriptException, ParseException {
		final ClassPathResource view = new ClassPathResource("static/js/messageAsMustacheTemplate.html");
		final String template = FileUtils.readFileToString(view.getFile());
		final Message message = new Message();
		message.setCreated(new GregorianCalendar());
		final Date date = new SimpleDateFormat("dd.MM.yyyy").parse("22.2.2014");
		message.getCreated().setTime(date);
		message.setSummary("Some message");
		message.setText("Some long text");
		final String result = (String) ((Invocable) engine).invokeMethod(mustache, "render", template, message);
		final ClassPathResource testOutput = new ClassPathResource("static/html/mustacheOutput.html");
		final String expected = FileUtils.readFileToString(testOutput.getFile());
		Assert.assertEquals(expected, result);
		log.debug(result);
	}
	
}
