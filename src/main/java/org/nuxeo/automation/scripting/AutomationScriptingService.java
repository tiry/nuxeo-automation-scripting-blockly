package org.nuxeo.automation.scripting;

import org.nuxeo.ecm.core.api.CoreSession;

public interface AutomationScriptingService {

    String getJSWrapper();
    
    String getJSWrapper(boolean refresh);
    
    ScriptRunner getRunner(CoreSession session);
    
    ScriptRunner getRunner();
}

