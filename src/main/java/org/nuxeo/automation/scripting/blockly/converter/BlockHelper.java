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

import org.dom4j.Element;
import org.dom4j.dom.DOMNodeHelper;

import com.sun.star.ucb.UnsupportedOpenModeException;

/**
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * @since TODO
 */
public class BlockHelper {


    public static boolean isSwallowBlock(Element e) {
        return "Automation.SwallowOutput".equals(e.attributeValue("type"));
    }

    public static Element getInputElement(Element block) {
        for (Object o  : block.elements("value")) {
            if ("INPUT".equals(((Element)o).attributeValue("name"))) {
                return (Element)o;
            }
        }
        return null;
    }

    public static Element getInputElementValue(Element block) {
        Element input = getInputElement(block);
        if (input !=null) {
            return (Element) input.elements().get(0);
        } else {
            return null;
        }
    }

    public static Element getPreviousBlock(Element block) {

        if(!"block".equals(block.getName()) && !"placeHolder".equals(block.getName())) {
            throw new UnsupportedOperationException(block.getName() + " is not a block!");
        }

        Element parentBlock = block.getParent();
        if ("next".equals(parentBlock.getName())){
            return parentBlock.getParent();
        }

        return (Element) DOMNodeHelper.getPreviousSibling(block);
    }

}
