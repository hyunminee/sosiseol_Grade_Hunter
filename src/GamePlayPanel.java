import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 게임 플레이 클래스 - 게임 이벤트 및 애니메이션 제어
 * @author 박현민, 정서윤
 * */
public class GamePlayPanel extends JPanel implements ActionListener, KeyListener {
    private BufferedImage backgroundImage;
    private Timer timer;
    private final int DELAY = 1000; // 타이머의 실행 간격 (밀리초 단위) 5ms로 설정
    private Player character; // 게임 내 플레이어 캐릭터 객체
    private List<Item> items; // 화면에 표시될 아이템들을 저장하는 리스트
    private int stage = 1; // 현재 게임의 스테이지 번호
    private int itemFallSpeed; // 아이템이 떨어지는 속도 (밀리초 단위)
    private double stageTime = 60000; // 각 스테이지의 지속 시간 (초)
    private int gaugeValue = 0; // 현재 게이지 값
    private final int GAUGE_PER_STAGE = 50; // 스테이지 당 필요한 게이지 증가량
    private final int MAX_STAGE = 8; // 최대 스테이지 번호
    private int maxGaugeValue; // 최대 게이지 값 (스테이지에 따라 변함)
    private Random rand; // 랜덤 이벤트 및 아이템 위치 생성에 사용될 Random 객체
    public ImageIcon stage1Popup = new ImageIcon(("images/popup/popup_1.png"));
    public ImageIcon stage2Popup = new ImageIcon(("images/popup/popup_2.png"));
    public ImageIcon stage3Popup = new ImageIcon(("images/popup/popup_3.png"));
    public ImageIcon stage4Popup = new ImageIcon(("images/popup/popup_4.png"));
    public ImageIcon stage5Popup = new ImageIcon(("images/popup/popup_5.png"));
    public ImageIcon stage6Popup = new ImageIcon(("images/popup/popup_6.png"));
    public ImageIcon stage7Popup = new ImageIcon(("images/popup/popup_7.png"));
    public ImageIcon stage8Popup = new ImageIcon(("images/popup/popup_8.png"));

    private JPanel blackOverlay;
    private KeyAdapter keyBlocker = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            e.consume();    // 키 이벤트 무시
        }
    };

    public GamePlayPanel() {
        // 배경 이미지 로드
        try {
            backgroundImage = ImageIO.read(new File("images/bg_gameplay.png")); // 이미지 파일 경로 지정
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }

        addKeyListener(this);
        setFocusable(true);
        setPreferredSize(new Dimension(1080, 720)); // 화면 크기 설정
        items = new ArrayList<>(); // 아이템 리스트 초기화
        rand = new Random(); // Random 객체 초기화
    }

    // 컴포넌트가 화면에 추가된 후에 호출될 메서드
    @Override
    public void addNotify() {
        super.addNotify();
        // 화면 크기를 기반으로 캐릭터의 실제 위치 설정
        character = new Player(getWidth() / 2 - 60, getHeight() - 120); // 캐릭터 생성
        character.setScreenWidth(getWidth()); // 캐릭터가 화면 너비를 인식하도록 설정
        initStage(); // 스테이지 초기화
    }

    // 스테이지 시작 시 호출되어 게임 환경을 초기화하는 메소드
    private void initStage() {
        maxGaugeValue = stage * GAUGE_PER_STAGE; // 스테이지별 최대 게이지 값을 계산
        itemFallSpeed = Math.max(100, 1000 - (stage * 300)); // 스테이지별 아이템 하강 속도 조절
        if (timer != null) {
            timer.stop(); // 이전 타이머가 있으면 정지
        }
        timer = new Timer(DELAY, this); // 새 타이머 설정 및 시작
        timer.start();
        showStagePopup(); // 스테이지 시작 팝업 호출
        addStageSpecificItems(); // 스테이지별 아이템 추가
    }

    // 스테이지 정보를 보여주는 팝업창을 표시하는 메소드
    private void showStagePopup() {

        setLayout(null);    //레이아웃 관리자를 사용하지 않음

        ImageIcon stageImage = null;
        if (stage == 1)
            stageImage = stage1Popup;
        else if(stage == 2)
            stageImage = stage2Popup;
        else if(stage == 3)
            stageImage = stage3Popup;
        else if(stage == 4)
            stageImage = stage4Popup;
        else if(stage == 5)
            stageImage = stage5Popup;
        else if(stage == 6)
            stageImage = stage6Popup;
        else if(stage == 7)
            stageImage = stage7Popup;
        else if(stage == 8)
            stageImage = stage8Popup;

        JLabel stage1popup = new JLabel(stageImage);
        stage1popup.setBounds(290, 160, stageImage.getIconWidth(), stageImage.getIconHeight()); // x, y 위치와 너비, 높이 설정
        add(stage1popup);

        //!!!여기부터 오버레이
        blackOverlay = new JPanel();
        blackOverlay.setBackground(new Color(0, 0, 0, 200));    // 검정색 오버레이창 투명도 설정
        blackOverlay.setBounds(0, 0, 1080, 720);
        blackOverlay.setVisible(false);
        add(blackOverlay);

        //Timer를 설정하여 지정된 시간 후에 레이블을 패널에서 제거
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(stage1popup);
                blackOverlay.setVisible(false);    // 검은색 패널 비활성화
                removeKeyListener(keyBlocker);     // 키 이벤트 리스너 제거
                revalidate();
                repaint();
            }
        });
        timer.setRepeats(false);    //한 번만 실행
        addKeyListener(keyBlocker); //타이머 시작전 키 이벤트 리스너 추가
        setFocusable(true);         // 패널이 키 이벤트를 받을 수 있도록 설정
        timer.start();
        blackOverlay.setVisible(true);      // 검은색 오버레이 패널 활성화하여 팝업과 함께 표시
    }

    // 아이템을 랜덤 위치에 스폰하는 메소드
    private void spawnItem(ItemType type, int speed) {
        Random rand = new Random();
        int panelWidth = getWidth();

        // 패널 너비가 50 이하일 경우, x 좌표를 0으로 설정
        int x = (panelWidth > 50) ? rand.nextInt(panelWidth - 50) : 0;

        int y = 0; // 아이템을 화면 맨 위에서 시작하도록 설정
        items.add(new Item(type.getImagePath(), x, y, type, speed));
    }

    // 스테이지별 아이템을 추가하는 메소드
    private void addStageSpecificItems() {
        items.clear(); // 리스트 초기화
        int numItemsToAdd = 20; // 스테이지마다 추가할 아이템 수

        int gridSize = 50; // 격자 크기
        int numColumns = (getWidth() - gridSize) / gridSize; // 격자 열 개수
        int numRows = (getHeight() - gridSize) / gridSize; // 격자 행 개수
        List<Point> availablePositions = new ArrayList<>();

        // 가능한 모든 격자 위치를 생성
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                availablePositions.add(new Point(i * gridSize, j * gridSize));
            }
        }

        int baseSpeed = 100; // 기본 하강 속도
        int speedIncrementPerStage = 10; // 스테이지당 속도 증가량
        int itemSpeed = baseSpeed + (stage - 1) * speedIncrementPerStage;

        // 아이템을 추가할 수 있는 위치가 충분할 때만 실행
        for (int i = 0; i < Math.min(numItemsToAdd, availablePositions.size()); i++) {
            ItemType itemType = selectItemTypeForStage();

            // 사용 가능한 위치 중 랜덤 위치 선택
            int randomIndex = rand.nextInt(availablePositions.size());
            Point selectedPosition = availablePositions.remove(randomIndex);

            // 아이템을 선택된 위치에 생성
            items.add(new Item(itemType.getImagePath(), selectedPosition.x, selectedPosition.y - getHeight(), itemType, itemSpeed));
        }
    }

    // 스테이지별 아이템 타입을 결정하는 메소드
    private ItemType selectItemTypeForStage() {
        ItemType itemType;
        if (stage <= 4) {
            // 스테이지 1~4: BLUE, GREEN, YELLOW, RED 아이템만 나옴
            itemType = ItemType.values()[rand.nextInt(4)]; // BLUE, GREEN, YELLOW, RED 중 하나를 랜덤으로 선택
        } else {
            // 스테이지 5~8: 모든 아이템이 나옴
            itemType = ItemType.values()[rand.nextInt(ItemType.values().length)]; // 모든 ItemType 중 하나를 랜덤으로 선택
        }
        return itemType;
    }

    // Timer 이벤트 처리를 위한 actionPerformed 메소드
    @Override
    public void actionPerformed(ActionEvent e) {
        // 캐릭터 상태 업데이트
        character.update();
        // 아이템의 상태를 업데이트하고 화면에서 움직임을 처리
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            item.move(); // 아이템 움직임 업데이트
            if (!item.isOnScreen()) {
                it.remove(); // 화면 밖으로 나간 아이템 제거
            } else if (character.collidesWith(item)) {
                applyItemEffect(item); // 아이템 효과 적용
                it.remove(); // 아이템 리스트에서 제거
            }
        }
        // 게임의 다른 상태들을 업데이트
        updateGameState();
        // 화면을 다시 그림
        repaint();
    }

    // 게임 상태 업데이트 메소드
    private void updateGameState() {
        // 게이지가 최대치에 도달했는지 체크
        if (gaugeValue >= maxGaugeValue) {
            if (stage < 8) {
                stage++; // 다음 스테이지로 진행
                initStage(); // 스테이지 초기화
            } else {
                gameClear(); // 게임 클리어 처리
            }
        }
        // 스테이지 시간이 0 이하가 되면 게임 오버
        if (stageTime <= 0) {
            gameOver();
        }

        // 타이머 업데이트
        stageTime -= DELAY; // 스테이지 시간 감소
        if (stageTime <= 0) {
            gameOver(); // 스테이지 시간 종료 시 게임 오버 처리
        }
    }

    /** 아이템과 충돌 시 게이지를 조절하는 메소드*/
    private void applyItemEffect(Item item) {
        // 아이템의 효과를 적용하는 로직
        // TARDY 아이템의 경우 시간 감소, 그 외에는 게이지 증가/감소
        if (item.getType() == ItemType.TARDY) {
            stageTime = Math.max(0, stageTime - Math.abs(item.getEffectValue()));
        } else {
            // 아이템이 게이지를 증가시키는 경우 최대값을 초과하지 않도록 한다.
            if (item.getEffectValue() > 0) {
                gaugeValue = Math.min(maxGaugeValue, gaugeValue + item.getEffectValue());
            } else {
                // 아이템이 게이지를 감소시키는 경우 0 미만으로 내려가지 않도록 한다.
                gaugeValue = Math.max(0, gaugeValue + item.getEffectValue());
            }
        }
    }

    /** 게임 클리어 메소드 */
    private void gameClear() {

    }

    /** 게임 오버 메소드 */
    private void gameOver() {

    }

    /** 화면 그리기를 위한 paintComponent 메소드 */
    @Override
    protected void paintComponent(Graphics g) {
        // 부모 클래스의 paintComponent() 호출로 기본 패널 그리기를 수행
        super.paintComponent(g);
        // 배경 이미지 그리기
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
        // 캐릭터를 화면에 그림
        character.draw(g);
        // 모든 아이템을 화면에 그림
        for (Item item : items) {
            item.draw(g);
        }
        // 게이지 바 그리기(위치, 색상, 폰트 조절 필요)
        // 게이지 비율에 따라 너비 계산
        int gaugeWidth = (int) ((double) gaugeValue / maxGaugeValue * getWidth());
        g.setColor(Color.GREEN);
        g.fillRect(10, 10, gaugeWidth, 20); // 게이지 바 그리기

        // 게이지 바 테두리 그리기
        g.setColor(Color.BLACK);
        g.drawRect(300, 88, getWidth() - 490, 30);

        // 타이머 그리기
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 30)); // 예: 글자체는 SansSerif, 스타일은 Bold, 크기는 20
        int seconds = (int) (stageTime/1000); // 전체 시간을 초 단위로 변환
        int milliseconds = (int) (stageTime%1000) / 10; // 밀리초 부분 계산
        g.drawString(String.format("%02d:%02d", seconds, milliseconds), getWidth() - 130, 105); // mm:ss 형식으로 표시

    }

    /** KeyListener 메소드들 */
    // 키가 타이핑될 때 호출. 여기서는 사용하지 않으므로 비워둠
    @Override
    public void keyTyped(KeyEvent e) {
    }

    // 사용자가 키를 눌렀을 때 호출
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            character.setMovingLeft(true); // 키가 눌렸을 때 캐릭터의 이동 상태를 true로 설정
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            character.setMovingRight(true);
        }
        repaint(); // 키가 눌렸을 때 화면을 다시 그림
    }

    // 사용자가 키를 놓았을 때 호출.
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            character.setMovingLeft(false); // 키가 놓였을 때 이동 상태를 false로 설정
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            character.setMovingRight(false);
        }
        repaint(); // 키가 놓였을 때 화면을 다시 그림
    }
}