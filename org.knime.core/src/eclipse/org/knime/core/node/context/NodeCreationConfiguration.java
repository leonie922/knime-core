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
 *   Oct 8, 2019 (Mark Ortmann, KNIME GmbH, Berlin, Germany): created
 */
package org.knime.core.node.context;

import java.util.Optional;

import org.knime.core.node.ConfigurableNodeFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.context.ports.IPortsConfiguration;
import org.knime.core.node.context.url.IURLConfiguration;

/**
 * Class storing any additional information required for the appropriate initialization of a {@link NodeModel}.
 *
 * @author Mark Ortmann, KNIME GmbH, Berlin, Germany
 * @noreference This class is not intended to be referenced by clients.
 */
public final class NodeCreationConfiguration extends NodeCreationConfigurationRO
    implements IDeepCopy<NodeCreationConfiguration>, INodeSettingsSerializable {

    /** Node creation config key. */
    private static final String NODE_CREATION_CONFIG_KEY = "node_creation_config";

    /**
     * Constructor.
     *
     * @param factory the node factory
     */
    public NodeCreationConfiguration(final ConfigurableNodeFactory<NodeModel> factory) {
        super(factory);
    }

    private NodeCreationConfiguration(final IURLConfiguration url, final IPortsConfiguration portsConfig) {
        super(url, portsConfig);
    }

    /**
     * Returns the ports config.
     *
     * @return the ports config
     */
    public Optional<IPortsConfiguration> getPortsConfig() {
        return Optional.ofNullable(m_portsConfig);
    }

    /**
     * Returns the url config.
     *
     * @return the url config
     */
    public Optional<IURLConfiguration> getURLConfig() {
        return Optional.ofNullable(m_urlConfig);
    }

    @Override
    public NodeCreationConfiguration copy() {
        return new NodeCreationConfiguration(getURLConfig().map(cfg -> cfg.copy()).orElse(null),
            getPortsConfig().map(cfg -> cfg.copy()).orElse(null));
    }

    @Override
    public void saveSettingsTo(final NodeSettingsWO settings) {
        if (m_urlConfig != null || m_portsConfig != null) {
            final NodeSettingsWO creationConfig = settings.addNodeSettings(NODE_CREATION_CONFIG_KEY);
            if (m_urlConfig != null) {
                m_urlConfig.saveSettingsTo(creationConfig);
            }
            if (m_portsConfig != null) {
                m_portsConfig.saveSettingsTo(creationConfig);
            }
        }
    }

    @Override
    public void loadSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        if (m_urlConfig != null || m_portsConfig != null) {
            final NodeSettingsRO creationConfig = settings.getNodeSettings(NODE_CREATION_CONFIG_KEY);
            if (m_urlConfig != null) {
                m_urlConfig.loadSettingsFrom(creationConfig);
            }
            if (m_portsConfig != null) {
                m_portsConfig.loadSettingsFrom(creationConfig);
            }
        }
    }

}
