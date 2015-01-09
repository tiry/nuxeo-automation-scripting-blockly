package org.nuxeo.automation.scripting.blockly;

import org.nuxeo.ecm.automation.OperationDocumentation;
import org.nuxeo.ecm.automation.OperationDocumentation.Param;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.ecm.automation.core.impl.InvokableMethod;

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

    public boolean hasOutput() {
        for (InvokableMethod meth : type.getMethods()) {
            if (!meth.getOutputType().equals(Void.TYPE)) {
                return true;
            }
        }
        return false;
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
