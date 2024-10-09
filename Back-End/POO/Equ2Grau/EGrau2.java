public class EGrau2 {

    public double a, b, c;

    EGrau2(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double delta() {
        return (b*b) - (4*a*c);
    }

    public double calculaX1(double d) {
        return ((-b) + Math.sqrt(d)) / (2*a);
    }

    public double calculaX2(double d) {
        return ((-b) - Math.sqrt(d)) / (2*a);
    }

}