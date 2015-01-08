package org.nuxeo.automation.scripting.operation;

import org.nuxeo.automation.scripting.ScriptableMap;

public interface ScriptingOperationInterface {

    Object run(ScriptableMap ctx, Object input, ScriptableMap parameters);

}
