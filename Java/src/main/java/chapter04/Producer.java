package chapter04;

import java.util.Optional;

public class Producer {
    private Province _province;
    private int _cost;
    private String _name;
    private int _production;// = 0;

    Producer(Province province, int cost, String name, int production) {
        this._province = province;
        this._cost = cost;
        this._name = name;
        this._production = production;
    }

    String name() {
        return _name;
    }

    int cost() {
        return _cost;
    }

    void cost(String arg) {
        _cost = Integer.parseInt(arg);
    }

    int production() {
        return _production;
    }

    void production(String arg) {
        Optional<Integer> amount = parseIntOptional(arg);
        int newProduction = amount.orElse(0);
        Province province = this._province;
        province.totalProduction(province.totalProduction() + newProduction - _production);
        this._production = newProduction;
    }

    public static Optional<Integer> parseIntOptional(String arg) {
        try {
            return Optional.of(Integer.parseInt(arg));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
