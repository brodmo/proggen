package edu.kit.informatik.SS19_AB1_complex.model;

import edu.kit.informatik.SS19_AB1_complex.MagicStrings;

import java.util.Objects;

public class ComplexNumber implements Comparable<ComplexNumber> {

    private final int realPart;
    private final int imaginaryPart;

    public ComplexNumber(int realPart, int imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public int realPart() {
        return realPart;
    }

    public int imaginaryPart() {
        return imaginaryPart;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        ComplexNumber complexNumber = (ComplexNumber) o;
        return complexNumber.realPart == realPart
                && complexNumber.imaginaryPart == imaginaryPart;
    }

    @Override
    public int compareTo(ComplexNumber complexNumber) {
        return Double.compare(norm(), complexNumber.norm());
    }

    public double norm() {
        return Math.sqrt((double) realPart * realPart + imaginaryPart * imaginaryPart);
    }

    @Override
    public String toString() {
        return "" + MagicStrings.COMPLEX_OPEN_BRACKET + realPart + MagicStrings.SPACE + MagicStrings.ADD
            + MagicStrings.SPACE + imaginaryPart + MagicStrings.COMPLEX_SYMBOL + MagicStrings.COMPLEX_CLOSED_BRACKET;
    }

    @Override
    public int hashCode() {
        return Objects.hash(realPart, imaginaryPart);
    }

}
