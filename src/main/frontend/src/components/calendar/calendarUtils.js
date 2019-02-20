import flow from 'lodash/fp/flow';
import moment from 'moment';

export const createCalendar = (year, publicHolidays, personalData) =>
  flow([createMonths, createDays])(year, publicHolidays, personalData);

const fillDaysWithData = (day, publicHoliday, personalData) =>
  flow([
    checkIfPublicHoliday,
    checkIfPersonalDay,
    checkIfWeekend,
    checkIfToday
  ])(day, publicHoliday, personalData);

const createMonths = (year, publicHolidays, personalData) => {
  const months = moment.months().map(month => {
    return {
      name: month,
      year
    };
  });
  return { months, publicHolidays, personalData };
};

const createDays = monthObject => {
  const monthsAndDays = monthObject.months.map(month => {
    const days = [];
    for (let day = 1; day <= 31; day++) {
      const dayObject = {
        date: moment(`${day}-${month.name}-${month.year}`, 'DD-MMMM-YYYY')
      };
      day > moment(`${month.year}-${month.name}`, 'YYYY-MMMM').daysInMonth()
        ? days.push(null)
        : days.push(
            fillDaysWithData(
              dayObject,
              monthObject.publicHolidays,
              monthObject.personalData
            )
          );
    }
    return {
      ...month,
      days
    };
  });
  return monthsAndDays;
};

const checkIfPublicHoliday = (day, publicHolidays, personalData) => {
  if (!publicHolidays) return day;
  const publicHolidayCheck = publicHolidays.find(holiday =>
    moment(holiday.date).isSame(day.date, 'day')
  );
  return publicHolidayCheck
    ? { day, publicHoliday: publicHolidayCheck.name, personalData }
    : { day, personalData };
};

const checkIfPersonalDay = dayObject => {
  if (!dayObject.personalData.length && !dayObject.publicHoliday) {
    return { day: dayObject.day };
  } else if (!dayObject.personalData.length) {
    return { day: dayObject.day, publicHoliday: dayObject.publicHoliday };
  }
  const personalDataCheck = dayObject.personalData.find(data =>
    data.date.isSame(dayObject.day.date, 'day')
  );
  return personalDataCheck
    ? { ...dayObject, personalDay: { ...personalDataCheck } }
    : dayObject;
};

const checkIfWeekend = dayObject =>
  dayObject.day.date.day() === 0 || dayObject.day.date.day() === 6
    ? { ...dayObject, isWeekend: true }
    : dayObject;

const checkIfToday = dayObject =>
  moment(moment()).isSame(dayObject.day.date, 'day')
    ? { ...dayObject, isToday: true }
    : dayObject;
