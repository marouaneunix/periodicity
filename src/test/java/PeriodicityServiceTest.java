import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.List.of;

public class PeriodicityServiceTest {

    PeriodicityService periodicityService;
    @BeforeEach
    void setUp() {
        this.periodicityService = new PeriodicityService();
    }


    @Test
    public void should_each_1_weeks_on_MONDAY() {
        List<LocalDateTime> days = this.periodicityService.getTRsCreationDatesByXweekYdayPeriodicity(
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0),
                LocalDateTime.of(2020, 3, 30, 0, 0, 0, 0),
                1,
                of(DayOfWeek.MONDAY));

        Assertions.assertThat(days).hasSize(13);
        Assertions.assertThat(days).extracting(LocalDateTime::getDayOfWeek).containsOnly(DayOfWeek.MONDAY);
    }

    @Test
    public void should_each_2_weeks_on_MONDAY_and_TUESDAY() {
        List<LocalDateTime> days = this.periodicityService.getTRsCreationDatesByXweekYdayPeriodicity(
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0),
                LocalDateTime.of(2020, 3, 30, 0, 0, 0, 0),
                2,
                of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY));

        Assertions.assertThat(days).hasSize(12);
        Assertions.assertThat(days)
                .extracting(LocalDateTime::getDayOfWeek)
                .contains(DayOfWeek.MONDAY, DayOfWeek.TUESDAY);

        Assertions.assertThat(days)
                .extracting(LocalDateTime::getDayOfWeek)
                .filteredOn(dayOfWeek -> dayOfWeek.equals(DayOfWeek.MONDAY))
                .hasSize(6)
                .containsOnly(DayOfWeek.MONDAY);
        Assertions.assertThat(days)
                .extracting(LocalDateTime::getDayOfWeek)
                .filteredOn(dayOfWeek -> dayOfWeek.equals(DayOfWeek.TUESDAY))
                .hasSize(6)
                .containsOnly(DayOfWeek.TUESDAY);
    }

    @Test
    public void should_second_MONDAY_every_month() {
        List<LocalDateTime> days = this.periodicityService.getTRsCreationDatesByLeXDayOrderYDayOfWeekZMonthPeriodicity(
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0),
                LocalDateTime.of(2020, 3, 30, 0, 0, 0, 0),
                2,
                DayOfWeek.MONDAY,
                1
        );

        System.out.println(days);
        Assertions.assertThat(days).hasSize(3);
        Assertions.assertThat(days)
                .extracting(LocalDateTime::getDayOfWeek)
                .containsOnly(DayOfWeek.MONDAY);
    }

    @Test
    public void should_second_MONDAY_every_2_months() {
        List<LocalDateTime> days = this.periodicityService.getTRsCreationDatesByLeXDayOrderYDayOfWeekZMonthPeriodicity(
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0),
                LocalDateTime.of(2020, 3, 30, 0, 0, 0, 0),
                2,
                DayOfWeek.MONDAY,
                2
        );

        Assertions.assertThat(days).hasSize(2);
        Assertions.assertThat(days)
                .extracting(LocalDateTime::getDayOfWeek)
                .containsOnly(DayOfWeek.MONDAY);
    }

    @Test
    public void should_15_each_month() {
        List<LocalDateTime> days = this.periodicityService.getTRsCreationDatesByLeXDayYMonthPeriodicity(
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0),
                LocalDateTime.of(2020, 3, 30, 0, 0, 0, 0),
                15,
                1);

        Assertions.assertThat(days).hasSize(3);
        Assertions.assertThat(days).extracting(LocalDateTime::getDayOfMonth).containsOnly(15);
    }

    @Test
    public void should_10_each_2_months() {
        List<LocalDateTime> days = this.periodicityService.getTRsCreationDatesByLeXDayYMonthPeriodicity(
                LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0),
                LocalDateTime.of(2020, 3, 30, 0, 0, 0, 0),
                10,
                2);

        Assertions.assertThat(days).hasSize(2);
        Assertions.assertThat(days).extracting(LocalDateTime::getDayOfMonth).containsOnly(10);
    }
}
