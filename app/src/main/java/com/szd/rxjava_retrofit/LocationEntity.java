package com.szd.rxjava_retrofit;

/**
 * Created by JY on 2016/12/6.
 */

public class LocationEntity {

    /**
     * status : OK
     * result : {"location":{"lng":121.490523,"lat":31.407452},"formatted_address":"上海市宝山区牡丹江路1599号","business":"友谊路,北翼商业街,吴淞","addressComponent":{"city":"上海市","direction":"near","distance":"0","district":"宝山区","province":"上海市","street":"牡丹江路","street_number":"1599号"},"cityCode":289}
     */

    private String status;
    private ResultBean result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * location : {"lng":121.490523,"lat":31.407452}
         * formatted_address : 上海市宝山区牡丹江路1599号
         * business : 友谊路,北翼商业街,吴淞
         * addressComponent : {"city":"上海市","direction":"near","distance":"0","district":"宝山区","province":"上海市","street":"牡丹江路","street_number":"1599号"}
         * cityCode : 289
         */

        private LocationBean location;
        private String formatted_address;
        private String business;
        private AddressComponentBean addressComponent;
        private int cityCode;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public AddressComponentBean getAddressComponent() {
            return addressComponent;
        }

        public void setAddressComponent(AddressComponentBean addressComponent) {
            this.addressComponent = addressComponent;
        }

        public int getCityCode() {
            return cityCode;
        }

        public void setCityCode(int cityCode) {
            this.cityCode = cityCode;
        }

        public static class LocationBean {
            /**
             * lng : 121.490523
             * lat : 31.407452
             */

            private double lng;
            private double lat;

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }
        }

        public static class AddressComponentBean {
            /**
             * city : 上海市
             * direction : near
             * distance : 0
             * district : 宝山区
             * province : 上海市
             * street : 牡丹江路
             * street_number : 1599号
             */

            private String city;
            private String direction;
            private String distance;
            private String district;
            private String province;
            private String street;
            private String street_number;

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreet_number() {
                return street_number;
            }

            public void setStreet_number(String street_number) {
                this.street_number = street_number;
            }
        }
    }
}
