package fr.s3i.pointeuse.business.pointages.interactors.internal;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public enum Periode
{
    // @formatter:off
    ANNEE(Calendar.DAY_OF_YEAR, Calendar.YEAR),
    MOIS(Calendar.DAY_OF_MONTH, Calendar.MONTH),
    SEMAINE(Calendar.DAY_OF_WEEK, Calendar.WEEK_OF_YEAR),
    JOUR(Calendar.HOUR, Calendar.DAY_OF_MONTH);
    // @formatter:on

    private final int toReset;
    private final int toIncrease;

    private Periode(int toReset, int toIncrease)
    {
        this.toReset = toReset;
        this.toIncrease = toIncrease;
    }

    public Date getDebutPeriode(Date reference)
    {
        Calendar calendar = getCalendar(reference);
        reset(calendar);
        return calendar.getTime();
    }

    public Date getFinPeriode(Date reference)
    {
        Calendar calendar = getCalendar(reference);
        reset(calendar);
        increase(calendar);
        return calendar.getTime();
    }

    private void reset(Calendar calendar)
    {
        if (toReset == Calendar.DAY_OF_WEEK)
        {
            calendar.set(toReset, calendar.getFirstDayOfWeek());
        }
        else
        {
            calendar.set(toReset, calendar.getActualMinimum(toReset));
        }
    }

    private void increase(Calendar calendar)
    {
        calendar.add(toIncrease, 1);
    }

    private Calendar getCalendar(Date date)
    {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}