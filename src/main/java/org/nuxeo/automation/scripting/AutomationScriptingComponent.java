package org.nuxeo.automation.scripting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngineManager;

import org.nuxeo.automation.scripting.operation.ScriptingOperationDescriptor;
import org.nuxeo.automation.scripting.operation.ScriptingTypeImpl;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class AutomationScriptingComponent extends DefaultComponent implements AutomationScriptingService {

    protected ScriptEngineManager engineManager;   
    protected int nbFunctions = 0;
    protected String jsWrapper=null;
    
    public static final String EP_OP = "operation";
    
    @Override
    public void activate(ComponentContext context) throws Exception {
        super.activate(context);
        engineManager = new ScriptEngineManager();
    }
    
    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor)
            throws Exception {

        if (EP_OP.equals(extensionPoint)) {
            ScriptingOperationDescriptor desc = (ScriptingOperationDescriptor) contribution;
            AutomationService as =Framework.getService(AutomationService.class);
            ScriptingTypeImpl type = new ScriptingTypeImpl(as, desc);            
            as.putOperation(type, true);
        } else {        
            super.registerContribution(contribution, extensionPoint, contributor);
        }
    }

    public String getJSWrapper() {
        return getJSWrapper(false);
    }
    
    
    
    public synchronized String getJSWrapper(boolean refresh) {
        
        if (jsWrapper==null || refresh) {
            nbFunctions=0;
            StringBuffer sb = new StringBuffer();
            
            AutomationService as = Framework.getService(AutomationService.class);            
            
            Map<String, List<OperationType>> opMap = new HashMap<String, List<OperationType>>();
            List<OperationType> flatOps = new ArrayList<>();
            
            for (OperationType op : as.getOperations()) {
                String id = op.getId();
                int idx = id.indexOf(".");
                if (idx > 0 ) {
                    String obName = id.substring(0, idx);                    
                    List<OperationType> ops = opMap.get(obName);
                    if (ops==null) {
                        ops = new ArrayList<>();
                    }
                    ops.add(op);
                    opMap.put(obName, ops);
                } else {
                    flatOps.add(op);
                }
            }
            
            for (String obName : opMap.keySet()) {
                List<OperationType> ops = opMap.get(obName);
                sb.append("\nvar " + obName + "={};");
                
                for (OperationType op : ops) {
                    generateFunction(sb, op);
                }
            }
            for (OperationType op : flatOps) {
                generateFunction(sb, op);
            }

            jsWrapper = sb.toString();
                        
        }
        return jsWrapper;
    }   
    
    protected void generateFunction(StringBuffer sb, OperationType op) {            
        sb.append("\n" + op.getId() + " = function(input,params) {");                
        sb.append("\nreturn automation.executeOperation('" + op.getId() + "', input , params);");         
        sb.append("\n};");
        nbFunctions++;
    }

    public ScriptRunner getRunner(CoreSession session) {        
        ScriptRunner runner = new ScriptRunner(engineManager,getJSWrapper());
        runner.setCoreSession(session);
        return runner;        
    }

    public ScriptRunner getRunner() {        
        ScriptRunner runner = new ScriptRunner(engineManager,getJSWrapper());
        return runner;        
    }

}
