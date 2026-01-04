package com.boscostay.searchservice.model;

public enum NoiseLevel {
    /**
     * Unknown or not applicable
     */
    NA,

    /**
     * Minimal or low-level noise
     */
    Quiet,

    /**
     * Noticeable daytime noise, quiet at night
     */
    Moderate,

    /**
     * Regular street or neighbor noise
     */
    Busy,

    /**
     * High traffic, nightlife, or construction nearby
     */
    Loud
} 
