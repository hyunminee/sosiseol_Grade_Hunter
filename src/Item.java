import javax.swing.ImageIcon;
import java.awt.*;
import java.net.URL;
/**
 * 게임 내 아이템을 나타내는 클래스
 * @author 박현민
 * */
public class Item {
    private Image image; // 아이템의 이미지를 나타내는 객체
    private int x, y; // 아이템의 화면 내 위치 (x, y 좌표)
    private int deltaY; // 아이템의 Y축 이동 속도 (하강 속도)
    private int effectValue; // 아이템의 효과 값 (게이지 증가/감소, 시간 조절 등)
    private final ItemType type; // 아이템의 타입 (BLUE, GREEN 등)

    /** 아이템 객체 생성자 */
    public Item(String imagePath, int x, int y, ItemType type, int speed) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.effectValue = type.getEffectValue(); // 아이템 효과 값 반환
        this.deltaY = speed; // 하강 속도 초기화, 스테이지별로 조정 가능
        loadImage(imagePath); // 이미지 로딩
    }

    /** 이미지 로드 메소드 */
    private void loadImage(String imagePath) {
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            ImageIcon ii = new ImageIcon(imageUrl);
            image = ii.getImage(); // 이미지 로드하고 저장
        } else {
            // 이미지를 찾을 수 없을 경우의 처리
            System.err.println("Image not found: " + imagePath);
            // 예외 처리 또는 기본 이미지 설정 등의 로직
        }

        ImageIcon ii = new ImageIcon(getClass().getResource(imagePath));
        image = ii.getImage(); // 이미지 로드하고 저장
    }

    /** 아이템의 이동 로직*/
    public void move() {
        y += deltaY; // 아이템을 하강시킴
        // 화면 밖으로 나가는 처리는 actionPerformed에서 수행
    }
    /** 아이템을 화면에 그리는 메소드 */
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEffectValue() {
        return effectValue;
    }

    /** 아이템의 타입을 반환하는 메소드 */
    public ItemType getType() {
        return type;
    }

    /**아이템이 화면 내에 있는지 확인하는 메소드*/
    public boolean isOnScreen() {
        final int screenHeight = 720; // 화면 높이
        return y >= 0 && y < screenHeight; // 아이템이 화면 안에 있는지 확인

    }
}