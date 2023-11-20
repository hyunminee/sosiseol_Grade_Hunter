// ItemType.java

/** 각 아이템 타입과 이에 해당하는 이미지 경로 및 효과 값을 정의 */
public enum ItemType {
    BLUE("hw_b.png", 10),
    GREEN("hw_g.png", 5),
    YELLOW("hw_y.png", -3),
    RED("hw_r.png", -5),
    TARDY("timer.png", -3), // 시간 감소 효과
    PRESENTATION("ppt.png", 15);

    private final String imagePath; // 아이템 이미지 경로
    private final int effectValue; // 아이템의 효과 값

    /** ItemType 생성자 */
    ItemType(String imagePath, int effectValue) {
        this.imagePath = imagePath;
        this.effectValue = effectValue;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getEffectValue() {
        return effectValue;
    }
}
