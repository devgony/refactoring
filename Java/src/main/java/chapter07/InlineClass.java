package chapter07;

class InlineClass {
    static class TrackingInformation {
        String _trackingNumber;

        TrackingInformation(String trackingNumber) {
            this._trackingNumber = trackingNumber;
        }

        String trackingNumber() {
            return this._trackingNumber;
        }

        void trackingNumber(String arg) {
            this._trackingNumber = arg;
        }
    }

    static class Shipment {
        String _shippingCompany;
        TrackingInformation _trackingInformation;

        Shipment(String shippingCompany, String trackingNumber) {
            this._shippingCompany = shippingCompany;
            this._trackingInformation = new TrackingInformation(trackingNumber);
        }

        String trackingInfo() {
            return this.shippingCompany() + ": " + this.trackingNumber();
        }

        TrackingInformation trackingInformation() {
            return this._trackingInformation;
        }

        void setTrackingInformation(TrackingInformation aTrackingInformation) {
            this._trackingInformation = aTrackingInformation;
        }

        String shippingCompany() {
            return this._shippingCompany;
        }

        void shippingCompany(String arg) {
            this._shippingCompany = arg;
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
