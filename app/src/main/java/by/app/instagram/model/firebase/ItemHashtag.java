package by.app.instagram.model.firebase;

public class ItemHashtag {

    String name;
    boolean isCopy = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCopy() {
        return isCopy;
    }

    public void setCopy(boolean copy) {
        isCopy = copy;
    }
}
