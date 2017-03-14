
package de.unratedfilms.movielens.fmlmod.config;

import org.apache.commons.lang3.Validate;

public class Format {

    public static final Format[] KNOWN_FORMATS  = {
            new Format(1.00f, "Square"),
            new Format(1.20f, "Fox Movietone"),
            new Format(1.25f, "5:4"),
            new Format(1.33f, "4:3"),
            new Format(1.37f, "Academy Standard Flat"),
            new Format(1.43f, "IMAX"),
            new Format(1.66f, "European Widescreen"),
            new Format(1.77f, "16:9"),
            new Format(1.85f, "Academy Standard Flat Matted"),
            new Format(2.00f, "Univisium"),
            new Format(2.20f, "Super Panavision 70"),
            new Format(2.35f, "Anamorphic v1"),
            new Format(2.39f, "Anamorphic v2"),
            new Format(2.76f, "Ultra Panavision 70"),
            new Format(3.00f, "3:1"),
            new Format(4.00f, "4:1")
    };

    public static final Format   DEFAULT_FORMAT = KNOWN_FORMATS[0];

    private final String         name;

    // Width-to-height ratio; e.g., for 2.35:1, this would be 2.35
    private final float          aspectRatio;

    public Format(float aspectRatio, String name) {

        Validate.notBlank(name, "Format name cannot be blank");
        Validate.isTrue(aspectRatio > 0, "Width-to-height aspect ratio must be > 0");

        this.name = name;
        this.aspectRatio = aspectRatio;
    }

    public String getName() {

        return name;
    }

    public float getAspectRatio() {

        return aspectRatio;
    }

}
