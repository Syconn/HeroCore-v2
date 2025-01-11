package mod.syconn.hero.extra.data.powers;

public class FlightPower implements ISuperPower {

    public void use() {
        System.out.println("Using Flight Power");
    }

    public boolean canUse() {
        return true;
    }
}
