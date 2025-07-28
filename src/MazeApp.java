import javax.swing.SwingUtilities;

import controllers.MazeController;
import views.MazeFrame;

public class MazeApp {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            MazeFrame view = new MazeFrame();
            
            MazeController controller = new MazeController(view);
            
            view.setVisible(true);
        });
       
    }
}
