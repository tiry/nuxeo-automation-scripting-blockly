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
package org.nuxeo.automation.scripting.blockly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.OperationType;
import org.nuxeo.runtime.api.Framework;

/**
 * @author <a href="mailto:tdelprat@nuxeo.com">Tiry</a>
 * @since TODO
 */
public class AutomationHelper {

    protected static Map<String, List<String>> opByCat = null;

    private static final String MISC = "Misc";

    protected void initIfNeeded() {
        if (opByCat == null) {
            opByCat = buildMap();
        }
    }

    protected Map<String, List<String>> buildMap() {

        Map<String, List<String>> map = new HashMap<String, List<String>>();

        AutomationService as = Framework.getService(AutomationService.class);
        for (OperationType opType : as.getOperations()) {
            try {
                String cat = opType.getDocumentation().getCategory();
                if (cat == null || cat.isEmpty()) {
                    cat = MISC;
                }
                if (!map.keySet().contains(cat)) {
                    map.put(cat, new ArrayList<String>());
                }
                map.get(cat).add(opType.getId());
            } catch (OperationException e) {
                System.out.println("Exeception on OP " + opType.getClass().getSimpleName());
            }
        }
        for (String cat : map.keySet()) {
            Collections.sort(map.get(cat));
        }
        return map;

    }

    public List<String> getOperationIdsForCategory(String cat, String filter) {
        initIfNeeded();
        if (filter==null) {
            return opByCat.get(cat);
        } else {
            List<String> opids = new ArrayList<String>();
            for (String op : opByCat.get(cat)) {
                if (op.toLowerCase().contains(filter.toLowerCase())) {
                    opids.add(op);
                }
            }
            Collections.sort(opids);
            return opids;
        }
    }

    public List<String> getCategories(String filter) {
        initIfNeeded();
        List<String> categories = null;
        if (filter==null) {
            categories = new ArrayList<>(opByCat.keySet());
        } else {
            categories = new ArrayList<>();
            for (String cat : opByCat.keySet()) {
                if (getOperationIdsForCategory(cat, filter).size()>0) {
                    categories.add(cat);
                }
            }
        }
        Collections.sort(categories);
        return categories;
    }

}
