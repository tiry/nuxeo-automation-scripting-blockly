package org.nuxeo.automation.scripting.blockly;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.automation.OperationDocumentation;
import org.nuxeo.ecm.automation.OperationDocumentation.Param;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.ecm.automation.core.impl.InvokableMethod;
import org.nuxeo.ecm.core.api.DocumentModelList;

public class BlocklyOperationWrapper {

    protected final String category;

    protected final String label;

    protected final OperationDocumentation doc;

    protected final OperationType type;

    protected final Param[] params;

    public BlocklyOperationWrapper(OperationType type) throws OperationException {
        this.type = type;
        doc = type.getDocumentation();
        category = doc.getCategory();
        label = doc.getLabel();
        params = doc.getParams();
    }

    public boolean hasInput() {
        for (InvokableMethod meth : type.getMethods()) {
            if (!meth.getInputType().equals(Void.TYPE)) {
                return true;
            }
        }
        return false;
    }
    
    public String getInputTypes() {
        List<String> types = new ArrayList<>();
        for (InvokableMethod meth : type.getMethods()) {
            if (!meth.getInputType().equals(Void.TYPE)) {
                if (!types.contains(meth.getInputType().getSimpleName())) {
                    types.add(meth.getInputType().getSimpleName());
                }
            }
        }
        if (types.size()==1) {            
            return "'" + types.get(0) + "'";
        } else if (types.size()>1) {
            StringBuffer sb = new StringBuffer("[");
            for (int i = 0; i < types.size(); i++) {
                sb.append("'" + types.get(i));
                sb.append("'");
                if (i < types.size()-1) {
                    sb.append(",");
                }                
            }
            sb.append("]");
            return sb.toString();            
        }
        return null;
    }    

    public boolean hasOutput() {
        for (InvokableMethod meth : type.getMethods()) {
            if (!meth.getOutputType().equals(Void.TYPE)) {
                return true;
            }
        }
        return false;
    }

    public String getOutputType() {
        List<String> types = new ArrayList<>();
        for (InvokableMethod meth : type.getMethods()) {
            if (!meth.getOutputType().equals(Void.TYPE) ) {
                if (!types.contains(meth.getOutputType().getSimpleName())) {
                    types.add(getOutputTypeMapping(meth.getOutputType().getSimpleName()));
                }
            }
        }
        if (types.size()==1) {
            return types.get(0);
        }
        return null;
    }    

    protected String getOutputTypeMapping(String type) {
        if (DocumentModelList.class.getSimpleName().equals(type)) {
            return "Array";
        }
        return type;
    }
    
    public String getId() {
        return type.getId();
    }

    public String getCategory() {
        return category;
    }

    public String getLabel() {
        return label;
    }

    public OperationDocumentation getDoc() {
        return doc;
    }

    public OperationType getType() {
        return type;
    }

    public Param[] getParams() {
        return params;
    }

}
