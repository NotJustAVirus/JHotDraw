package jhotdraw.samples.svg.figures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.jhotdraw.samples.svg.figures.SVGRectFigure;
import org.junit.jupiter.api.Test;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

class SVGRectFigureTest {

    @Test
    void drawStroke_plainRectangle_drawsBounds() {
        TestableSVGRectFigure fig = new TestableSVGRectFigure();
        fig.setBounds(new Point2D.Double(0, 0), new Point2D.Double(100, 50));
        fig.setArc(0, 0);

        Graphics2D g = mock(Graphics2D.class);

        fig.callDrawStroke(g);

        verify(g).draw(any(Rectangle2D.class));
    }

    @Test
    void drawStroke_roundedRectangle_drawsPath() {
        TestableSVGRectFigure fig = new TestableSVGRectFigure();
        fig.setBounds(new Point2D.Double(0, 0), new Point2D.Double(100, 50));
        fig.setArc(20, 20);

        Graphics2D g = mock(Graphics2D.class);

        fig.callDrawStroke(g);

        verify(g).draw(any(java.awt.Shape.class));
    }
    
    @Test
    void drawStroke_afterResize_usesCorrectStrategy() {
        TestableSVGRectFigure fig = new TestableSVGRectFigure();
        fig.setBounds(new Point2D.Double(0, 0), new Point2D.Double(50, 25));
        fig.setArc(0, 0);

        Graphics2D g = mock(Graphics2D.class);

        fig.callDrawStroke(g);
        verify(g).draw(any(Rectangle2D.class));
        
        reset(g);

        fig.setBounds(new Point2D.Double(0, 0), new Point2D.Double(100, 50));
        fig.callDrawStroke(g);
        verify(g).draw(any(Rectangle2D.class));
    }

}


// Needed as SVGRectFigure.drawStroke is protected
class TestableSVGRectFigure extends SVGRectFigure {
    void callDrawStroke(Graphics2D g) {
        super.drawStroke(g);
    }
}