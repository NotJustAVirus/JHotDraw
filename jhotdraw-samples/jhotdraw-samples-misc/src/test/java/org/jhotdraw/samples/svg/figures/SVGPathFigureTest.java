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

    @Test
    public void testContainsOnBoundary() {
        SVGPathFigure figure = new SVGPathFigure();
        figure.setBounds(new Point2D.Double(0, 0), new Point2D.Double(100, 0));

        Point2D.Double pointOnFigure = new Point2D.Double(50.0, 1.0); 
        Point2D.Double pointNotOnfigure = new Point2D.Double(50.0, 2.0);

        assertTrue(figure.contains(pointOnFigure));
        assertFalse(figure.contains(pointNotOnfigure));
    }

    
}
