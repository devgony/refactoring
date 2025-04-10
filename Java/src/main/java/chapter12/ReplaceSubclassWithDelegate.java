package chapter12;

import java.util.Map;

class ReplaceSubclassWithDelegate {
    // Ex1.
    static class Show {
        String _name;
        double _price;
        String _talkback;

        Show(String name, double price, String talkBack) {
            this._name = name;
            this._price = price;
            this._talkback = talkBack;
        }

        String name() {
            return this._name;
        }

        double price() {
            return this._price;
        }
    }

    static class Extras {
        double _premiumFee;
        String _dinner;

        Extras(double premiumFee, String dinner) {
            this._premiumFee = premiumFee;
            this._dinner = dinner;
        }

        double premiumFee() {
            return this._premiumFee;
        }

        String dinner() {
            return this._dinner;
        }
    }

    static Booking createBooking(Show show, String date) {
        return new Booking(show, date);
    }

    static class Booking {
        Show _show;
        String _date;
        PremiumBookingDelegate _premiumDelegate;

        Booking(Show show, String date) {
            this._show = show;
            this._date = date;
        }

        boolean hasTalkback() {
            return (this._premiumDelegate != null && this._premiumDelegate.hasTalkback())
                    || (this._show._talkback != null && !this.isPeakDay());
        }

        double basePrice() {
            double result = this._show.price();
            if (this.isPeakDay())
                result += Math.round(result * 0.15);
            return (this._premiumDelegate != null) ? this._premiumDelegate.extendBasePrice(result) : result;
        }

        // My custom
        boolean isPeakDay() {
            return this._date.equals("Saturday") || this._date.equals("Sunday");
        }

        void _bePremium(Extras extras) {
            this._premiumDelegate = new PremiumBookingDelegate(this, extras);
        }
    }

    static Booking createPremiumBooking(Show show, String date, Extras extras) {
        Booking result = new Booking(show, date);
        result._bePremium(extras);

        return result;
    }

    static class PremiumBookingDelegate {
        Booking _host;
        Extras _extras;

        PremiumBookingDelegate(Booking hostBooking, Extras extras) {
            this._host = hostBooking;
            this._extras = extras;
        }

        boolean hasTalkback() {
            return this._host._show._talkback != null;
        }

        double extendBasePrice(double base) {
            return Math.round(base + this._extras.premiumFee());
        }

        boolean hasDinner() {
            return this._extras.dinner() != null && !this._host.isPeakDay();
        }
    }

    // Ex2.
    static Bird createBird(Map<String, Object> data) {
        switch ((String) data.get("type")) {
            case "NorweigianBlueParrot":
                return new NorwegianBlueParrot(data);
            default:
                return new Bird(data);
        }
    }

    static class Bird {
        String _name;
        String _plumage;
        SpeciesDelegate _speciesDelegate;

        SpeciesDelegate selectSpeciesDelegate(Map<String, Object> data) {
            switch (data.get("type").toString()) {
                case "EuropeanSwallow":
                    return new EuropeanSwallowDelegate();
                case "AfricanSwallow":
                    return new AfricanSwallowDelegate(data);
                case "NorweigianBlueParrot":
                    return new NorweigianBlueParrotDelegate(data, this);
                default:
                    return null;
            }
        }

        Bird(Map<String, Object> data) {
            this._name = (String) data.get("name");
            this._plumage = (String) data.get("plumage");
            this._speciesDelegate = selectSpeciesDelegate(data);
        }

        String name() {
            return this._name;
        }

        String plumage() {
            return this._plumage != null ? this._plumage : "average";
        }

        Double airSpeedVelocity() {
            return this._speciesDelegate != null ? this._speciesDelegate.airSpeedVelocity() : null;
        }
    }

    static class NorwegianBlueParrot extends Bird {
        double _voltage;
        boolean _isNailed;

        NorwegianBlueParrot(Map<String, Object> data) {
            super(data);
            this._voltage = (double) data.get("voltage");
            this._isNailed = (boolean) data.get("isNailed");
        }

        String plumage() {
            return this._speciesDelegate.plumage();
        }

        Double airSpeedVelocity() {
            return this._isNailed ? 0 : 10 + this._voltage / 10;
        }
    }

    static class SpeciesDelegate {
        Double airSpeedVelocity() {
            throw new UnsupportedOperationException("Unimplemented method 'airSpeedVelocity'");
        }

        String plumage() {
            throw new UnsupportedOperationException("Unimplemented method 'plumage'");
        }
    }

    static class EuropeanSwallowDelegate extends SpeciesDelegate {
        Double airSpeedVelocity() {
            return 35.0;
        }
    }

    static class AfricanSwallowDelegate extends SpeciesDelegate {
        int _numberOfCoconuts;

        AfricanSwallowDelegate(Map<String, Object> data) {
            this._numberOfCoconuts = (int) data.get("numberOfCoconuts");
        }

        Double airSpeedVelocity() {
            return (double) 40 - 2 * this._numberOfCoconuts;

        }
    }

    static class NorweigianBlueParrotDelegate extends SpeciesDelegate {
        Bird _bird;
        double _voltage;
        boolean _isNailed;

        NorweigianBlueParrotDelegate(Map<String, Object> data, Bird bird) {
            this._bird = bird;
            this._voltage = (double) data.get("voltage");
            this._isNailed = (boolean) data.get("isNailed");
        }

        Double airSpeedVelocity() {
            return this._isNailed ? 0 : 10 + this._voltage / 10;
        }

        String plumage() {
            if (this._voltage > 100)
                return "scorched";
            else
                return this._bird._plumage != null ? this._bird._plumage : "beautiful";
        }
    }
}
