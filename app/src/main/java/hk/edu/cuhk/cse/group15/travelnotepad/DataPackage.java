package hk.edu.cuhk.cse.group15.travelnotepad;

public class DataPackage {
    public static class TripData {
        public String name;
        public String origin;
        public String dst;
        public String date;

        public TripData(
            String name,
            String origin,
            String dst,
            String date){
            this.name = name;
            this.origin = origin;
            this.dst = dst;
            this.date = date;
        }
    }
}
