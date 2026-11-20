package com.example.calcsy;

public class BmiEngine {

    public static double calculateBMI(double weightKg, double heightCm) {
        if (weightKg <= 0 || heightCm <= 0) return 0.0;
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    public static String getCategory(double bmi) {
        if (bmi < 18.5) {
            return "KURANG BERAT BADAN";
        } else if (bmi < 25.0) {
            return "BERAT BADAN IDEAL";
        } else if (bmi < 30.0) {
            return "KELEBIHAN BERAT BADAN";
        } else {
            return "OBESITAS";
        }
    }

    public static String getCategoryColorHex(double bmi) {
        if (bmi < 18.5) {
            return "#EAB308";
        } else if (bmi < 25.0) {
            return "#16A34A";
        } else if (bmi < 30.0) {
            return "#F97316";
        } else {
            return "#DC2626";
        }
    }

    public static String getAdvice(double bmi) {
        return getAdvice(bmi, true);
    }

    public static String getAdvice(double bmi, boolean isMale) {
        String title = isMale ? "Pria" : "Wanita";
        if (bmi < 18.5) {
            return "Kategori Kurang Berat Badan untuk " + title + ". Disarankan untuk meningkatkan asupan nutrisi seimbang dan pembentukan massa otot.";
        } else if (bmi < 25.0) {
            return "Kerja bagus! Postur tubuh ideal untuk " + title + ". Pertahankan pola makan seimbang dan olahraga teratur.";
        } else if (bmi < 30.0) {
            return "Kategori Kelebihan Berat Badan untuk " + title + ". Cobalah untuk mengatur pola makan dan tingkatkan aktivitas fisik harian.";
        } else {
            return "Kategori Obesitas untuk " + title + ". Disarankan berkonsultasi dengan profesional kesehatan atau ahli gizi.";
        }
    }
}
