package rms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ErrorWindow extends JFrame {
    public ErrorWindow(String warningText) {
        JDialog d = new JDialog();
        JLabel l = new JLabel(warningText, SwingConstants.CENTER);
        l.setBorder(new EmptyBorder(10,10,10,10));
        d.add(l);
        Dimension dim = d.getToolkit().getScreenSize();
        Rectangle r = d.getBounds();
        d.setLocation( (dim.width - r.width)/2, (dim.height - r.height)/2 );
        d.pack();
        d.setVisible(true);
    }

}
