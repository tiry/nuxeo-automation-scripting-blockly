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
package org.nuxeo.automation.scripting.blockly.test;

import java.io.InputStream;

import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.automation.scripting.blockly.converter.Chains2Blockly;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * @since TODO
 */

@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
public class TestConversion {

    protected InputStream getXml(String name) {
        InputStream xmlStream = this.getClass().getResourceAsStream(name);
        assertNotNull(xmlStream);
        return xmlStream;
    }

    @Test
    public void testMergeNested() throws Exception{

        Chains2Blockly converter = new Chains2Blockly();
        Element tree = converter.convert(getXml("/simpleNestedCall.xml"));

        // check that the 2 nested chains where merged
        assertEquals(1, tree.elements().size());

        // check that we have 2 level nesting
        Element next = tree.element("block").element("next");
        assertNotNull(next);
        Element next2 = next.element("block").element("next");
        assertNotNull(next2);

        String xml = converter.convertXML(getXml("/simpleNestedCall.xml"));
        assertFalse(xml.contains("Nested1"));
        assertFalse(xml.contains("Nested2"));

    }

    @Test
    public void testNotMergeNested() throws Exception{

        Chains2Blockly.Config config  = new Chains2Blockly.Config();
        config.setMergeSubChains(false);
        Chains2Blockly converter = new Chains2Blockly(config);
        Element tree = converter.convert(getXml("/simpleNestedCall.xml"));
        assertEquals(3, tree.elements().size());

        String xml = converter.convertXML(getXml("/simpleNestedCall.xml"));
        assertTrue(xml.contains("Nested1"));
        assertTrue(xml.contains("Nested2"));

    }

}
