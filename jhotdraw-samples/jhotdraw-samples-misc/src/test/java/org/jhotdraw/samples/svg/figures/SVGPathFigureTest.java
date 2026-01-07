package org.jhotdraw.samples.svg.figures;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SVGPathFigureTest {

    @Test
    public void testIsEmptyAndAddChild() {
        SVGPathFigure figure = new SVGPathFigure(true);

        assertTrue(figure.isEmpty());

        // then add child
        SVGBezierFigure child = new SVGBezierFigure();
        child.setBounds(new Point2D.Double(0,0), new Point2D.Double(10,10));
        figure.add(child);

        assertFalse(figure.isEmpty());
    }

    
}
