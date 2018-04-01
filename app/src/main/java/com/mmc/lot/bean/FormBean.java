package com.mmc.lot.bean;

/**
 * Created by zhangzd on 2018/3/29.
 */

public class FormBean {


    /**
     * token : bbdd44kk004ef5dc37424e4399a47908a5980ebb1524394662
     * transportInformation : {"logisticsCompany":"顺丰速运","consignor":{"name":"中国医药集团","address":"北京市海淀区知春路20号","contactNumber":"+8613800138000"},"consignee":{"name":"深圳市人民医院","address":"广东省深圳市罗湖区东门北路1017号","contactNumber":"+8613811138111"},"product":{"company":"中国医药集团","category":"流感疫苗"},"validRange":{"min":-30,"max":60}}
     * tagInformation : {"mac":"00:11:22:33:44:55","tagID":"aabbccddeeff","energy":100,"intervalTime":1,"gps":"123,23","category":"bluetooth","description":"record temperature"}
     */

    private String token;
    private TransportInformationBean transportInformation;
    private TagInformationBean tagInformation;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TransportInformationBean getTransportInformation() {
        return transportInformation;
    }

    public void setTransportInformation(TransportInformationBean transportInformation) {
        this.transportInformation = transportInformation;
    }

    public TagInformationBean getTagInformation() {
        return tagInformation;
    }

    public void setTagInformation(TagInformationBean tagInformation) {
        this.tagInformation = tagInformation;
    }

    public static class TransportInformationBean {
        /**
         * logisticsCompany : 顺丰速运
         * consignor : {"name":"中国医药集团","address":"北京市海淀区知春路20号","contactNumber":"+8613800138000"}
         * consignee : {"name":"深圳市人民医院","address":"广东省深圳市罗湖区东门北路1017号","contactNumber":"+8613811138111"}
         * product : {"company":"中国医药集团","category":"流感疫苗"}
         * validRange : {"min":-30,"max":60}
         */

        private String logisticsCompany;
        private ConsignorBean consignor;
        private ConsigneeBean consignee;
        private ProductBean product;
        private ValidRangeBean validRange;
        private String orderId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getLogisticsCompany() {
            return logisticsCompany;
        }

        public void setLogisticsCompany(String logisticsCompany) {
            this.logisticsCompany = logisticsCompany;
        }

        public ConsignorBean getConsignor() {
            return consignor;
        }

        public void setConsignor(ConsignorBean consignor) {
            this.consignor = consignor;
        }

        public ConsigneeBean getConsignee() {
            return consignee;
        }

        public void setConsignee(ConsigneeBean consignee) {
            this.consignee = consignee;
        }

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public ValidRangeBean getValidRange() {
            return validRange;
        }

        public void setValidRange(ValidRangeBean validRange) {
            this.validRange = validRange;
        }

        public static class ConsignorBean {


            public ConsignorBean(String name, String address, String contactNumber) {
                this.name = name;
                this.address = address;
                this.contactNumber = contactNumber;
            }

            /**
             * name : 中国医药集团
             * address : 北京市海淀区知春路20号
             * contactNumber : +8613800138000
             */

            private String name;
            private String address;
            private String contactNumber;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getContactNumber() {
                return contactNumber;
            }

            public void setContactNumber(String contactNumber) {
                this.contactNumber = contactNumber;
            }
        }

        public static class ConsigneeBean {

            public ConsigneeBean(String name, String address, String contactNumber) {
                this.name = name;
                this.address = address;
                this.contactNumber = contactNumber;
            }

            /**
             * name : 深圳市人民医院
             * address : 广东省深圳市罗湖区东门北路1017号
             * contactNumber : +8613811138111
             */

            private String name;
            private String address;
            private String contactNumber;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getContactNumber() {
                return contactNumber;
            }

            public void setContactNumber(String contactNumber) {
                this.contactNumber = contactNumber;
            }
        }

        public static class ProductBean {
            public ProductBean(String company, String category) {
                this.company = company;
                this.category = category;
            }

            /**
             * company : 中国医药集团
             * category : 流感疫苗
             */


            private String company;
            private String category;

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }
        }

        public static class ValidRangeBean {
            public ValidRangeBean(int min, int max) {
                this.min = min;
                this.max = max;
            }

            /**
             * min : -30
             * max : 60
             */

            private int min;
            private int max;

            public int getMin() {
                return min;
            }

            public void setMin(int min) {
                this.min = min;
            }

            public int getMax() {
                return max;
            }

            public void setMax(int max) {
                this.max = max;
            }
        }
    }

    public static class TagInformationBean {
        /**
         * mac : 00:11:22:33:44:55
         * tagID : aabbccddeeff
         * energy : 100
         * intervalTime : 1
         * gps : 123,23
         * category : bluetooth
         * description : record temperature
         */

        private String mac;
        private String tagID;
        private int energy;
        private int intervalTime;
        private String gps;
        private String category;
        private String description;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getTagID() {
            return tagID;
        }

        public void setTagID(String tagID) {
            this.tagID = tagID;
        }

        public int getEnergy() {
            return energy;
        }

        public void setEnergy(int energy) {
            this.energy = energy;
        }

        public int getIntervalTime() {
            return intervalTime;
        }

        public void setIntervalTime(int intervalTime) {
            this.intervalTime = intervalTime;
        }

        public String getGps() {
            return gps;
        }

        public void setGps(String gps) {
            this.gps = gps;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
