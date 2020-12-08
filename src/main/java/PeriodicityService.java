import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public class PeriodicityService {


    public int sum(int a, int b) {
        return a+b;
    }


    public List<LocalDateTime> getTRsCreationDatesByXweekYdayPeriodicity(LocalDateTime dateDebutValidity,
                                                                          LocalDateTime dateFinValidity,
                                                                          Integer week_periodicity_week_count,
                                                                          List<DayOfWeek> selectedDays) {
        List<LocalDateTime> dateTRCreationOccurrences = new ArrayList<>();

        LocalDateTime firstMondayOfWeek = this.getFirstMondayOfWeek(dateDebutValidity);

        LocalDateTime validDate = dateDebutValidity;

        while (!validDate.isAfter(dateFinValidity)) {

            if (validDate.isAfter(firstMondayOfWeek.plusDays(6))) {
                firstMondayOfWeek = firstMondayOfWeek.plusWeeks(week_periodicity_week_count);
                validDate = firstMondayOfWeek;
            } else {
                if (selectedDays.contains(DayOfWeek.of(validDate.getDayOfWeek().getValue()))) {
                    dateTRCreationOccurrences.add(validDate);
                }
                validDate = validDate.plusDays(1);
            }
        }

        return dateTRCreationOccurrences;
    }

    // Periodicity3 : X day ordre Y dayOfWeek each Z month
    public List<LocalDateTime> getTRsCreationDatesByLeXDayOrderYDayOfWeekZMonthPeriodicity(LocalDateTime dateDebutValidity,
                                                                                            LocalDateTime dateFinValidity,
                                                                                            Integer orderOfDayEachChosenMonthPeriodicityDayOrder,
                                                                                            DayOfWeek orderOfDayEachChosenMonthPeriodicityDay,
                                                                                            Integer orderOfDayEachChosenMonthPeriodicityMonthCount) {
        List<LocalDateTime> dateTRCreationOccurrences = new ArrayList<>();

        LocalDateTime firstDayOfTheMonth = dateDebutValidity.withDayOfMonth(1);
        LocalDateTime validDate = dateDebutValidity;
        while (!validDate.isAfter(dateFinValidity)) {

            LocalDateTime targetDate = chosenDatePeriodicity3ForOneMonth(firstDayOfTheMonth, orderOfDayEachChosenMonthPeriodicityDayOrder, orderOfDayEachChosenMonthPeriodicityDay);
            if(!dateDebutValidity.isAfter(targetDate) && !targetDate.isAfter(dateFinValidity)) {
                dateTRCreationOccurrences.add(targetDate);
            }

            firstDayOfTheMonth = firstDayOfTheMonth.plusMonths(orderOfDayEachChosenMonthPeriodicityMonthCount);
            validDate = firstDayOfTheMonth;
        }

        return dateTRCreationOccurrences;
    }


    private LocalDateTime getFirstMondayOfWeek(LocalDateTime dateTime) {
        LocalDateTime dateOnFirstMonday = dateTime;
        while (dateOnFirstMonday.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            dateOnFirstMonday = dateOnFirstMonday.minusDays(1);
        }
        return dateOnFirstMonday;
    }

    // Periodicity2 : X dayNumber each Y month
    public List<LocalDateTime> getTRsCreationDatesByLeXDayYMonthPeriodicity(LocalDateTime dateDebutValidity,
                                                                             LocalDateTime dateFinValidity,
                                                                             Integer monthPeriodicityDayNumber,
                                                                             Integer monthPeriodicityMonthCount) {

        List<LocalDateTime> dateTRCreationOccurrences = new ArrayList<>();

        LocalDateTime firstDayOfTheMonth = dateDebutValidity.withDayOfMonth(1);
        LocalDateTime validDate = dateDebutValidity;
        while (!validDate.isAfter(dateFinValidity)) {
            if (validDate.getDayOfMonth() <= monthPeriodicityDayNumber && !getDateWithDayNumberOrWithLastDayOfTheMonth(validDate, monthPeriodicityDayNumber).isAfter(dateFinValidity)) {
                dateTRCreationOccurrences.add(getDateWithDayNumberOrWithLastDayOfTheMonth(validDate, monthPeriodicityDayNumber));
            }
            firstDayOfTheMonth = firstDayOfTheMonth.plusMonths(monthPeriodicityMonthCount);
            validDate = firstDayOfTheMonth;
        }

        return dateTRCreationOccurrences;
    }


    private LocalDateTime chosenDatePeriodicity3ForOneMonth(LocalDateTime dateWithFirstChosenMonth, int orderOfDay, DayOfWeek dayOfWeek) {
        LocalDateTime dayTarget = null;
        LocalDateTime validDate = dateWithFirstChosenMonth.plusWeeks(orderOfDay - 1);

        while (!validDate.isAfter(dateWithFirstChosenMonth.with(TemporalAdjusters.lastDayOfMonth()))) {
            if(validDate.getDayOfWeek().getValue() == dayOfWeek.getValue()) {
                dayTarget = validDate;
                break;
            }
            validDate = validDate.plusDays(1);
        }
        if(dayTarget == null) {
            throw new RuntimeException("Periodicity3: Impossible to find a date with orderOfDay {} and dayOfWeek {}");
        }
        return dayTarget;
    }

    private LocalDateTime getDateWithDayNumberOrWithLastDayOfTheMonth(LocalDateTime localDateTime, int dayNumber) {
        try {
            return localDateTime.withDayOfMonth(dayNumber);
        } catch (Exception e) {
            return localDateTime.with(TemporalAdjusters.lastDayOfMonth());
        }
    }


}
