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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.renderer.AbstractDataValueRendererFactory;
import org.knime.core.data.renderer.AbstractPainterDataValueRenderer;
import org.knime.core.data.renderer.DataValueRenderer;

/**
 *
 * @author Perla Gjoka, KNIME GmbH, Konstanz, Germany
 */
public class ProbabilityDistributionBarValueRenderer extends AbstractPainterDataValueRenderer {

    private static DecimalFormat format = new DecimalFormat("##.##");

    private static final long serialVersionUID = 1L;

    private static final String DESCRIPTION_PROB_DISTR = "Probability Distribution Bar Value";

    private static List<ClassProbabilityBar> m_bars = new ArrayList<>();

    private static DataColumnSpec m_spec;

    private ProbabilityDistributionValue m_value;

    private static double m_minProb;

    private static double m_maxProb;

    private static double m_range;

    private static class ClassProbability {

        private double m_probability;

        private String m_classProbability;

        public ClassProbability(final double probability, final String classProbability) {
            m_probability = probability;
            m_classProbability = classProbability;
        }

        public double getProbability() {
            return m_probability;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return String.format("%s: %s%%", m_classProbability, format.format(m_probability * 100));
        }
    }

    private static class ClassProbabilityBar extends Rectangle2D.Double {

        private static final long serialVersionUID = 1L;

        private static final int MARGIN_TOP = 15;

        private static final int MARGIN_LEFT = 5;

        private final ClassProbability m_classProbability;

        /**
         *
         * @param classProbability holds the probability a row belongs to this class.
         * @param offset helps in positioning the bar, based on the previous painted bars.
         * @param barWidth holds the width of each of the bars.
         * @param barHeight holds the height of the cell.
         */
        public ClassProbabilityBar(final ClassProbability classProbability, final int offset, final double barWidth,
            final double barHeight) {
            super(barWidth * offset + MARGIN_LEFT,
                barHeight + MARGIN_TOP - calculateHeight(classProbability, barHeight), barWidth,
                calculateHeight(classProbability, barHeight));
            m_classProbability = classProbability;
        }

        /**
         * @param classProbability holds the probability of each class.
         * @param barHeight holds the height of the cell.
         * @return the height of the bar to be painted.
         */
        private static double calculateHeight(final ClassProbability classProbability, final double barHeight) {
            if (classProbability.getProbability() == 0) {
                return 0;
            } else {
                return (((classProbability.getProbability() - m_minProb) / m_range) * barHeight) + 10;
            }
        }

        ClassProbability getClassProbability() {
            return m_classProbability;
        }

    }

    ProbabilityDistributionBarValueRenderer(final DataColumnSpec spec) {
        m_spec = spec;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accepts(final DataColumnSpec spec) {
        if (spec.getElementNames() == null || spec.getElementNames().isEmpty()) {
            return !super.accepts(spec);
        } else {
            return super.accepts(spec);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(m_spec.getElementNames().size() * 50, 60);
    }

    @Override
    public String getDescription() {
        return DESCRIPTION_PROB_DISTR;
    }

    /**
     * {@inheritDoc} gets the text shown while hovering a bar.
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
        m_bars.clear();
        super.paintComponent(g);
        if (m_value == null) {
            return;
        }
        List<String> probClasses = m_spec.getElementNames();
        Graphics2D g2d = (Graphics2D)g.create();
        for (int i = 0; i < probClasses.size(); i++) {
            m_bars.add(new ClassProbabilityBar(new ClassProbability(m_value.getProbability(i), probClasses.get(i)), i,
                (Math.abs(getWidth()) - 10) / m_value.size(), Math.abs(getHeight()) - 20));
            g2d.setPaint(Color.ORANGE);
            g2d.setStroke(new BasicStroke(1));
            g2d.fill(m_bars.get(i));
            g2d.setPaint(Color.BLACK);
            g2d.draw(m_bars.get(i));
        }
    }

    @Override
    protected void setValue(final Object value) {
        if (value instanceof ProbabilityDistributionValue) {
            m_value = (ProbabilityDistributionValue)value;
            calculateMinMaxProbability(m_value);
        } else {
            m_value = null;
        }
    }

    private static void calculateMinMaxProbability(final ProbabilityDistributionValue value) {
        m_minProb = Double.MAX_VALUE;
        m_maxProb = Double.MIN_VALUE;
        for (int i = 0; i < m_spec.getElementNames().size(); i++) {
            double prob = value.getProbability(i);
            if (prob > m_maxProb) {
                m_maxProb = prob;
            }
            if (prob < m_minProb) {
                m_minProb = prob;
            }
        }
        m_range = m_maxProb - m_minProb;
    }

    /** Renderer factory registered through extension point. */
    public static final class DefaultRendererFactory extends AbstractDataValueRendererFactory {

        @Override
        public String getDescription() {
            return DESCRIPTION_PROB_DISTR;
        }

        @Override
        public DataValueRenderer createRenderer(final DataColumnSpec colSpec) {
            return new ProbabilityDistributionBarValueRenderer(colSpec);
        }
    }
}
