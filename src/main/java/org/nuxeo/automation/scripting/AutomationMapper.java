package org.nuxeo.automation.scripting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.util.DataModelProperties;
import org.nuxeo.ecm.automation.core.util.Properties;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.runtime.api.Framework;

public class AutomationMapper {

    protected final CoreSession session;

    public AutomationMapper(CoreSession session) {
        this.session = session;
    }

    public Object executeOperation(String opId, Object input, ScriptObjectMirror parameters) throws Exception {
        AutomationService as = Framework.getService(AutomationService.class);
        OperationContext ctx = new OperationContext(session);
        populateContext(ctx, input);
        Map<String, Object> params = unwrapParameters(parameters);
        return as.run(ctx, opId, params);
    }
    
    protected Map<String, Object> unwrapParameters(ScriptObjectMirror parameters) {
        Map<String, Object> params = new HashMap<String, Object>();
        for (String k : parameters.keySet()) {
            Object value = parameters.get(k);
            if (value instanceof ScriptObjectMirror) {                
                ScriptObjectMirror jso = (ScriptObjectMirror) value;                
                if (jso.isArray()) {
                    params.put( k, MarshalingHelper.unwrap(jso));
                } else {
                    params.put( k, extractProperties(jso));    
                }                                
            } else {
                if (value != null) {
                    params.put((String) k, value.toString());
                } else {
                    params.put((String) k, null);
                }
            }
        }
        return params;
    }

    
    protected void populateContext(OperationContext ctx, Object input) {

        if (input instanceof String) {
            ctx.setInput((String) input);
        } else if (input instanceof DocumentModel) {
            ctx.setInput((DocumentModel) input);
        } else if (input instanceof DocumentRef) {
            ctx.setInput((DocumentRef) input);
        } else if (input instanceof Blob) {
            ctx.setInput((Blob) input);
        } else if (input instanceof ScriptObjectMirror) {
            ctx.setInput(extractProperties((ScriptObjectMirror) input));
        }
    }

    protected Properties extractProperties(ScriptObjectMirror parameters) {
        DataModelProperties props = new DataModelProperties();
        Map<String, Object> data = MarshalingHelper.unwrapMap(parameters);
        for (String k : data.keySet()) {
            props.getMap().put(k, (Serializable) data.get(k));
        }
        return props;
    }
}
