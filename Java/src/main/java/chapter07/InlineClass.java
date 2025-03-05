package chapter07;

class InlineClass {
    static class Shipment {
        String _shippingCompany;
        String _trackingNumber;

        Shipment(String shippingCompany, String trackingNumber) {
            this._shippingCompany = shippingCompany;
            this._trackingNumber = trackingNumber;
        }

        String trackingInfo() {
            return this.shippingCompany() + ": " + this.trackingNumber();
        }

        String shippingCompany() {
            return this._shippingCompany;
        }

        void shippingCompany(String arg) {
            this._shippingCompany = arg;
        }

        String trackingNumber() {
            return this._trackingNumber;
        }

        void trackingNumber(String arg) {
            this._trackingNumber = arg;
        }
    }

    static class Request {
        String _vendor;

        Request(String vendor) {
            this._vendor = vendor;
        }

        String vendor() {
            return this._vendor;
        }

        void vendor(String arg) {
            this._vendor = arg;
        }
    }
}
