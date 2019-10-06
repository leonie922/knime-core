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
 *   Sep 27, 2019 (Perla Gjoka, KNIME GmbH, Konstanz, Germany): created
 */
package org.knime.core.data.probability;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.Icon;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.renderer.AbstractDataValueRendererFactory;
import org.knime.core.data.renderer.DataValueRenderer;
import org.knime.core.data.renderer.DefaultDataValueRenderer;

/**
 *
 * @author Perla Gjoka, KNIME GmbH, Konstanz, Germany
 */
public class ProbabilityDistributionValueRenderer2 extends DefaultDataValueRenderer {

    /**
     *
     */
    private static DecimalFormat format = new DecimalFormat("##.##");

    private static final long serialVersionUID = 1L;

    private static final String DESCRIPTION_PROB_DISTR = "Probability Distribution2";

    BarIcon m_icon;

    private ClassProbabilityBar[] m_bars;

    private static class ClassProbability {
        private double m_probability;
        private String m_class;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return String.format("%s: %s%%", m_class, format.format(m_probability * 100));
        }
    }

    private static class ClassProbabilityBar extends Rectangle2D.Double {
        private ClassProbability m_classProbability;

        ClassProbabilityBar(final ClassProbability classProbability) {
            m_classProbability = classProbability;
        }

        ClassProbability getClassProbability() {
            return m_classProbability;
        }
    }

    ProbabilityDistributionValueRenderer2(final DataColumnSpec spec) {
        super(spec);
        m_icon = new BarIcon();
    }

    @Override
    public String getDescription() {
        return DESCRIPTION_PROB_DISTR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToolTipText(final MouseEvent event) {
        for (ClassProbabilityBar bar : m_bars) {
            if (bar.contains(event.getPoint())) {
                return bar.getClassProbability().toString();
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g.create();
    }

    @Override
    protected void setValue(final Object value) {
        if (value instanceof ProbabilityDistributionValue) {
            List<String> probClasses = getColSpec().getElementNames();
            ProbabilityDistributionValue probDistrValue = (ProbabilityDistributionValue)value;
            String[] values = new String[probDistrValue.size()];
            double[] valuesBars = new double[probDistrValue.size()];
            for (int i = 0; i < probDistrValue.size(); i++) {
                values[i] = format.format(probDistrValue.getProbability(i) * 100) + "%";
                valuesBars[i] = probDistrValue.getProbability(i);
            }
            Icon icon = getIcon();
            if (icon == null) {
                super.setIcon(m_icon);
                icon = m_icon;
            }
            ((BarIcon)icon).setText(probClasses);
            ((BarIcon)icon).setBars(valuesBars);
            ((BarIcon)icon).setNumberOfColumns(probDistrValue.size());
            ((BarIcon)icon).setValue(10);

        } else {
            super.setValue(value);
        }
    }

    /** Renderer factory registered through extension point. */
    public static final class DefaultRendererFactory extends AbstractDataValueRendererFactory {

        @Override
        public String getDescription() {
            return DESCRIPTION_PROB_DISTR;
        }

        @Override
        public DataValueRenderer createRenderer(final DataColumnSpec colSpec) {
            return new ProbabilityDistributionValueRenderer2(colSpec);
        }
    }

    private class BarIcon implements Icon {

        private int m_value = 0;

        private double[] m_bars;

        private List<String> m_labelText;

        private int m_numberOfColumns;

        public void setBars(final double[] bars) {
            m_bars = bars;
        }
        public void setText(final List<String> labelText) {
            m_labelText = labelText;
        }

        public void setNumberOfColumns(final int numberOfColumns) {
            m_numberOfColumns = numberOfColumns;
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconHeight() {
            return 80;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getIconWidth() {
            return 100;
        }

        /**
         * Sets the current vale.
         *
         * @param d double value.
         */
        public void setValue(final int d) {
            m_value = d;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void paintIcon(final Component c, final Graphics g, final int x, final int y) {
            int width = m_value * m_numberOfColumns;
            for (int i = 0; i < m_bars.length; i ++) {
                int height = (int)(m_bars[i]*100/2);
                int startVertical = getIconHeight()/3 + getIconHeight()/2 - height;
                int startHorizontal = x + (width*i);
                final Graphics2D g2d = (Graphics2D)g;
                g2d.setPaint(Color.ORANGE);
                g2d.setStroke(new BasicStroke(10));
                g2d.fill(new Rectangle2D.Double(startHorizontal, startVertical, width, height ));
                g2d.setPaint(Color.black);
                g.draw3DRect(startHorizontal, startVertical, width, height, true);
                g.drawString(m_labelText.get(i), startHorizontal, startVertical-1);
            }

        }
    }
}
