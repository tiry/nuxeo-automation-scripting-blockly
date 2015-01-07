package org.nuxeo.automation.scripting.operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.nuxeo.automation.scripting.AutomationScriptingService;
import org.nuxeo.automation.scripting.ScriptRunner;
import org.nuxeo.automation.scripting.ScriptableMap;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.impl.DocumentModelListImpl;
import org.nuxeo.runtime.api.Framework;

import sun.org.mozilla.javascript.NativeArray;
import sun.org.mozilla.javascript.NativeObject;

public class ScriptingOperationImpl {

    protected final ScriptRunner runner;    
    protected final OperationContext ctx;
    protected final Map<String, Object> args;
    protected final String source;
            
    public ScriptingOperationImpl(String source, OperationContext ctx, Map<String, Object> args) {    
        AutomationScriptingService ass = Framework.getService(AutomationScriptingService.class);        
        runner = ass.getRunner();
        runner.setCoreSession(ctx.getCoreSession());
        this.ctx=ctx;
        this.args=args;
        this.source=source;
    }
 
    public Object run(Object input) throws Exception {    
        try {
            ScriptingOperationInterface itf = runner.getInterface(ScriptingOperationInterface.class, source);
            return wrapResult(itf.run(wrap(ctx), input, wrap(args)));
        } 
        catch (ScriptException e) {
            throw new OperationException(e);
        }
        
    }
    
    protected Object wrapResult(Object res) {
        if (res==null) {
            return null;
        }
        if (res instanceof NativeArray) {
            NativeArray na = (NativeArray) res;            
            Object[] array = na.toArray();
            if (array.length==0) {
                return new ArrayList<>();
            } else {
                List<Object> wraped = new ArrayList<>();
                DocumentModelList docs = new DocumentModelListImpl();
                for (int i =0 ; i < array.length; i++) {
                    Object val = na.get(i);
                    if (val instanceof DocumentModel) {
                        docs.add((DocumentModel)val);
                    }
                    wraped.add(wrapResult(val));
                }
                if (docs.size()==wraped.size()) {
                    return docs;
                } else {
                    return wraped;    
                }                
            }
            
        } else if (res instanceof NativeObject) {
            NativeObject no = (NativeObject) res;
            Map<Object, Object> wraped = new HashMap<Object, Object>();
            
            for (Object key : no.keySet()) {
                wraped.put(key, wrapResult(no.get(key)));
            }
            return wraped;
            
        } else {
            return res;
        }
    }
    
    protected ScriptableMap wrap(OperationContext ctx) {
        return wrap(ctx.getVars());
    }
    
    protected ScriptableMap wrap(Map<String, Object> vars) {
        return new ScriptableMap(vars);
    }

    protected NativeObject wrap2(OperationContext ctx) {
        return wrap2(ctx.getVars());
    }

    
    protected NativeObject wrap2(Map<String, Object> vars) {        
        NativeObject no = new NativeObject();        
        for (String k : vars.keySet()) {           
            no.defineProperty(k, vars.get(k), NativeObject.READONLY);
        }
        return no;
    }

}
