/*
 * ------------------------------------------------------------------------
 *
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
 * ---------------------------------------------------------------------
 *
 * History
 *   Oct 10, 2019 (Mark Ortmann, KNIME GmbH, Berlin, Germany): created
 */
package org.knime.core.node.context;

import java.util.Optional;

import org.knime.core.node.ConfigurableNodeFactory;
import org.knime.core.node.NodeModel;
import org.knime.core.node.context.ports.IPortsConfiguration;
import org.knime.core.node.context.ports.IPortsConfigurationRO;
import org.knime.core.node.context.ports.impl.PortsConfigurationBuilder;
import org.knime.core.node.context.url.IURLConfiguration;
import org.knime.core.node.context.url.IURLConfigurationRO;

/**
 * Class storing any additional information required for the appropriate initialization of a {@link NodeModel}.
 *
 * @author Mark Ortmann, KNIME GmbH, Berlin, Germany
 * @since 4.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class NodeCreationConfigurationRO {

    /** the url config. */
    protected final IURLConfiguration m_urlConfig;

    /** the ports config. */
    protected final IPortsConfiguration m_portsConfig;

    /**
     * Constructor.
     *
     * @param factory the node factory
     */
    protected NodeCreationConfigurationRO(final ConfigurableNodeFactory<NodeModel> factory) {
        m_urlConfig = factory.getURLConfig().orElse(null);
        m_portsConfig = factory.getPortsConfig().map(PortsConfigurationBuilder::build).orElse(null);
    }

    /**
     * Constructor.
     *
     * @param url the url config
     * @param portsConfig the ports config
     */
    protected NodeCreationConfigurationRO(final IURLConfiguration url, final IPortsConfiguration portsConfig) {
        m_urlConfig = url;
        m_portsConfig = portsConfig;
    }

    /**
     * Returns the port config.
     *
     * @return the port config.
     */
    public Optional<IPortsConfigurationRO> getPortConfigRO() {
        return Optional.ofNullable(m_portsConfig);
    }

    /**
     * Returns the url config
     *
     * @return the url config
     */
    public Optional<IURLConfigurationRO> getURLConfigRO() {
        return Optional.ofNullable(m_urlConfig);
    }

}
