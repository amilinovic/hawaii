import moment from 'moment';

const now = moment();
const year = now.year();
const months = moment.months();
const MOMENT_DATE_FORMAT = 'YYYY-MM-DD';

export const fillWithMonthsAndDays = (
  selectedYear = year,
  publicHolidays,
  personalData
) => [
  ...months.map(month => ({
    name: month,
    days: createDaysFromEmptyArray(
      selectedYear,
      month,
      publicHolidays,
      personalData
    )
  }))
];

const createDaysFromEmptyArray = (
  year,
  month,
  publicHolidays,
  personalData
) => {
  let calendarObject = new Array(31).fill([{}], 0).map((date, index) =>
    // TODO: Apply Composition
    addMetadata(
      checkDateValidity(convertDateToMomentObject(year, month, index + 1))
    )
  );

  if (publicHolidays) {
    calendarObject = addPublicHolidays(calendarObject, publicHolidays, year);
  }

  if (personalData) {
    // TODO: add personal data to calendar object
  }

  return calendarObject;
};
// TODO: Apply composition

const addPublicHolidays = (month, publicHolidays, selectedYear) => {
  if (!publicHolidays) return month;
  const momentPublicHolidays = convertPublicDatesToMoment(
    publicHolidays
  ).filter(holiday => holiday.date.year() === selectedYear);

  return month.map(day => (day ? addMetadata(day, momentPublicHolidays) : day));
};

const convertDateToMomentObject = (year, month, date) => ({
  date: moment(
    `${year}-${moment()
      .month(`${month}`)
      .format('M')}-${date}`,
    MOMENT_DATE_FORMAT
  )
});

const checkDateValidity = day => (day.date.isValid() ? day : null);

const addMetadata = (dayObject, publicHolidays) => {
  if (!dayObject) return dayObject;
  let dayWithMetadata = { ...dayObject };
  dayWithMetadata = checkIfToday(checkIfWeekend(dayWithMetadata));

  return checkForPublicHolidays(dayWithMetadata, publicHolidays);
};

const checkIfWeekend = day =>
  day.date.day() === 0 || day.date.day() === 6
    ? { ...day, weekend: true }
    : day;

const checkIfToday = day =>
  day.date.format(MOMENT_DATE_FORMAT) === moment().format(MOMENT_DATE_FORMAT)
    ? { ...day, today: true }
    : day;

const checkForPublicHolidays = (day, publicHolidays) => {
  if (!publicHolidays) return day;

  const publicHolidayCheck = publicHolidays.find(holiday =>
    holiday.date.isSame(day.date, 'day')
  );
  return publicHolidayCheck
    ? { ...day, publicHoliday: publicHolidayCheck.name }
    : day;
};

const convertPublicDatesToMoment = publicHolidays =>
  publicHolidays.map(holiday => ({
    name: holiday.name,
    date: moment(holiday.date, MOMENT_DATE_FORMAT)
  }));
