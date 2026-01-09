package org.jhotdraw.samples.svg.figures;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class PlainRectStrokeStrategy implements RectStrokeStrategy {
    @Override
    public void drawStroke(Graphics2D g, RoundRectangle2D.Double rect) {
        g.draw(rect.getBounds2D());
    }
}
