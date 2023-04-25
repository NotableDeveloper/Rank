package NotableDeveloper.rank.domain.enums;

public enum Tier {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    F("F");

    private String tier;

    Tier(String tier) {
        this.tier = tier;
    }

    public String getTier(){
        return this.tier;
    }
}
