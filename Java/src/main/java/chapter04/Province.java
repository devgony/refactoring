package chapter04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class Province {
    private String _name;
    private List<Producer> _producers;
    private int _totalProduction;
    private int _demand;
    private int _price;

    Province(JsonNode doc) {
        this._name = doc.get("name").asText();
        this._producers = new ArrayList<>();
        this._totalProduction = 0;
        this._demand = doc.get("demand").asInt();
        this._price = doc.get("price").asInt();
        doc.get("producers").forEach(d -> this.addProducer(
                new Producer(this, d.get("cost").asInt(), d.get("name").asText(), d.get("production").asInt())));
    }

    void addProducer(Producer arg) {
        _producers.add(arg);
        _totalProduction += arg.production();
    }

    String name() {
        return _name;
    }

    List<Producer> producers() {
        return Collections.unmodifiableList(this._producers);
    }

    int totalProduction() {
        return this._totalProduction;
    }

    void totalProduction(int arg) {
        this._totalProduction = arg;
    }

    int demand() {
        return this._demand;
    }

    void demand(String arg) {
        this._demand = Integer.parseInt(arg);
    }

    int price() {
        return this._price;
    }

    void price(String arg) {
        this._price = Integer.parseInt(arg);
    }

    int shortfall() {
        return this._demand - this.totalProduction();
    }

    int profit() {
        return this.demandValue() - this.demandCost();
    }

    private int demandValue() {
        return this.satisfiedDemand() * this._price;
    }

    private int satisfiedDemand() {
        return Math.min(this._demand, this.totalProduction());
    }

    private int demandCost() {
        int remainingDemand = this._demand;
        int result = 0;
        this._producers.sort((a, b) -> a.cost() - b.cost());
        for (Producer p : this._producers) {
            int contribution = Math.min(remainingDemand, p.production());
            remainingDemand -= contribution;
            result += contribution * p.cost();
        }
        ;
        return result;
    }

}
