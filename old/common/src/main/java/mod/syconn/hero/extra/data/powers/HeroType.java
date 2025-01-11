package mod.syconn.hero.extra.data.powers;

import java.util.List;

public enum HeroType {
    
    NONE("No Powers", 0, 32, 0, List.of()),
    IRON_MAN("Iron Man's Powers", 1, 0, 0, List.of(new FlightPower()));
    
    private final String name;
    private final List<ISuperPower> powers;
    private final int id;
    private final int xPos;
    private final int yPos;

    HeroType(String name, int id, int xPos, int yPos, List<ISuperPower> powers) {
        this.name = name;
        this.id = id;
        this.xPos = xPos;
        this.yPos = yPos;
        this.powers = powers;
    }

    public String getName() {
        return name;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public int getId() {
        return id;
    }

    public List<ISuperPower> getPowers() {
        return powers;
    }

    public static HeroType getType(int id) {
        for (HeroType type : HeroType.values())
            if (type.id == id) return type;
        return HeroType.IRON_MAN;
    }
}
