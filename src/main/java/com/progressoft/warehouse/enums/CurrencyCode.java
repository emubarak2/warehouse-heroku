package com.progressoft.warehouse.enums;

/**
 * Created by EYAD on 3/7/2019.
 */
public enum CurrencyCode {

    AFA, ALL, DZD, ADP, AOA, ARS, AMD, AWG, ATS, AZM, BSD, BHD, BDT, BBD, BYB, RYR,
    BEF, BZD, BMD, BTN, BOB, BOV, BAM, BWP, BRL, BND, BGL, BGN, BIF, KHR, CAD, CVE, KYD, MDL,
    CLP, CLF, CNY, HKD, MOP, COP, KMF, CDF, CRC, HRK, CUP, CYP, CZK, DOP, TPE, IDE, ECS, YUN,
    ECV, EGP, SVC, ERN, EEK, ETB, XEU, EUR, FKP, FJD, FIM, XAF, GMD, GEL, DEM, GHC, GIP, UYU,
    GRD, DKK, GTQ, GNF, GWP, GYD, HTG, HNL, HUF, ISK, INR, IDR, XDR, IRR, IQD, IEP, ILS, VND,
    JMD, JPY, JOD, KZT, KES, KPW, KRW, KWD, KGS, LAK, LVL, LBP, LSL, LRD, USD, VUV, MAD, YER,
    LYD, LTL, LUF, MKD, MGF, MWK, MYR, MVR, MTL, MRO, MUR, MXN, MNT, XPF, DJF, VEB,
    MZM, MMK, NAD, NPR, ANG, NLG, NIO, NGN, OMR, PKR, PAB, PGK, PYG, PEN, PHP, PLN, PTE, ZMK,
    QAR, ROL, RUR, RUB, RWF, FRF, XCD, SHP, WST, ITL, STD, SAR, SCR, SLL, SGD, SKK, SIT, UZS,
    SBD, SOS, ZAR, ESP, LKR, SDP, SRG, NOK, SZL, SEK, CHF, SYP, TWD, TJR, TZS, THB, XOF, ZRN,
    NZD, TOP, TTD, TND, TRL, TMM, AUD, UGX, UAH, AED, GBP, USS;


    public static Boolean checkCodeExists(String code) {
        for (CurrencyCode value : CurrencyCode.values()) {
            if (value.toString().equals(code.toUpperCase())) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

}