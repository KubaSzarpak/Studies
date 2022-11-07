package UTP43;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public interface MousePressListener extends MouseListener {
    void mouseClicked(MouseEvent e);


    default void mousePressed(MouseEvent e){

    }


    default void mouseReleased(MouseEvent e){

    }


    default void mouseEntered(MouseEvent e){

    }


    default void mouseExited(MouseEvent e){

    }
}
