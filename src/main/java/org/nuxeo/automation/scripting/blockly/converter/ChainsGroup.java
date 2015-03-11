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

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.ecm.automation.core.OperationChainContribution;
import org.nuxeo.ecm.automation.core.OperationChainContribution.Operation;

/**
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * @since TODO
 */

public class ChainsGroup {

    protected List<String> neededOperationNames = new ArrayList<String>();
    protected List<String> chainNames = new ArrayList<String>();
    protected List<OperationChainContribution> chains = new ArrayList<OperationChainContribution>();

    public void add (OperationChainContribution chain) {
        chains.add(chain);
        chainNames.add(chain.getId());
        for (Operation op :  chain.getOps()) {
            if (!neededOperationNames.contains(op.getId())) {
                neededOperationNames.add(op.getId());
            }
        }
    }

    public List<String> getNeededOperationNames() {
        return neededOperationNames;
    }

    public List<String> getChainNames() {
        return chainNames;
    }

    public List<OperationChainContribution> getAllChains() {
        return chains;
    }

    public List<OperationChainContribution> getTopLevelChains() {
        List<OperationChainContribution> result =  new ArrayList<OperationChainContribution>();
        for (OperationChainContribution chain : chains) {
            if (!neededOperationNames.contains(chain.getId())) {
                result.add(chain);
            }
        }
        return result;
    }

    public OperationChainContribution getChain(String id) {
        for (OperationChainContribution chain : chains) {
            if (chain.getId().equals(id)) {
                return chain;
            }
        }
        return null;
    }




}
