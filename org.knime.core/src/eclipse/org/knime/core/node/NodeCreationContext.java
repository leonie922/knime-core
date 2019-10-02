/*
 * ------------------------------------------------------------------------
 *  Copyright by KNIME AG, Zurich, Switzerland
 *  Website: http://www.knime.com; Email: contact@knime.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME AG herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ------------------------------------------------------------------------
 */
package org.knime.core.node;

import java.net.MalformedURLException;
import java.net.URL;

import org.knime.core.node.context.IURLCreationContext;

/**
 * Class storing an URL.
 *
 * @author ohl, University of Konstanz
 */
public class NodeCreationContext implements IURLCreationContext<NodeCreationContext> {

    private URL m_url;

    /**
     * Default URL constructor.
     *
     * @noreference This constructor is not intended to be referenced by clients.
     */
    public NodeCreationContext() {
        this(null);
    }

    /**
     * Constructor.
     *
     * @param url the URL
     */
    public NodeCreationContext(final URL url) {
        m_url = url;
    }

    /**
     * @return the url
     */
    @Override
    public URL getUrl() {
        return m_url;
    }

    @Override
    public void save(final NodeSettingsWO settings) {
        if (m_url != null) {
            settings.addString("url", m_url.toString());
        }
    }

    @Override
    public void load(final NodeSettingsRO settings) throws InvalidSettingsException {
        if (settings.containsKey("url")) {
            try {
                m_url = new URL(settings.getString("url"));
            } catch (MalformedURLException ex) {
                throw new InvalidSettingsException(
                    "The value " + settings.getString("url") + " does not encode a valid URL", ex.getCause());
            }
        }
    }

    /**
     * @since 4.1
     */
    @Override
    public NodeCreationContext cloneIt() {
        return new NodeCreationContext(m_url);
    }
}
