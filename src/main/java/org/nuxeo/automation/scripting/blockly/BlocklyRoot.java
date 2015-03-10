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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.ecm.platform.web.common.ServletHelper;
import org.nuxeo.ecm.webengine.forms.FormData;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.ResourceType;
import org.nuxeo.ecm.webengine.model.WebContext;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

@Path("/automationscripting/blockly")
@Produces("text/html;charset=UTF-8")
@WebObject(type = "Blockly")
public class BlocklyRoot extends ModuleRoot {

    protected AutomationHelper helper = new AutomationHelper();

    protected String getFilter() {
        return getContext().getRequest().getParameter("filter");
    }

    @GET
    @Path("/")
    @Produces({ MediaType.TEXT_HTML})
    public Object getEditor(@QueryParam("sample") String sample) throws IOException {;
        if (sample!=null && sample.endsWith(".xml")) {
            InputStream is = this.getClass().getResourceAsStream("/" + sample);
            if (is!=null) {
                String xml = IOUtils.toString(is, "UTF-8");
                return getView("editor").arg("xml",xml);
            }
        }
        return getView("editor");
    }

    @GET
    @Path("/blocklyFrame")
    @Produces({ MediaType.TEXT_HTML})
    public Object getBlocklyFrame() {
        return getView("blocklyFrame");
    }

    public List<String> getCategories() {
        return helper.getCategories(getFilter());
    }

    public List<String> getOperationIdsForCategory(String cat)  {
        return helper.getOperationIdsForCategory(cat, getFilter());
    }

    @GET
    @Path("/blocks")
    @Produces({ "text/javascript"})
    public Object getBlocks() {
        AutomationService as = Framework.getService(AutomationService.class);

        List<BlocklyOperationWrapper> ops = new ArrayList<>();
        for (OperationType opType : as.getOperations()) {
            try {
                opType.getDocumentation();
                ops.add(new BlocklyOperationWrapper(opType));
            } catch (OperationException e) {
                System.out.println("Exeception on OP " + opType.getClass().getSimpleName());
            }
        }
        return getView("automationBlocks").arg("operations", ops);
    }

    @GET
    @Path("/toolbox")
    @Produces({ "text/xml"})
    public Object getToolbox() {
        return getView("toolbox");
    }

    @POST
    @Path("/save")
    public Response save() {
        FormData form = getContext().getForm();
        String xmlFileName = form.getString("xmlFileName");
        String xml = form.getString("xml");
        String contentDisposition = ServletHelper.getRFC2231ContentDisposition(
                ctx.getRequest(), xmlFileName);
        Blob blob = new StringBlob(xml);
        ResponseBuilder builder = Response.ok(blob).header(
                "Content-Disposition", contentDisposition).type(
                blob.getMimeType());
        return builder.build();
    }


    @POST
    @Path("/")
    public Object load() throws IOException {
        FormData form = getContext().getForm();
        Blob blob = form.getBlob("xmlFile");
        return getView("editor").arg("xml",blob.getString());
    }

    @POST
    @Path("/downloadJS")
    public Response downloadJS() {
        FormData form = getContext().getForm();
        String jsFileName = form.getString("jsFileName");
        String js = form.getString("js");
        String contentDisposition = ServletHelper.getRFC2231ContentDisposition(
                ctx.getRequest(), jsFileName);
        Blob blob = new StringBlob(js);
        ResponseBuilder builder = Response.ok(blob).header(
                "Content-Disposition", contentDisposition).type(
                blob.getMimeType());
        return builder.build();
    }



}
