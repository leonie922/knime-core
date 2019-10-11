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

import org.knime.core.node.context.ports.IPortGroupConfiguration;
import org.knime.core.node.port.PortType;

/**
 * Utility class for creating @link {@link IPortGroupConfiguration port group configurations}.
 *
 * @author Mark Ortmann, KNIME GmbH, Berlin, Germany
 */
public final class PortGroupUtils {

    private PortGroupUtils() {

    }

    /**
     * Creates a static input port group configuration.
     *
     * @param pTypes the static port types
     * @return a static input port group
     */
    public static IPortGroupConfiguration createStaticInputPortGroupConfig(final PortType[] pTypes) {
        return new StaticPortGroup(pTypes, true, false);
    }

    /**
     * Creates a static output port group configuration.
     *
     * @param pTypes the static port types
     * @return a static output port group
     */
    public static IPortGroupConfiguration createStaticOutputPortGroupConfig(final PortType[] pTypes) {
        return new StaticPortGroup(pTypes, false, true);
    }

    /**
     * Creates a static port group configuration.
     *
     * @param pTypes the static port types
     * @return a static port group
     */
    public static IPortGroupConfiguration createStaticPortGroupConfig(final PortType[] pTypes) {
        return new StaticPortGroup(pTypes, true, true);
    }

    /**
     * Creates a exchangeable input port group configuration.
     *
     * @param defaultType the default port type
     * @param supportedTypes the supported port types (has to include the default type itself)
     * @return an exchangeable input port group
     */
    public static IPortGroupConfiguration createExchangeableInputPort(final PortType defaultType,
        final PortType[] supportedTypes) {
        return new ExchangeablePortGroup(defaultType, supportedTypes, true, false);
    }

    /**
     * Creates a exchangeable output port group configuration.
     *
     * @param defaultType the default port type
     * @param supportedTypes the supported port types (has to include the default type itself)
     * @return an exchangeable output port group
     */
    public static IPortGroupConfiguration createExchangeableOutputPort(final PortType defaultType,
        final PortType[] supportedTypes) {
        return new ExchangeablePortGroup(defaultType, supportedTypes, false, true);
    }

    /**
     * Creates a exchangeable port group configuration.
     *
     * @param defaultType the default port type
     * @param supportedTypes the supported port types (has to include the default type itself)
     * @return an exchangeable port group
     */
    public static IPortGroupConfiguration createExchangeablePort(final PortType defaultType,
        final PortType[] supportedTypes) {
        return new ExchangeablePortGroup(defaultType, supportedTypes, true, true);
    }

    /**
     * Creates a extendable input port group configuration.
     *
     * @param requiredTypes the required port types
     * @param supportedTypes the supported port types
     * @return an extendable input port group
     */
    public static IPortGroupConfiguration createExtendableInputPort(final PortType[] requiredTypes,
        final PortType[] supportedTypes) {
        return new ExtendablePortGroup(requiredTypes, supportedTypes, true, false);
    }

    /**
     * Creates a extendable output port group configuration.
     *
     * @param requiredTypes the required port types
     * @param supportedTypes the supported port types
     * @return an extendable output port group
     */
    public static IPortGroupConfiguration createExtendableOutputPort(final PortType[] requiredTypes,
        final PortType[] supportedTypes) {
        return new ExtendablePortGroup(requiredTypes, supportedTypes, false, true);
    }

    /**
     * Creates a extendable port group configuration.
     *
     * @param requiredTypes the required port types
     * @param supportedTypes the supported port types
     * @return an extendable port group
     */
    public static IPortGroupConfiguration createExtendablePort(final PortType[] requiredTypes,
        final PortType[] supportedTypes) {
        return new ExtendablePortGroup(requiredTypes, supportedTypes, true, true);
    }

}
