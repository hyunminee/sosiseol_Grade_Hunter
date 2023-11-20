import javax.swing.*;
import java.awt.*;

public class frame {
    public static void main(String[] args){



        JFrame frame = new JFrame(); // 프레임 생성
        GamePlayPanel gameplayPanel = new GamePlayPanel(); // GamePlayPanel 인스턴스 생성
        frame.setTitle("Grade Hunter");
        frame.setContentPane(gameplayPanel); // GamePlayPanel을 메인 패널로 설정
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();

        Image img = kit.getImage("images\\Gradcap.png");
        frame.setIconImage(img);
        frame.setResizable(false); // 화면 크기 조정 불가능하도록 설정
        frame.setPreferredSize(new Dimension(1080, 720)); // 프레임의 선호 크기 설정
        frame.setSize(1080, 720); // 프레임 크기 설정
        frame.setLocationRelativeTo(null); // 화면 가운데에서 출력되도록 설정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프로그램 종료 동작 설정
        frame.setVisible(true); // 화면을 보이게 설정
    }
}
