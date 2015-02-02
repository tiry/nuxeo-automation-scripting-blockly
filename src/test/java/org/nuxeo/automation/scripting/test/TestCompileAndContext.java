package org.nuxeo.automation.scripting.test;

import java.io.InputStream;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.automation.scripting.AutomationScriptingService;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;
import org.nuxeo.runtime.test.runner.LocalDeploy;


@RunWith(FeaturesRunner.class)
@Features({ CoreFeature.class })
@Deploy({ "org.nuxeo.ecm.automation.core"})
@RepositoryConfig(cleanup = Granularity.METHOD)
@LocalDeploy({"org.nuxeo.ecm.automation.scripting:OSGI-INF/automation-scripting-service.xml"})
public class TestCompileAndContext {

    @Test
    public void testWithoutPreCompile() throws Exception {     
        AutomationScriptingService ass = Framework.getService(AutomationScriptingService.class);
        Assert.assertNotNull(ass);
        
        String jsWrapper = ass.getJSWrapper();
        Assert.assertNotNull(jsWrapper);

        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("Nashorn");
        Assert.assertNotNull(engine);
        
        engine.eval(jsWrapper);
        
        InputStream stream = this.getClass().getResourceAsStream("/checkWrapper.js");
        Assert.assertNotNull(stream);        
        engine.eval(IOUtils.toString(stream));
        
    }

    // http://comments.gmane.org/gmane.comp.java.openjdk.nashorn.devel/2905
    
    @Test
    public void testWithPreCompile() throws Exception {     
        AutomationScriptingService ass = Framework.getService(AutomationScriptingService.class);
        Assert.assertNotNull(ass);
        
        String jsWrapper = ass.getJSWrapper();
        Assert.assertNotNull(jsWrapper);

        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine engine = engineManager.getEngineByName("Nashorn");
        Assert.assertNotNull(engine);

        // Compile
        Compilable compilabe = (Compilable)engine;
        CompiledScript compiled = compilabe.compile(jsWrapper);
        compiled.eval(engine.getContext());
        
        // Extract Bindings
        Bindings b= engine.getBindings(ScriptContext.ENGINE_SCOPE);
        Assert.assertTrue(b.containsKey("Document"));
                
        // Run 1
        ScriptEngine engine2 = engineManager.getEngineByName("Nashorn");        
        engine2.setBindings(b, ScriptContext.ENGINE_SCOPE);
        InputStream stream = this.getClass().getResourceAsStream("/checkWrapper.js");
        Assert.assertNotNull(stream);   
        engine2.eval(IOUtils.toString(stream));
                
        // Run2
        ScriptEngine engine3 = engineManager.getEngineByName("Nashorn");  
        engine3.setContext(new ProtectedScriptingContext(b));
        //engine3.setBindings(b, ScriptContext.ENGINE_SCOPE);
        stream = this.getClass().getResourceAsStream("/checkWrapper.js");
        Assert.assertNotNull(stream);   
        engine3.eval(IOUtils.toString(stream), new ProtectedScriptingContext(b));

        // Try overwrite         
        stream = this.getClass().getResourceAsStream("/tryOverwrite.js");
        Assert.assertNotNull(stream);
        boolean forbidden = false;
        try {
            engine3.eval(IOUtils.toString(stream), new ProtectedScriptingContext(b));
        } catch (ScriptException se) {
            forbidden = true;            
        }
        //Assert.assertTrue(forbidden);
        stream = this.getClass().getResourceAsStream("/checkWrapper.js");
        Assert.assertNotNull(stream);   
        engine3.eval(IOUtils.toString(stream), new ProtectedScriptingContext(b));

        
        
        
    }

}
