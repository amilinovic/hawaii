import moment from 'moment';

const now = moment();
const year = now.year();
const months = moment.months();
const MOMENT_DATE_FORMAT = 'YYYY-MM-DD';

export const fillWithMonthsAndDays = () => [
  ...months.map(month => ({
    name: month,
    days: createDaysFromEmptyArray(year, month)
  }))
];

const createDaysFromEmptyArray = (year, month) =>
  new Array(31)
    .fill([{}], 0)
    .map((date, index) =>
      addMetadata(
        checkDateValidity(convertDateToMomentObject(year, month, index + 1))
      )
    );
// TODO: Apply composition

const convertDateToMomentObject = (year, month, date) => ({
  date: moment(
    `${year}-${moment()
      .month(`${month}`)
      .format('M')}-${date}`,
    MOMENT_DATE_FORMAT
  )
});

const checkDateValidity = day => (day.date.isValid() ? day : null);

const addMetadata = dayObject => {
  if (!dayObject) return dayObject;
  let dayMetadata = { ...dayObject };

  dayMetadata = checkIfToday(checkIfWeekend(dayMetadata));

  return dayMetadata;
};

const checkIfWeekend = day =>
  day.date.day() === 0 || day.date.day() === 6
    ? { ...day, weekend: true }
    : day;

const checkIfToday = day =>
  day.date.format(MOMENT_DATE_FORMAT) === moment().format(MOMENT_DATE_FORMAT)
    ? { ...day, today: true }
    : day;

export const filterExistingDays = (calendar, filterParameter, selectedYear) => {
  const { publicHolidays, personalData } = filterParameter;
  if (publicHolidays) {
    const selectedYearHolidays = publicHolidays.filter(
      holiday => holiday.date.year() === selectedYear
    );
    return findDatesInCalendarAndAddMetadata(calendar, selectedYearHolidays);
  }
  return calendar;
};

const findDatesInCalendarAndAddMetadata = (calendarObject, dates) =>
  dates.reduce((acc, holiday) => {
    const foundMonth =
      acc.length > 0
        ? acc.find(month => month.name === holiday.date.format('MMMM'))
        : calendarObject.find(
            month => month.name === holiday.date.format('MMMM')
          );

    const replacedDayFromFoundMonth = addHolidayMetadataToDay(
      foundMonth.days,
      holiday
    );
    return calendarObject.map(
      month =>
        month.name === foundMonth.name
          ? { name: foundMonth.name, days: [...replacedDayFromFoundMonth] }
          : month
    );
  }, []);

const addHolidayMetadataToDay = (month, holiday) =>
  month.map(
    day =>
      day.date.isSame(holiday.date, 'day')
        ? { ...day, publicHoliday: holiday.name }
        : day
  );

export const convertPublicDatesToMoment = publicHolidays =>
  publicHolidays.map(holiday => ({
    name: holiday.name,
    date: moment(holiday.date, MOMENT_DATE_FORMAT)
  }));
