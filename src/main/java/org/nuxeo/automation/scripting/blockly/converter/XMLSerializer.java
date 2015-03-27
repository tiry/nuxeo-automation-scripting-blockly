/*
 * (C) Copyright 2006-20012 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Nuxeo - initial API and implementation
 *
 */

package org.nuxeo.automation.scripting.blockly.converter;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.nuxeo.ecm.automation.core.OperationChainContribution;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * {@link TemplateInput} parameters are stored in the {@link DocumentModel} as a single String Property via XML
 * Serialization. This class contains the Serialization/Deserialization logic.
 *
 * @author Tiry (tdelprat@nuxeo.com)
 */
public class XMLSerializer {

    protected static final Log log = LogFactory.getLog(XMLSerializer.class);

    public static final String XML_NAMESPACE = "http://www.w3.org/1999/xhtml";

    public static final String XML_NAMESPACE_PREFIX = "";

    public static final Namespace ns = new Namespace(XML_NAMESPACE_PREFIX, XML_NAMESPACE);

    public static final QName xmlTag = DocumentFactory.getInstance().createQName("xml", ns);

    public static final QName chainTag = DocumentFactory.getInstance().createQName("chain", ns);

    public static final QName blockTag = DocumentFactory.getInstance().createQName("block", ns);

    public static final QName placeHolderTag = DocumentFactory.getInstance().createQName("placeHolder", ns);

    public static final QName valueTag = DocumentFactory.getInstance().createQName("value", ns);

    public static final QName fieldTag = DocumentFactory.getInstance().createQName("field", ns);

    public static final QName nextTag = DocumentFactory.getInstance().createQName("next", ns);

    protected static AtomicInteger counter = new AtomicInteger();

    public static Element createRoot() {
        return DocumentFactory.getInstance().createElement(xmlTag);
    }

    public static Element createChainElement(String name) {
        Element e = DocumentFactory.getInstance().createElement(chainTag);
        e.addAttribute("name", name);
        return e;
    }

    public static Element createBlock(String type) {
        Element block =  DocumentFactory.getInstance().createElement(blockTag);

        block.addAttribute("type", type);
        block.addAttribute("id", counter.incrementAndGet()+"");
        block.addAttribute("inline", "false");

        return block;
    }

    public static Element createPlaceHolder(String target, String parameters, boolean loop, boolean allowPipe) {
        Element block =  DocumentFactory.getInstance().createElement(placeHolderTag);
        block.addAttribute("target", target);
        block.addAttribute("id", counter.incrementAndGet()+"");
        if (parameters!=null) {
            block.addAttribute("parameters", parameters);
        }
        block.addAttribute("allowPipe", Boolean.toString(allowPipe));
        return block;
    }

    public static Element createValueElement(Element e, String name) {
        Element value = e.addElement(valueTag);
        value.addAttribute("name", name);
        return value;
    }


    public static Element createFieldElement(Element e, String name) {
        Element value = e.addElement(fieldTag);
        value.addAttribute("name", name);
        return value;
    }

    public static Element createTextBlock(Element e, String value) {
        Element block = createBlock("Automation.TextCode");
        Element field = createFieldElement(block, "CODE");
        field.setText(value);
        e.add(block);
        return block;
    }

    public static Element createIntBlock(Element e, String value) {
        Element block = createBlock("math_number");
        Element field = createFieldElement(block, "NUM");
        field.setText(value);
        e.add(block);
        return block;
    }

    public static Element createSwallowBlock(Element e) {
        Element block = createBlock("Automation.SwallowOutput");
        Element input = createValueElement(block, "INPUT");
        input.add(e);
        return block;
    }

    public static Element pipeBlock(Element first, Element second) {
        if (first.element(nextTag)!=null) {
            throw new UnsupportedOperationException("Can only have one child!!!");
        }
        Element next = first.addElement(nextTag);
        next.add(second);
        return first;
    }

}
