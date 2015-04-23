/* Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License. */

package org.apache.isis.core.metamodel.facets.actions.layout;

import java.util.Properties;

import org.apache.isis.applib.annotation.CssClassFaPosition;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facets.members.cssclassfa.CssClassFaFacet;
import org.apache.isis.core.metamodel.facets.members.cssclassfa.CssClassFaFacetAbstract;

import com.google.common.base.Strings;

public class CssClassFaFacetOnActionFromLayoutProperties extends CssClassFaFacetAbstract {

    public static CssClassFaFacet create(final Properties properties, final FacetHolder holder) {
        final String cssClassFa = cssClassFa(properties);
        CssClassFaPosition position = cssClassFaPosition(properties);
        return cssClassFa != null ? new CssClassFaFacetOnActionFromLayoutProperties(cssClassFa, position, holder) : null;
    }

    private CssClassFaFacetOnActionFromLayoutProperties(final String cssClass,
            final CssClassFaPosition position, final FacetHolder holder) {
        super(cssClass, null, holder);
    }

    private static String cssClassFa(final Properties properties) {
        if (properties == null) {
            return null;
        }
        return Strings.emptyToNull(properties.getProperty("cssClassFa"));
    }

    private static CssClassFaPosition cssClassFaPosition(final Properties properties) {
        if (properties == null) {
            return null;
        }
        String cssClassFaPosition = Strings.emptyToNull(properties.getProperty("cssClassFaPosition"));
        return cssClassFaPosition != null
                ? CssClassFaPosition.valueOf(cssClassFaPosition.toUpperCase())
                : CssClassFaPosition.LEFT;
    }
}
