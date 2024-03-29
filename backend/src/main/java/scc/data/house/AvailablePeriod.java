package scc.data.house;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import scc.utils.Constants;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AvailablePeriod {
    @Property("start_date")
    private String startDate;
    @Property("end_date")
    private String endDate;
    @Property("normal_price_per_day")
    private Float normalPricePerDay;
    @Property("promotion_price_per_day")
    private Float promotionPricePerDay;

    public AvailablePeriod() {
    }

    public AvailablePeriod(String startDate, String endDate, Float normalPricePerDay, Float promotionPricePerDay) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.normalPricePerDay = normalPricePerDay;
        this.promotionPricePerDay = promotionPricePerDay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Float getNormalPricePerDay() {
        return normalPricePerDay;
    }

    public void setNormalPricePerDay(Float normalPricePerDay) {
        this.normalPricePerDay = normalPricePerDay;
    }

    public Float getPromotionPricePerDay() {
        return promotionPricePerDay;
    }

    public void setPromotionPricePerDay(Float promotionPricePerDay) {
        this.promotionPricePerDay = promotionPricePerDay;
    }

    // [Period]
    public boolean containsPeriod(LocalDate start, LocalDate end) {
        LocalDate periodStart = getPeriodStart();
        LocalDate periodEnd = getPeriodEnd();

        return (periodStart.isBefore(start) || periodStart.isEqual(start)) && (periodEnd.isAfter(end) || periodEnd.isEqual(end));
    }

    // the startDate and EndDate are expected to be within the range of the period
    public Set<AvailablePeriod> subtract(LocalDate start, LocalDate end) {
        LocalDate periodStart = getPeriodStart();
        LocalDate periodEnd = getPeriodEnd();

        HashSet<AvailablePeriod> newPeriods = new HashSet<>();


        if (!periodStart.isEqual(start)) { // periodStart before start
            newPeriods.add(new AvailablePeriod(periodStart.toString(), start.toString(), getNormalPricePerDay(), getPromotionPricePerDay()));
        }
        if (!periodEnd.isEqual(end)) { // periodEnd after end
            newPeriods.add(new AvailablePeriod(end.toString(), periodEnd.toString(), getNormalPricePerDay(), getPromotionPricePerDay()));
        }

        return newPeriods;
    }

    private LocalDate getPeriodStart() {
        return LocalDate.parse(this.startDate, Constants.dateFormat);
    }

    private LocalDate getPeriodEnd() {
        return LocalDate.parse(this.endDate, Constants.dateFormat);
    }
}
