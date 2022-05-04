package com.coffee.shop.enums;

public enum CreditCardType {
    MASTERCARD("Mastercard"),
    VISA("Visa"),
    AMEX("American Express");

    private final String cardType;

    private CreditCardType(String cardType) {
        this.cardType = cardType;
    }

    public String toString() {
        return cardType;
    }

    public static CreditCardType getCreditCardType(String text) {
        for (CreditCardType cct : CreditCardType.values()) {
            if (cct.cardType.equalsIgnoreCase(text)) {
                return cct;
            }
        }
        return null;
    }

    public static boolean isValid(Long numberCreditCard) {
        return (getSize(numberCreditCard) >= 13 && getSize(numberCreditCard) <= 16) &&
                (prefixMatched(numberCreditCard, 4) || prefixMatched(numberCreditCard, 5) ||
                        prefixMatched(numberCreditCard, 37) || prefixMatched(numberCreditCard, 6)) &&
                ((sumOfDoubleEvenPlace(numberCreditCard) + sumOfOddPlace(numberCreditCard)) % 10 == 0);
    }

    public static int sumOfDoubleEvenPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getDigit(Integer.parseInt(num.charAt(i) + "") * 2);

        return sum;
    }

    public static int getDigit(int number)
    {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    public static int sumOfOddPlace(long number)
    {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    public static boolean prefixMatched(long number, int d)
    {
        return getPrefix(number, getSize(d)) == d;
    }

    public static int getSize(long d)
    {
        String num = d + "";
        return num.length();
    }

    public static long getPrefix(long number, int k)
    {
        if (getSize(number) > k) {
            String num = number + "";
            return Long.parseLong(num.substring(0, k));
        }
        return number;
    }

}
