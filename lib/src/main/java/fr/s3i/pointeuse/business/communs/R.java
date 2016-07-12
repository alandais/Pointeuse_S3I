package fr.s3i.pointeuse.business.communs;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public final class R
{

    private static final ResourceBundle R = PropertyResourceBundle.getBundle("resources", Locale.getDefault());

    public static String get(String name)
    {
        return R.getString(name);
    }

    public static String get(String name, Object... args)
    {
        return String.format(R.getString(name), args);
    }

}
