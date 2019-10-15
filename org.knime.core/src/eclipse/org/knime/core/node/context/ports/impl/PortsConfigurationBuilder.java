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
 *   Oct 11, 2019 (Mark Ortmann, KNIME GmbH, Berlin, Germany): created
 */
package org.knime.core.node.context.ports.impl;

import java.util.LinkedHashMap;

import org.knime.core.node.context.ports.IPortGroupConfiguration;
import org.knime.core.node.context.ports.IPortsConfiguration;
import org.knime.core.node.port.PortType;

/**
 * Builder to create an instance of @link {@link IPortGroupConfiguration port group configurations}. Note that the order
 * how the port groups are added directly affect the node's port order.
 *
 * @author Mark Ortmann, KNIME GmbH, Berlin, Germany
 * @since 4.1
 */
public final class PortsConfigurationBuilder {

    private final LinkedHashMap<String, IPortGroupConfiguration> m_portConfigs;

    /**
     * Constructor.
     */
    public PortsConfigurationBuilder() {
        m_portConfigs = new LinkedHashMap<>();
    }

    /**
     * Creates the {@code IPortsConfiguration}.
     *
     * @return an instance of {@code IPortsConfiguration}
     */
    public IPortsConfiguration build() {
        return new PortsConfiguration(m_portConfigs);
    }

    /**
     * Adds a static input port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param pTypes the static port types
     */
    public void addStaticInputPortGroupConfig(final String pGrpIdentifier, final PortType[] pTypes) {
        m_portConfigs.put(pGrpIdentifier, new StaticPortGroup(pTypes, true, false));
    }

    /**
     * Adds a static output port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param pTypes the static port types
     */
    public void addStaticOutputPortGroupConfig(final String pGrpIdentifier, final PortType[] pTypes) {
        m_portConfigs.put(pGrpIdentifier, new StaticPortGroup(pTypes, false, true));
    }

    /**
     * Adds a static port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param pTypes the static port types
     */
    public void addStaticPortGroupConfig(final String pGrpIdentifier, final PortType[] pTypes) {
        m_portConfigs.put(pGrpIdentifier, new StaticPortGroup(pTypes, true, true));
    }

    /**
     * Adds an extendable input port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param requiredTypes the required port types
     * @param supportedTypes the supported port types
     */
    public void addExtendableInputPort(final String pGrpIdentifier, final PortType[] requiredTypes,
        final PortType[] supportedTypes) {
        m_portConfigs.put(pGrpIdentifier, new ExtendablePortGroup(requiredTypes, supportedTypes, true, false));
    }

    /**
     * Adds an extendable output port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param requiredTypes the required port types
     * @param supportedTypes the supported port types
     */
    public void addExtendableOutputPort(final String pGrpIdentifier, final PortType[] requiredTypes,
        final PortType[] supportedTypes) {
        m_portConfigs.put(pGrpIdentifier, new ExtendablePortGroup(requiredTypes, supportedTypes, false, true));
    }

    /**
     * Adds an extendable port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param requiredTypes the required port types
     * @param supportedTypes the supported port types
     */
    public void addExtendablePort(final String pGrpIdentifier, final PortType[] requiredTypes,
        final PortType[] supportedTypes) {
        m_portConfigs.put(pGrpIdentifier, new ExtendablePortGroup(requiredTypes, supportedTypes, true, true));
    }

    /**
     * Adds an optional input port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param optionalPorts the optional port types
     */
    public void addOptionalInputPort(final String pGrpIdentifier, final PortType... optionalPorts) {
        m_portConfigs.put(pGrpIdentifier, new ExtendablePortGroup(new PortType[]{}, optionalPorts, true, false, 1));
    }

    /**
     * Adds an optional output port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param optionalPorts the optional port types
     */
    public void addOptionalOutputPort(final String pGrpIdentifier, final PortType... optionalPorts) {
        m_portConfigs.put(pGrpIdentifier, new ExtendablePortGroup(new PortType[]{}, optionalPorts, false, true, 1));
    }

    /**
     * Adds an optional port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param optionalPorts the optional port types
     */
    public void addOptionalePort(final String pGrpIdentifier, final PortType... optionalPorts) {
        m_portConfigs.put(pGrpIdentifier, new ExtendablePortGroup(new PortType[]{}, optionalPorts, true, true, 1));
    }

    /**
     * Adds an exchangeable input port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param defaultType the default port type
     * @param supportedTypes the supported port types (has to include the default type itself)
     */
    public void addExchangeableInputPort(final String pGrpIdentifier, final PortType defaultType,
        final PortType[] supportedTypes) {
        m_portConfigs.put(pGrpIdentifier, new ExchangeablePortGroup(defaultType, supportedTypes, true, false));
    }

    /**
     * Adds an exchangeable output port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param defaultType the default port type
     * @param supportedTypes the supported port types (has to include the default type itself)
     */
    public void addExchangeableOutputPort(final String pGrpIdentifier, final PortType defaultType,
        final PortType[] supportedTypes) {
        m_portConfigs.put(pGrpIdentifier, new ExchangeablePortGroup(defaultType, supportedTypes, false, true));
    }

    /**
     * Adds an exchangeable port group configuration.
     *
     * @param pGrpIdentifier the port group identifier
     * @param defaultType the default port type
     * @param supportedTypes the supported port types (has to include the default type itself)
     */
    public void addExchangeablePort(final String pGrpIdentifier, final PortType defaultType,
        final PortType[] supportedTypes) {
        m_portConfigs.put(pGrpIdentifier, new ExchangeablePortGroup(defaultType, supportedTypes, true, true));
    }

}
