package NotableDeveloper.rank.domain.enums;

public enum Tier {
    A_PLUS("A+"),
    A("A0"),
    A_MINUS("A-"),
    B_PLUS("B+"),
    B("B0"),
    B_MINUS("B-"),
    C_PLUS("C+"),
    C("C0"),
    C_MINUS("C-"),
    D_PLUS("D+"),
    D("D0"),
    D_MINUS("D-"),
    F("F"),
    U("Unranked");

    private String tier;

    Tier(String tier) {
        this.tier = tier;
    }

    public String getTier(){
        return this.tier;
    }
}
