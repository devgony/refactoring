package chapter07;

class InlineClass {
    static class TrackingInformation {
        String _shippingCompany;
        String _trackingNumber;

        TrackingInformation(String shippingCompany, String trackingNumber) {
            this._shippingCompany = shippingCompany;
            this._trackingNumber = trackingNumber;
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

        String display() {
            return this.shippingCompany() + ": " + this.trackingNumber();
        }
    }

    static class Shipment {
        TrackingInformation _trackingInformation;

        Shipment(String shippingCompany, String trackingNumber) {
            this._trackingInformation = new TrackingInformation(shippingCompany, trackingNumber);
        }

        String trackingInfo() {
            return this._trackingInformation.display();
        }

        TrackingInformation trackingInformation() {
            return this._trackingInformation;
        }

        void setTrackingInformation(TrackingInformation aTrackingInformation) {
            this._trackingInformation = aTrackingInformation;
        }

        String shippingCompany() {
            return this._trackingInformation._shippingCompany;
        }

        void shippingCompany(String arg) {
            this._trackingInformation._shippingCompany = arg;
        }

        String trackingNumber() {
            return this._trackingInformation._trackingNumber;
        }

        void trackingNumber(String arg) {
            this._trackingInformation._trackingNumber = arg;
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
