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
 *   Oct 11, 2019 (Adrian Nembach, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.data;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.knime.core.data.DataValue.UtilityFactory;

/**
 * Calculates meta data from actual data by creating meta data corresponding to the presented rows.
 * TODO
 *
 * @author Adrian Nembach, KNIME GmbH, Konstanz, Germany
 */
final class MetaDataCalculator {

    private final List<DataValueMetaDataCreator<?>> m_metaDataCreators;

    MetaDataCalculator(final DataType type) {
        m_metaDataCreators = type.getValueClasses().stream().map(DataType::getUtilityFor)
            .filter(UtilityFactory::hasMetaData).map(UtilityFactory::getMetaDataCreator).collect(Collectors.toList());
    }

    MetaDataCalculator(final MetaDataCalculator toCopy) {
        m_metaDataCreators =
            toCopy.m_metaDataCreators.stream().map(DataValueMetaDataCreator::copy).collect(Collectors.toList());
    }

    void update(final DataCell cell) {
        m_metaDataCreators.forEach(c -> c.update(cell));
    }

    List<DataValueMetaData<?>> createMetaData() {
        return m_metaDataCreators.stream().map(DataValueMetaDataCreator::create).collect(Collectors.toList());
    }

    void merge(final MetaDataCalculator other) {
        final Iterator<DataValueMetaDataCreator<?>> otherCreators = other.m_metaDataCreators.iterator();
        for (DataValueMetaDataCreator<?> creator : m_metaDataCreators) {
            assert otherCreators.hasNext();
            final DataValueMetaDataCreator<?> otherCreator = otherCreators.next();
            assert creator.getClass().equals(otherCreator.getClass());
            creator.merge(otherCreator);
        }
    }

}
