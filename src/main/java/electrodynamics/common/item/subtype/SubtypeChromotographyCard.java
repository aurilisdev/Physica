package electrodynamics.common.item.subtype;

import electrodynamics.api.ISubtype;

public enum SubtypeChromotographyCard implements ISubtype {

    nitrogen, oxygen, argon, carbondioxide, sulfurdioxide; // 78.1, 20.9, 0.93, 0.04


    @Override
    public String tag() {
        return "chromotographycard" + name();
    }

    @Override
    public String forgeTag() {
        return "chromotography_card" + name();
    }

    @Override
    public boolean isItem() {
        return true;
    }
}
