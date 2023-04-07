/**
 * @author Szarpak Jakub S25511
 */

package zad1;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Locale;

public class Time {
    public static String passed(String from, String to) {

        String[] arr1 = from.split("T");

        if (arr1.length == 2) {
            return passedWithHour(from, to);
        } else {
            return passedWithoutHour(from, to);
        }
    }

    private static String passedWithHour(String from, String to) {

        ZonedDateTime dateTimeFrom;
        ZonedDateTime dateTimeTo;
        try {
            dateTimeFrom = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneId.of("Europe/Warsaw"));
            dateTimeTo = LocalDateTime.parse(to, DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneId.of("Europe/Warsaw"));
        } catch (Exception e) {

            return "*** " + e;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter
                .ofPattern("dd MMMM yyyy (EEEE) 'godz. 'HH:mm", new Locale("pl"))
                .withZone(ZoneId.of("Europe/Warsaw"));

        int daysPassed = (int) dateTimeFrom.toLocalDate().until(dateTimeTo.toLocalDate(), ChronoUnit.DAYS);
        double weeksPassed = Math.round((((double) daysPassed) / 7) * 100);
        weeksPassed = weeksPassed / 100;
        Period period = Period.between(dateTimeFrom.toLocalDate(), dateTimeTo.toLocalDate());
        long years = period.get(ChronoUnit.YEARS);
        long months = period.get(ChronoUnit.MONTHS);
        long days = period.get(ChronoUnit.DAYS);
        long hours = dateTimeFrom.until(dateTimeTo, ChronoUnit.HOURS);
        long minutes = dateTimeFrom.until(dateTimeTo, ChronoUnit.MINUTES);


        String header = "Od " + dateTimeFrom.format(dateFormatter) + " do " + dateTimeTo.format(dateFormatter) + "\n";

        String mija = " - mija: ";
        if (daysPassed == 1) {
            mija += daysPassed + " dzień, tygodni ";
        } else {
            mija += daysPassed + " dni, tygodni ";
        }

        if ((weeksPassed * 10) % 10 == 0) {
            mija += (int) weeksPassed + "\n";
        } else {
            mija += weeksPassed + "\n";
        }


        String kalendarzowo = "";
        if (daysPassed >= 1) {
            kalendarzowo = " - kalendarzowo: ";
            if (years > 0) {
                if (years == 1)
                    kalendarzowo += years + " rok";
                else if (years % 10 >= 2 && years % 10 <= 4)
                    kalendarzowo += years + " lata";
                else
                    kalendarzowo += years + " lat";
            }
            if (months > 0) {
                if (kalendarzowo != " - kalendarzowo: ")
                    kalendarzowo += ", ";
                if (months == 1)
                    kalendarzowo += ", " + months + " miesiąc";
                else if (months % 10 >= 2 && months % 10 <= 4)
                    kalendarzowo += ", " + months + " miesiące";
                else
                    kalendarzowo += ", " + months + " miesięcy";
            }
            if (days > 0) {
                if (kalendarzowo != " - kalendarzowo: ")
                    kalendarzowo += ", ";
                if (days == 1)
                    kalendarzowo += days + " dzień";
                else
                    kalendarzowo += days + " dni";
            }
        }

        String odp;


        String godzin = " - godzin: " + hours + ", minut: " + minutes + '\n';

        odp = header + mija + godzin + kalendarzowo;

        return odp;
    }

    private static String passedWithoutHour(String from, String to) {


        LocalDate dateTimeFrom;
        LocalDate dateTimeTo;
        try {
            dateTimeFrom = LocalDate.parse(from, DateTimeFormatter.ISO_LOCAL_DATE).atTime(LocalTime.of(0,0,0)).atZone(ZoneId.of("Europe/Warsaw")).toLocalDate();
            dateTimeTo = LocalDate.parse(to, DateTimeFormatter.ISO_LOCAL_DATE).atTime(LocalTime.of(0,0,0)).atZone(ZoneId.of("Europe/Warsaw")).toLocalDate();
        } catch (Exception e) {

            return "*** " + e;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter
                .ofPattern("d MMMM yyyy (EEEE)", new Locale("pl"))
                .withZone(ZoneId.of("Europe/Warsaw"));


        int daysPassed = (int) dateTimeFrom.until(dateTimeTo, ChronoUnit.DAYS);
        double weeksPassed = Math.round((((double) daysPassed) / 7) * 100);
        weeksPassed = weeksPassed / 100;
        Period period = Period.between(dateTimeFrom, dateTimeTo);
        long years = period.get(ChronoUnit.YEARS);
        long months = period.get(ChronoUnit.MONTHS);
        long days = period.get(ChronoUnit.DAYS);


        String header = "Od " + dateTimeFrom.format(dateFormatter) + " do " + dateTimeTo.format(dateFormatter) + "\n";

        String mija = " - mija: ";
        if (daysPassed == 1) {
            mija += daysPassed + " dzień, tygodni ";
        } else {
            mija += daysPassed + " dni, tygodni ";
        }

        if ((weeksPassed * 10) % 10 == 0) {
            mija += (int) weeksPassed + "\n";
        } else {
            mija += weeksPassed + "\n";
        }

        String kalendarzowo = "";
        if (daysPassed >= 1) {
            kalendarzowo = " - kalendarzowo: ";
            if (years > 0) {
                if (years == 1)
                    kalendarzowo += years + " rok";
                else if (years % 10 >= 2 && years % 10 <= 4)
                    kalendarzowo += years + " lata";
                else
                    kalendarzowo += years + " lat";
            }
            if (months > 0) {
                if (kalendarzowo != " - kalendarzowo: ")
                    kalendarzowo += ", ";
                if (months == 1)
                    kalendarzowo += months + " miesiąc";
                else if (months % 10 >= 2 && months % 10 <= 4)
                    kalendarzowo += months + " miesiące";
                else
                    kalendarzowo += months + " miesięcy";
            }
            if (days > 0) {
                if (kalendarzowo != " - kalendarzowo: ")
                    kalendarzowo += ", ";
                if (days == 1)
                    kalendarzowo += days + " dzień";
                else
                    kalendarzowo += days + " dni";
            }
        }


        return header + mija + kalendarzowo;
    }
}
