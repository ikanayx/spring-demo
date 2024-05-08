package space.itzkana.service;

public class CompAImpl implements ICompA {

    private final String name;

    public CompAImpl(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }
}
