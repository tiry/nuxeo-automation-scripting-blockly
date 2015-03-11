/*
 * (C) Copyright 2015 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 */
package org.nuxeo.automation.scripting.blockly.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.nuxeo.automation.scripting.blockly.BlocklyOperationWrapper;
import org.nuxeo.common.xmap.XMap;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationDocumentation.Param;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.ecm.automation.core.OperationChainContribution;
import org.nuxeo.ecm.automation.core.OperationChainContribution.Operation;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * @since TODO
 */
public class Chains2Blockly {

    protected static Log log = LogFactory.getLog(Chains2Blockly.class);

    public String convertXML(InputStream xmlChains) throws IOException {
        Element root = convert(xmlChains);
        OutputFormat format = OutputFormat.createPrettyPrint();
        StringWriter out = new StringWriter();
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(root);
        out.flush();
        return out.getBuffer().toString();
    }

    public Element convert(InputStream xmlChains) throws IOException {

        ChainsGroup chains = load(xmlChains);
        Element root = XMLSerializer.createRoot();

        for (OperationChainContribution chain : chains.getTopLevelChains()) {
            convertChain(root, chain, chains);
        }
        return root;
    }

    protected OperationBlock swallow(OperationBlock block) {
        Element e = XMLSerializer.createSwallowBlock(block.getBlock());
        return new OperationBlock(e, new BlocklyOperationWrapper());
    }

    protected OperationBlock pipeBlocks(OperationBlock first, OperationBlock second) {
        Element block = XMLSerializer.pipeBlock(first.getBlock(), second.getBlock());
        return new OperationBlock(block, first.getWrapper());
    }

    protected Element getInput(OperationBlock block) {
        for (Object e : block.getBlock().elements(XMLSerializer.valueTag)) {
            if (((Element) e).attribute("name").getValue().equals("INPUT")) {
                return (Element) e;
            }
        }
        return null;
    }

    protected class OpBlocks {

        final Element root;

        OperationBlock lastProcessed = null;

        OperationBlock lastResult = null;

        OperationBlock lastStacked = null;

        OpBlocks(Element root) {
            this.root = root;
        }
    }

    protected void storeOrstackBlock(OpBlocks state, OperationBlock block) {
        if (block.getWrapper().hasOutput()) {
            state.lastResult = block;
        } else {
            stackBlock(state, block);
        }
        state.lastProcessed = block;
    }

    protected void stackBlock(OpBlocks state, OperationBlock block) {
        if (state.lastStacked == null) {
            state.root.add(block.getBlock());
        } else {
            pipeBlocks(state.lastStacked, block);
        }
        state.lastStacked = block;
    }

    protected void convertChain(Element root2, OperationChainContribution chain, ChainsGroup chains) {

        OpBlocks state = new OpBlocks(root2);

        for (Operation op : chain.getOps()) {
            if (!skipOperation(op.getId())) {

                OperationBlock block = createOperationBlock(op);

                if (state.lastProcessed != null) {
                    if (state.lastResult != null) {
                        if (block.getWrapper().hasInput()) {
                            // plug output of last block on the current one
                            Element input = getInput(block);
                            Element previousParent = state.lastProcessed.getBlock().getParent();
                            // unplung if needed !?
                            if (previousParent != null) {
                                previousParent.remove(state.lastProcessed.getBlock());
                                input.add(state.lastProcessed.getBlock());
                                previousParent.add(block.getBlock());
                            } else {
                                input.add(state.lastProcessed.getBlock());
                            }
                            state.lastProcessed = block;
                            if (block.getWrapper().hasOutput()) {
                                state.lastResult = block;
                            } else {
                                state.lastResult = null;
                                stackBlock(state, block);
                            }
                        } else {
                            // we have a result but no-one to read it
                            // add swallow block
                            OperationBlock swallowBlock = swallow(state.lastResult);
                            // stack it
                            stackBlock(state, swallowBlock);

                            state.lastProcessed = swallowBlock;
                            state.lastResult = null;
                            storeOrstackBlock(state, block);
                            state.lastProcessed = block;
                        }
                    } else {
                        // no pending result
                        storeOrstackBlock(state, block);
                        state.lastProcessed = block;
                    }
                } else {
                    // init round
                    storeOrstackBlock(state, block);
                    state.lastProcessed = block;
                }
            }
        }

        if (state.lastResult != null) {
            OperationBlock swallowBlock = swallow(state.lastResult);
            stackBlock(state, swallowBlock);
        }
    }

    protected boolean skipOperation(String id) {
        if ("Context.FetchDocument".equals(id)) {
            return true;
        }
        return false;
    }

    protected OperationBlock createOperationBlock(Operation op) {

        Element block = XMLSerializer.createBlock(op.getId());
        OperationBlock opBlock = null;
        try {
            OperationType type = Framework.getService(AutomationService.class).getOperation(op.getId());

            BlocklyOperationWrapper wrapper = new BlocklyOperationWrapper(type);

            opBlock = new OperationBlock(block, wrapper);
            if (wrapper.hasInput()) {
                Element value = XMLSerializer.createValueElement(block, "INPUT");
                opBlock.setInput(value);
            }

            for (Param p : wrapper.getParams()) {
                OperationChainContribution.Param parameter = null;
                for (OperationChainContribution.Param opp : op.getParams()) {
                    if (opp.getName().equals(p.getName())) {
                        parameter = opp;
                        break;
                    }
                }
                if (parameter != null && parameter.getValue() != null && !parameter.getValue().isEmpty()) {
                    Element e = null;
                    if (p.getType().equalsIgnoreCase("boolean")) {
                        e = XMLSerializer.createFieldElement(block, p.getName());

                    } else if (p.getType().equalsIgnoreCase("integer")) {
                        e = XMLSerializer.createValueElement(block, p.getName());
                        XMLSerializer.createIntBlock(e, parameter.getValue());
                    } else {
                        e = XMLSerializer.createValueElement(block, p.getName());
                        XMLSerializer.createTextBlock(e, parameter.getValue());
                    }
                }
            }

        } catch (Exception e) {
            log.error("Error during conversion of " + op.getId(), e);
        }
        return opBlock;
    }

    protected ChainsGroup load(InputStream xmlChains) throws IOException {
        XMap xmap = new XMap();
        xmap.register(OperationChainContribution.class);
        Object[] contribs = xmap.loadAll(xmlChains);

        ChainsGroup chains = new ChainsGroup();
        for (Object chain : contribs) {
            chains.add((OperationChainContribution) chain);
        }
        return chains;
    }
}
