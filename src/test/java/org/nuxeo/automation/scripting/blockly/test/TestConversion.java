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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.automation.scripting.blockly.converter.Chains2Blockly;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import junit.framework.Assert;

/**
 *
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * @since TODO
 */

@RunWith(FeaturesRunner.class)
@Features({ AutomationFeature.class })
public class TestConversion {

    @Test
    public void testConvert() throws Exception{

        InputStream xmlStream = this.getClass().getResourceAsStream("/chains.xml");
        Assert.assertNotNull(xmlStream);

        Chains2Blockly converter = new Chains2Blockly();
        String xml = converter.convertXML(xmlStream);

        System.out.println(xml);



    }

}
