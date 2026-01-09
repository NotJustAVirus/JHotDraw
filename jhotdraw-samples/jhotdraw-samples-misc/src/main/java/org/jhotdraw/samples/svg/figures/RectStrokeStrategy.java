package org.jhotdraw.samples.svg.figures;

import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public interface RectStrokeStrategy {
    void drawStroke(Graphics2D g, RoundRectangle2D.Double rect);
}
