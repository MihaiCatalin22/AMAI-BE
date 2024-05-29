package prjcb04.amaiproject2024.business.Implementation;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import prjcb04.amaiproject2024.domain.TimeSlot;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

@Service
public class AvailableTuesdays {
    //ALL THE AVAILABLE TUESDAYS ABIDING BY THE RULES OF AMAI
    int startYear = 2023;
    int endYear = 2024;
    public List<LocalDate> firstEducationalPeriod(){
        List<LocalDate> allTuesdays = generate();
        LocalDate startDate = LocalDate.of(startYear,Month.SEPTEMBER,1);
        LocalDate endDate = LocalDate.of(endYear,Month.FEBRUARY,10);
        return rangeBetween(allTuesdays,startDate,endDate);
    }
    public List<LocalDate> secondEducationalPeriod(){
        List<LocalDate> allTuesdays = generate();
        LocalDate startDate = LocalDate.of(endYear,Month.FEBRUARY,19);
        LocalDate endDate = LocalDate.of(endYear,Month.JUNE,29);
        return rangeBetween(allTuesdays,startDate,endDate);
    }
    public List<LocalDate> generate() {


        Set<LocalDate> holidays = new HashSet<>();
        // Add holidays to the set
        addHolidayRange(holidays, LocalDate.of(startYear, Month.OCTOBER, 16), LocalDate.of(startYear, Month.OCTOBER, 22)); // Autumn break
        addHolidayRange(holidays, LocalDate.of(endYear, Month.OCTOBER, 16), LocalDate.of(endYear, Month.OCTOBER, 22)); // Autumn break

        addHolidayRange(holidays, LocalDate.of(startYear, Month.DECEMBER, 25), LocalDate.of(endYear, Month.JANUARY, 7)); // Christmas break
        addHolidayRange(holidays, LocalDate.of(endYear, Month.DECEMBER, 25), LocalDate.of(endYear + 1, Month.JANUARY, 7)); // Christmas break

        addHolidayRange(holidays, LocalDate.of(startYear, Month.FEBRUARY, 12), LocalDate.of(startYear, Month.FEBRUARY, 18)); // Carnival break
        addHolidayRange(holidays, LocalDate.of(endYear, Month.FEBRUARY, 12), LocalDate.of(endYear, Month.FEBRUARY, 18)); // Carnival break

        holidays.add(LocalDate.of(startYear, Month.MARCH, 29)); // Good Friday
        holidays.add(LocalDate.of(endYear, Month.MARCH, 29)); // Good Friday

        holidays.add(LocalDate.of(startYear, Month.APRIL, 1)); // Easter Monday
        holidays.add(LocalDate.of(endYear, Month.APRIL, 1)); // Easter Monday

        addHolidayRange(holidays, LocalDate.of(startYear, Month.APRIL, 29), LocalDate.of(startYear, Month.MAY, 5)); // May break
        addHolidayRange(holidays, LocalDate.of(endYear, Month.APRIL, 29), LocalDate.of(endYear, Month.MAY, 5)); // May break

        addHolidayRange(holidays, LocalDate.of(startYear, Month.MAY, 9), LocalDate.of(startYear, Month.MAY, 10)); // Ascension Day and the day after
        addHolidayRange(holidays, LocalDate.of(endYear, Month.MAY, 9), LocalDate.of(endYear, Month.MAY, 10)); // Ascension Day and the day after

        holidays.add(LocalDate.of(startYear, Month.MAY, 20)); // Whit Monday
        holidays.add(LocalDate.of(endYear, Month.MAY, 20)); // Whit Monday

        addHolidayRange(holidays, LocalDate.of(startYear, Month.JULY, 26), LocalDate.of(startYear, Month.SEPTEMBER, 2)); // Summer break
        addHolidayRange(holidays, LocalDate.of(endYear, Month.JULY, 26), LocalDate.of(endYear, Month.SEPTEMBER, 2)); // Summer break

        // Add first week and last two weeks of educational periods to the holidays
        addFirstWeekAndLastTwoWeeks(holidays, LocalDate.of(startYear, Month.SEPTEMBER, 4), LocalDate.of(endYear, Month.FEBRUARY, 11)); // Educational period 1
        addFirstWeekAndLastTwoWeeks(holidays, LocalDate.of(endYear, Month.FEBRUARY, 19), LocalDate.of(endYear + 1, Month.JUNE, 29)); // Educational period 2

        List<LocalDate> tuesdays = getFilteredTuesdays(startYear, endYear, holidays);
        //Map<LocalDate, List<TimeSlot>> tuesdayTimeSlots = generateTimeSlotsForTuesdays(tuesdays);
        return tuesdays;
    }

    private static void addHolidayRange(Set<LocalDate> holidays, LocalDate start, LocalDate end) {
        LocalDate date = start;
        while (!date.isAfter(end)) {
            holidays.add(date);
            date = date.plusDays(1);
        }
    }
    private static List<LocalDate> rangeBetween(List<LocalDate> tuesdays, LocalDate start, LocalDate end) {
        List<LocalDate> inThePeriod = new ArrayList<>();
        for (LocalDate tuesday:tuesdays){
            if (!tuesday.isBefore(start)&&!tuesday.isAfter(end)){
                inThePeriod.add(tuesday);
            }
        }
       return inThePeriod;
    }

    private static void addFirstWeekAndLastTwoWeeks(Set<LocalDate> holidays, LocalDate start, LocalDate end) {
        // Add the first week
        addHolidayRange(holidays, start, start.plusDays(6));
        // Add the last two weeks
        addHolidayRange(holidays, end.minusDays(13), end);
    }

    public static List<LocalDate> getFilteredTuesdays(int startYear, int endYear, Set<LocalDate> holidays) {
        List<LocalDate> tuesdays = new ArrayList<>();

        for (int year = startYear; year <= endYear; year++) {
            for (Month month : Month.values()) {
                LocalDate date = LocalDate.of(year, month, 1);

                while (date.getMonth() == month) {
                    if (date.getDayOfWeek() == DayOfWeek.TUESDAY &&
                            isValidTuesday(date, holidays)) {
                        tuesdays.add(date);
                    }
                    date = date.plusDays(1);
                }
            }
        }

        return tuesdays;
    }

    public static boolean isValidTuesday(LocalDate date, Set<LocalDate> holidays) {
        Month month = date.getMonth();
        int day = date.getDayOfMonth();

        // Exclude first week of September and February
        if ((month == Month.SEPTEMBER || month == Month.FEBRUARY) && day <= 7) {
            return false;
        }

        // Exclude last two weeks of June and January
        if ((month == Month.JUNE && day >= 16) || (month == Month.JANUARY && day >= 16)) {
            return false;
        }

        // Exclude holidays
        if (holidays.contains(date)) {
            return false;
        }

        return true;
    }

    private static Map<LocalDate, List<TimeSlot>> generateTimeSlotsForTuesdays(List<LocalDate> tuesdays) {
        Map<LocalDate, List<TimeSlot>> tuesdayTimeSlots = new HashMap<>();
        for (LocalDate tuesday : tuesdays) {
            List<TimeSlot> slots = new ArrayList<>();
            LocalTime startTime = LocalTime.of(16, 0);
            LocalTime endTime = startTime.plusMinutes(10);
            while (!endTime.isAfter(LocalTime.of(17, 0))) {
                slots.add(new TimeSlot(startTime, endTime));
                startTime = startTime.plusMinutes(10);
                endTime = endTime.plusMinutes(10);
            }
            tuesdayTimeSlots.put(tuesday, slots);
        }
        return tuesdayTimeSlots;
    }
}

