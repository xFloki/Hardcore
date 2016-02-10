import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

public class Plane extends JPanel implements MouseMotionListener, MouseListener {

  // Attributes
  public static final int DRAW_POLYGON = 1;
  public static final int DRAW_STAR    = 2;

  private int maxY, currentOperation, sides;
  private Vector<Point> polygon;
  private Vector<Point> star;
  private Point center;

  
  public Plane()
  {
    setBackground(Color.WHITE);
    addMouseListener(this);
    addMouseMotionListener(this);
    setCurrentOperation(DRAW_POLYGON); // Draw polygon by default

    polygon = new Vector<Point>();
    star    = new Vector<Point>();
  }

  void setCurrentOperation(int operation)
  {
    currentOperation = operation;
  }

  int getCurrentOperation()
  {
    return currentOperation;
  }

  void setSides(int n)
  {
    sides = n;
  }

  int getSides()
  {
    return sides;
  }

  /**
   * Compute the coordinates of a polygon.
   *
   * @param n number of sides
   * @param center center of the polygon
   * @param point point of the first corner
   */
  void computePolygon(int n, Point center, Point point)
  {
    double r = center.distance(point),
           cx = center.getX(),
           cy = ly((int)center.getY()),
           px = point.getX(),
           py = ly((int)point.getY()),
           theta = 2*Math.PI/n,
           beta = Math.atan2(py - cy, px - cx),
           x, y, xPrime, yPrime;

    polygon.clear();

    for (int i = 0; i < n; i++) {
      x = r * Math.cos(i * theta);
      y = r * Math.sin(i * theta);

      // Rotate the polygon such that the mouse click matches the  polygon corner
      xPrime = x * Math.cos(beta) - y * Math.sin(beta);
      yPrime = x * Math.sin(beta) + y * Math.cos(beta);

      // Translate the polygon to it's original position
      xPrime += cx;
      yPrime += cy;

      polygon.add(new Point((int)Math.round(xPrime), (int)Math.round(yPrime)));
    }
  }
  
  /**
   * Compute the coordinates of a n-pointed star.
   *
   * @param n number of points
   * @param center star center
   * @param point first point of the star
   */

  void computeStar(int n, Point center, Point point)
  {
    double r = center.distance(point),
           theta = 2*Math.PI/n,
           cx = center.getX(),
           cy = ly((int)center.getY()),
           px = point.getX(),
           py = ly((int)point.getY()),
           beta = Math.atan2(py - cy, px - cx),
           x, y, xPrime, yPrime;

    star.clear();

    for (int i = 0; i < n; i++) {
      x = r * Math.cos(i * theta);
      y = r * Math.sin(i * theta);

      // Rotate the polygon such that the mouse click matches the  polygon corner
      xPrime = x * Math.cos(beta) - y * Math.sin(beta);
      yPrime = x * Math.sin(beta) + y * Math.cos(beta);

      // Translate to it's original position
      xPrime += cx;
      yPrime += cy;
      star.add(new Point((int)Math.round(xPrime), (int)Math.round(yPrime)));

      x = 0.5 * r * Math.cos(i * theta + 0.5 * theta);
      y = 0.5 * r * Math.sin(i * theta + 0.5 * theta);

      xPrime = x * Math.cos(beta) - y * Math.sin(beta);
      yPrime = x * Math.sin(beta) + y * Math.cos(beta);

      xPrime += cx;
      yPrime += cy;
      star.add(new Point((int)Math.round(xPrime), (int)Math.round(yPrime)));
    }
  }


  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;

    // Draw smooth lines
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);

    maxY = getHeight(); // Update maxY

    int n = polygon.size();
    for (int i = 0; i < n; i++) {
      int x1 = (int)polygon.get(i).getX(),
          y1 = oy((int)polygon.get(i).getY()),
          x2 = (int)polygon.get((i + 1)%n).getX(),
          y2 = oy((int)polygon.get((i + 1)%n).getY());

      g2d.drawLine(x1, y1, x2, y2);
    }
  
    n = star.size();
    for (int i = 0; i < n; i++) {
      int x1 = (int)star.get(i).getX(),
          y1 = oy((int)star.get(i).getY()),
          x2 = (int)star.get((i + 1)%n).getX(),
          y2 = oy((int)star.get((i + 1)%n).getY());

      g2d.drawLine(x1, y1, x2, y2);
    }
  }

  // Returns y transformed to cartesian plane normal behaviour: y grows up.
  private int ly(int y) { return maxY - y; }

  // Returns y normal behaviour: y grows down.
  private int oy(int y) { return maxY - y; }

  // MouseListener interface
  public void mouseClicked(MouseEvent event) { }
  public void mouseEntered(MouseEvent event) { }
  public void mouseExited(MouseEvent event)  { }
  public void mouseReleased(MouseEvent event){ }

  public void mousePressed(MouseEvent event)
  {
    center = event.getPoint();
  }


  // MouseMotionListener interface
  public void mouseMoved(MouseEvent event)   { }

  public void mouseDragged(MouseEvent event)
  { 
    Point p = event.getPoint();
    
    if (getCurrentOperation() == DRAW_POLYGON)
      computePolygon(getSides(), center, p);
    else if (getCurrentOperation() == DRAW_STAR)
      computeStar(getSides(), center, p);

    repaint();
  }

}

