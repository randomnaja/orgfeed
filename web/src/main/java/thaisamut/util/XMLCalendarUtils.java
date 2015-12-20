package thaisamut.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.*;
import java.text.DateFormatSymbols;


import org.apache.commons.lang3.time.DateUtils;

public final class XMLCalendarUtils
{
    private static final DatatypeFactory DATATYPE_FACTORY;

    static
    {
        try
        {
            DATATYPE_FACTORY = DatatypeFactory.newInstance();
        }
        catch (DatatypeConfigurationException e)
        {
            throw new IllegalStateException("Unable to instantiate class DatatypeFactory");
        }
    }

    private XMLCalendarUtils() { }

    public static XMLGregorianCalendar createToday()
    {
        return DATATYPE_FACTORY.newXMLGregorianCalendar(
                new GregorianCalendar(Locale.US));
    }

    public static XMLGregorianCalendar create(Date date)
    {
        GregorianCalendar cal = new GregorianCalendar(Locale.US);

        cal.setTime(date);

        return create(cal);
    }

    public static XMLGregorianCalendar create(Calendar cal)
    {
        GregorianCalendar c = new GregorianCalendar(Locale.US);

        c.setTimeInMillis(cal.getTimeInMillis());

        return create(c);
    }

    public static XMLGregorianCalendar create(GregorianCalendar cal)
    {
        return DATATYPE_FACTORY.newXMLGregorianCalendar(cal);
    }

    public static XMLGregorianCalendar create(int year, int month, int day)
    {
        return create(year, month, day, 0, 0, 0);
    }

    public static XMLGregorianCalendar create(int year, int month, int day, int hr, int min, int sec)
    {
        GregorianCalendar cal = new GregorianCalendar(Locale.US);

        cal.clear();
        cal.setLenient(false);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hr);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);

        return DATATYPE_FACTORY.newXMLGregorianCalendar(cal);
    }

    public static Calendar toCalendar(XMLGregorianCalendar cal)
    {
        if (null != cal)
        {
            Calendar c = Calendar.getInstance(Locale.US);

            c.setTime(toDate(cal));

            return c;
        }

        return null;
    }    

    public static Date toDate(XMLGregorianCalendar cal)
    {
        if (null != cal)
        {
            return cal.toGregorianCalendar().getTime();
        }

        return null;
    }
    
    //input parameter : index expect value = 0 - 11
    //output parameter = thai month string
    public static String strThaiMonthByIndex(Integer index)
    {  
        return ((new DateFormatSymbols(new Locale("th", "TH"))).getMonths())[index];
    }
    
    //input parameter : calMonth expect value = Calendar.JANUARY..... Calendar.DECEMBER
    //output parameter = thai month string
    public static String strThaiMonthByCalendarMonth(Integer calMonth)
    {
    	return ((new DateFormatSymbols(new Locale("th", "TH"))).getMonths())[calMonth];
    }
    
    //output parameter = String[] of thai month string
    public static String[] strListThaiMonth()
    {
    	return ((new DateFormatSymbols(new Locale("th", "TH"))).getMonths());
    }
    
    //input parameter : d expect value = java.util.Date
    //output parameter java.util.Date with last date of month
    public static Date getLastDateOfMonth(Date d)
    {
    	Calendar c = Calendar.getInstance();
    	c.setTime(d);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }
    
    
}
