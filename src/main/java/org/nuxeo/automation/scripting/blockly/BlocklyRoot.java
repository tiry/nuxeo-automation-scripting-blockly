/*
 * (C) Copyright ${year} Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     dmetzler
 */
       
package org.nuxeo.automation.scripting.blockly;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

@Path("/automationscripting/blockly")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "Blockly")
public class BlocklyRoot extends ModuleRoot {


    @GET
    @Path("/")
    @Produces({ MediaType.TEXT_HTML})           
    public Object getEditor() {
        return getView("editor");
    }

    @GET
    @Path("/blocklyFrame")
    @Produces({ MediaType.TEXT_HTML})           
    public Object getBlocklyFrame() {
        return getView("blocklyFrame");
    }    
    
    public List<String> getCategories() throws OperationException {
        List<String> categories = new ArrayList<>();
        AutomationService as = Framework.getService(AutomationService.class);
        
        for (OperationType opType : as.getOperations()) {
            try {
                if (!categories.contains(opType.getDocumentation().getCategory())) {
                    categories.add(opType.getDocumentation().getCategory());
                }
            } catch (OperationException e) {
                System.out.println("Exeception on OP " + opType.getClass().getSimpleName());
            }
        }
        return categories;
    }
    
    public List<String> getOperationIdsForCategory(String cat)  {
        List<String> ids = new ArrayList<>();
        AutomationService as = Framework.getService(AutomationService.class);
        
        for (OperationType opType : as.getOperations()) {
            try {
                if (cat.equals(opType.getDocumentation().getCategory())) {
                    ids.add(opType.getId());
                }
            } catch (OperationException e) {
                System.out.println("Exeception on OP " + opType.getClass().getSimpleName());
            }
        }
        return ids;        
    }
    
    @GET
    @Path("/blocks")
    @Produces({ "text/javascript"})           
    public Object getBlocks() {
        AutomationService as = Framework.getService(AutomationService.class);
        
        List<OperationType> ops = new ArrayList<>();
        for (OperationType opType : as.getOperations()) {
            try {
                opType.getDocumentation();
                ops.add(opType);
            } catch (OperationException e) {
                System.out.println("Exeception on OP " + opType.getClass().getSimpleName());
            }
        }        
        return getView("automationBlocks").arg("operations", ops);
    }
    
}
