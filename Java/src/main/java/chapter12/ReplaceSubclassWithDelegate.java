package chapter12;

import java.util.Map;

class ReplaceSubclassWithDelegate {
    // Ex1.
    static class Show {
        String _name;
        double _price;
        String _talkback;

        Show(String name, double price) {
            this._name = name;
            this._price = price;
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

        Booking(Show show, String date) {
            this._show = show;
            this._date = date;
        }

        boolean hasTalkback() {
            return this._show._talkback != null && !this.isPeakDay();
        }

        double basePrice() {
            double result = this._show.price();
            if (this.isPeakDay())
                result += Math.round(result * 0.15);
            return result;
        }

        // My custom
        boolean isPeakDay() {
            return this._date.equals("Saturday") || this._date.equals("Sunday");
        }
    }

    static PremiumBooking createPremiumBooking(Show show, String date, Extras extras) {
        return new PremiumBooking(show, date, extras);
    }

    static class PremiumBooking extends Booking {
        Extras _extras;

        PremiumBooking(Show show, String date, Extras extras) {
            super(show, date);
            this._extras = extras;
        }

        boolean hasTalkback() {
            return this._show._talkback != null;
        }

        double basePrice() {
            return Math.round(super.basePrice() + this._extras.premiumFee());
        }

        boolean hasDinner() {
            return this._extras.dinner() != null && !this.isPeakDay();
        }
    }

    static class PremiumBookingDelegate {
        Booking _host;
        Extras _extras;

        PremiumBookingDelegate(Booking hostBooking, Extras extras) {
            this._host = hostBooking;
            this._extras = extras;
        }

    }

    // Ex2.
    static Bird createBird(Map<String, Object> data) {
        switch ((String) data.get("type")) {
            case "EuropeanSwallow":
                return new EuropeanSwallow(data);
            case "AfricanSwallow":
                return new AfricanSwallow(data);
            case "NorweigianBlueParrot":
                return new NorwegianBlueParrot(data);
            default:
                return new Bird(data);
        }
    }

    static class Bird {
        String _name;
        String _plumage;

        Bird(Map<String, Object> data) {
            this._name = (String) data.get("name");
            this._plumage = (String) data.get("plumage");
        }

        String name() {
            return this._name;
        }

        String plumage() {
            return this._plumage != null ? this._plumage : "average";
        }

        Double airSpeedVelocity() {
            return null;
        }
    }

    static class EuropeanSwallow extends Bird {
        EuropeanSwallow(Map<String, Object> data) {
            super(data);
        }

        Double airSpeedVelocity() {
            return 35.0;
        }
    }

    static class AfricanSwallow extends Bird {
        int _numberOfCoconuts;

        AfricanSwallow(Map<String, Object> data) {
            super(data);
            this._numberOfCoconuts = (int) data.get("numberOfCoconuts");
        }

        Double airSpeedVelocity() {
            return (double) 40 - 2 * this._numberOfCoconuts;

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
            if (this._voltage > 100)
                return "scorched";
            else
                return this._plumage != null ? this._plumage : "beautiful";
        }

        Double airSpeedVelocity() {
            return this._isNailed ? 0 : 10 + this._voltage / 10;
        }
    }
}
