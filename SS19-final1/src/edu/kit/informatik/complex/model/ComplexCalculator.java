package edu.kit.informatik.complex.model;

public class ComplexCalculator {

    public static ComplexNumber add(ComplexNumber first, ComplexNumber second) {
        int realPart = first.realPart() + second.realPart();
        int imaginaryPart = first.imaginaryPart() + second.imaginaryPart();
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public static ComplexNumber subtract(ComplexNumber first, ComplexNumber second) {
        int realPart = first.realPart() - second.realPart();
        int imaginaryPart = first.imaginaryPart() - second.imaginaryPart();
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public static ComplexNumber multiply(ComplexNumber first, ComplexNumber second) {
        int realPart = first.realPart() * second.realPart() - first.imaginaryPart() * second.imaginaryPart();
        int imaginaryPart = first.realPart() * second.imaginaryPart() + first.imaginaryPart() * second.realPart();
        return new ComplexNumber(realPart, imaginaryPart);
    }

    public static ComplexNumber divide(ComplexNumber first, ComplexNumber second) {
        if (second.equals(new ComplexNumber(0, 0))) {
            throw new DivideByZeroException();
        }
        double divisor = second.norm() * second.norm();
        int realPart = (int) ((first.realPart() * second.realPart()
                + first.imaginaryPart() * second.imaginaryPart()) / divisor);
        int imaginaryPart = (int) ((first.imaginaryPart() * second.realPart()
                - first.realPart() * second.imaginaryPart()) / divisor);
        return new ComplexNumber(realPart, imaginaryPart);
    }
}
