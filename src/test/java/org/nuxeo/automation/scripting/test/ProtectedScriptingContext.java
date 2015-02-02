package org.nuxeo.automation.scripting.test;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleScriptContext;

public class ProtectedScriptingContext extends SimpleScriptContext implements ScriptContext {

    protected final Bindings rootBinding;
    
     
    
    public ProtectedScriptingContext(Bindings rootBinding) {
        super();
        this.rootBinding = rootBinding;        
        super.setBindings(rootBinding, ScriptContext.ENGINE_SCOPE);
    }    
    
    @Override
    public Object removeAttribute(String name, int scope) {
        if (scope == ScriptContext.ENGINE_SCOPE) {
            return null;
        }
        return super.removeAttribute(name, scope);
    }

    @Override
    public void setAttribute(String name, Object value, int scope) {
        if (name.startsWith("Document")) {
            System.out.println("Overrwrite");
        }
        if (scope == ScriptContext.ENGINE_SCOPE) {
            return;
        }
        super.setAttribute(name, value,scope);
    }

    @Override
    public void setBindings(Bindings binding, int scope) {
        if (scope == ScriptContext.ENGINE_SCOPE) {
            return;
        }
        super.setBindings(binding,scope);
    }

    
}
