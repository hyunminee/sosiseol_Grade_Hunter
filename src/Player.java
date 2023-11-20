import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * 게임 내 player character를 나타내는 클래스
 * @author 박현민
 * */
public class Player {
    private int x, y; // 캐릭터의 화면 내 위치(x, y 좌표)
    private BufferedImage image; // character 이미지
    private boolean movingLeft = false; // 캐릭터의 현재 이동 방향 상태
    private boolean movingRight = false; // 캐릭터의 현재 이동 방향 상태
    private final int SPEED = 5; // 캐릭터의 이동 속도
    private final int width = 120; // 캐릭터의 너비
    private final int height = 120; // 캐릭터의 높이
    public int screenWidth = 1080; // 화면의 너비
    public int screenHeight= 720; // 화면의 높이

    /** 캐릭터 생성자(위치를 지정하여 생성) */
    public Player(int x, int y) {
        this.x = screenWidth/2 - this.width/2;
        this.y = screenHeight - this.height;
        loadImage();
    }

    // 캐릭터를 왼쪽으로 이동
    public void moveLeft() {
        x -= 5;
    }
    // 캐릭터를 오른쪽으로 이동
    public void moveRight() {
        x += 5;
    }
    // 화면 너비 설정
    public void setScreenWidth(int width) {
        this.screenWidth = width;
    }

    /** 캐릭터의 상태를 업데이트하는 메소드 */
    public void update() {
        if (movingLeft) {
            x -= SPEED;
        }
        if (movingRight) {
            x += SPEED;
        }
        // 화면 바운더리 체크를 여기서 수행할 수 있음
        x = Math.max(0, x); // 화면 왼쪽 경계를 넘지 않도록
        x = Math.min(screenWidth - width, x); // 화면 오른쪽 경계를 넘지 않도록
    }
    // 캐릭터 이미지를 로드
    private void loadImage() {
        try {
            image = ImageIO.read(new File("images/player_f.png")); // 이미지 파일 경로
        } catch (IOException e) {
            e.printStackTrace();
            image = null;
        }
    }

    /** 캐릭터를 화면에 그리는 메소드 */
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null); // 이미지 그리기
        }
    }

    /** 캐릭터와 아이템의 충돌 여부를 판단하는 메소드 */
    public boolean collidesWith(Item item) {
        // 캐릭터와 아이템 사이의 거리를 계산
        int deltaX = Math.abs(x - item.getX());
        int deltaY = Math.abs(y - item.getY());

        // 캐릭터와 아이템이 충분히 가까울 때만 충돌 검사 수행
        if (deltaX < width && deltaY < height) {
            Rectangle characterBounds = new Rectangle(x, y, width, height);
            Rectangle itemBounds = new Rectangle(item.getX(), item.getY(), width, height);
            return characterBounds.intersects(itemBounds);
        }
        return false;
    }




    /** 키 입력에 따른 캐릭터 이동 상태 변경 메소드 */
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    // Getters and setters

    public int getX() { return x; } // 캐릭터의 x 좌표 반환
    public int getY() { return y; } // 캐릭터의 y 좌표 반환
    public void setX(int x) { this.x = x; } // 캐릭터의 x 좌표 설정
    public void setY(int y) { this.y = y; } // 캐릭터의 x 좌표 설정
}
