import * as _ from 'lodash';
import flow from 'lodash/flow';
import moment from 'moment';

export const createCalendar = (year, publicHolidays, personalData) =>
  flow([createMonths, createDays])(year, publicHolidays, personalData);

const createMonths = (year, publicHolidays, personalData) => {
  const months = moment.months().map(month => {
    return {
      name: month,
      year
    };
  });

  return { months, publicHolidays, personalData };
};

const createDays = data => {
  const monthsAndDays = data.months.map(month => {
    const days = [];

    for (let day = 1; day <= 31; day++) {
      const dayObject = {
        date: moment(`${day}-${month.name}-${month.year}`, 'DD-MMMM-YYYY')
      };
      day > moment(`${month.year}-${month.name}`, 'YYYY-MMMM').daysInMonth()
        ? days.push(null)
        : days.push(
            fillDaysWithData(dayObject, data.publicHolidays, data.personalData)
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

  return {
    day,
    personalData,
    publicHoliday: _.get(publicHolidayCheck, 'name')
  };
};

const checkIfPersonalDay = dayObject => {
  const { day, publicHoliday, personalData } = dayObject;
  if (!personalData.length) {
    return { day, publicHoliday };
  }

  const personalDay = personalData.find(personalDay =>
    personalDay.date.isSame(personalData.day.date, 'day')
  );

  return { ...dayObject, personalDay };
};

const checkIfWeekend = dayObject =>
  dayObject.day.date.day() === 0 || dayObject.day.date.day() === 6
    ? { ...dayObject, isWeekend: true }
    : dayObject;

const checkIfToday = dayObject =>
  moment().isSame(dayObject.day.date, 'day')
    ? { ...dayObject, isToday: true }
    : dayObject;

const fillDaysWithData = flow([
  checkIfPublicHoliday,
  checkIfPersonalDay,
  checkIfWeekend,
  checkIfToday
]);
