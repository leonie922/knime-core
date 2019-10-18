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
 *   Oct 10, 2019 (Adrian Nembach, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.data.probability;

import java.util.LinkedHashSet;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataValueMetaDataCreator;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.util.CheckUtils;

/**
 * TODO
 *
 * @author Adrian Nembach, KNIME GmbH, Konstanz, Germany
 * @since 4.1
 */
public final class NominalDistributionValueMetaDataCreator
    implements DataValueMetaDataCreator<NominalDistributionValue> {

    private final LinkedHashSet<String> m_values;

    NominalDistributionValueMetaDataCreator() {
        m_values = new LinkedHashSet<>();
    }

    private NominalDistributionValueMetaDataCreator(final LinkedHashSet<String> values) {
        m_values = new LinkedHashSet<>(values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final DataCell cell) {
        if (cell.isMissing()) {
            return;
        }
        CheckUtils.checkArgument(cell instanceof NominalDistributionValue,
            "The cell %s of type %s does not implement NominalDistributionValue.", cell, cell.getClass());
        final NominalDistributionValue value = (NominalDistributionValue)cell;
        m_values.addAll(value.getKnownValues());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NominalDistributionValueMetaData create() {
        return new DefaultNominalDistributionValueMetaData(m_values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataValueMetaDataCreator<NominalDistributionValue> copy() {
        return new NominalDistributionValueMetaDataCreator(m_values);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void merge(final DataValueMetaDataCreator<?> other) {
        CheckUtils.checkArgument(other instanceof NominalDistributionValueMetaDataCreator,
            "Can only merge with NominalDistributionValueMetaDataCreator but received object of type %s.",
            other.getClass().getName());
        final NominalDistributionValueMetaDataCreator otherCreator = (NominalDistributionValueMetaDataCreator)other;
        m_values.addAll(otherCreator.m_values);
    }

    /**
     * {@inheritDoc}
     * @throws InvalidSettingsException
     */
    @Override
    public NominalDistributionValueMetaData load(final ConfigRO config) throws InvalidSettingsException {
        final String[] values = config.getStringArray(DefaultNominalDistributionValueMetaData.CFG_VALUES);
        return new DefaultNominalDistributionValueMetaData(values);
    }

}
