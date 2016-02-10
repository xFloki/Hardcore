import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Poligonos extends JFrame{

  private Plane plane;
  private JToolBar toolBar;
  private JButton polygonButton;
  private JButton starButton;
  private JSpinner sidesSpinner;

  public Poligonos()
  {
    super("Polygons and Starts");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600, 400);
    setLocationRelativeTo(null);

    polygonButton = new JButton(new ImageIcon("pictures/draw-polygon.png"));
    starButton    = new JButton(new ImageIcon("pictures/draw-star.png"));
    sidesSpinner   = new JSpinner(new SpinnerNumberModel(3, 3, 100, 1));
    toolBar       = new JToolBar();

    toolBar.add(polygonButton);
    toolBar.add(starButton);
    toolBar.add(sidesSpinner);

    ActionHandler actionHandler = new ActionHandler();

    polygonButton.addActionListener(actionHandler);
    starButton.addActionListener(actionHandler);
    sidesSpinner.addChangeListener(actionHandler);


    plane = new Plane();
    plane.setSides(3); // Minimum
    add(toolBar, BorderLayout.PAGE_START);
    add(plane, BorderLayout.CENTER);

    setVisible(true);
  }

  public static void main(String[] args)
  {
    Poligonos main = new Poligonos();
  }


  class ActionHandler implements ActionListener, ChangeListener {
    public void actionPerformed(ActionEvent event)
    {
      if (event.getSource() == polygonButton)
        plane.setCurrentOperation(Plane.DRAW_POLYGON);
      else if (event.getSource() == starButton)
        plane.setCurrentOperation(Plane.DRAW_STAR);
    }

    public void stateChanged(ChangeEvent event)
    {
      if (event.getSource() == sidesSpinner) {
        JSpinner spinner = (JSpinner)event.getSource();
        int value = (Integer)spinner.getValue();
        plane.setSides(value);
      }
    }
  } // End of ActionListener

} // End of Main

