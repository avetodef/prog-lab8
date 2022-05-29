package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

/**
 * Класс команды RZHAKA, предназначенный для мемного троллинга
 */
public class Rzhaka extends ACommands{

//    public String execute(RouteDAO routeDAO) {
//        while (true) {
//            try {
//                File common.file = new File("forAlex.png");
//                BufferedImage bufferedImage = ImageIO.read(common.file);
//
//                ImageIcon imageIcon = new ImageIcon(bufferedImage);
//                JFrame jFrame = new JFrame();
//
//                jFrame.setLayout(new FlowLayout());
//
//                jFrame.setSize(1080, 1080);
//                JLabel jLabel = new JLabel();
//
//                jLabel.setIcon(imageIcon);
//                jFrame.add(jLabel);
//                jFrame.setVisible(true);
//
//                jFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//                break;
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//return "vefwerfgw";
//    }

    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        new Thread(new GifRzhaka()).start();
        return response.msg("hehe monkey").status(Status.OK);
    }
}
