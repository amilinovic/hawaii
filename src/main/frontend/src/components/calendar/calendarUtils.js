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
  let monthObject = new Array(31).fill({}).map((date, index) =>
    // TODO: Apply Composition
    addMetadata(
      checkDateValidity(convertDateToMomentObject(year, month, index + 1))
    )
  );

  if (publicHolidays || personalData) {
    monthObject = addFetchedData(
      monthObject,
      publicHolidays,
      personalData,
      year
    );
  }

  return monthObject;
};
// TODO: Apply Composition

const addFetchedData = (month, publicHolidays, personalData, selectedYear) => {
  if (!publicHolidays && !personalData) return month;

  const momentPublicHolidays = publicHolidays
    ? mapAndConvertToMoment(publicHolidays).filter(
        holiday => holiday.date.year() === selectedYear
      )
    : [];

  const momentPersonalData = personalData
    ? mapAndConvertToMoment(personalData).filter(
        data => data.date.year() === selectedYear
      )
    : [];

  return month.map(
    day =>
      day ? addMetadata(day, momentPublicHolidays, momentPersonalData) : day
  );
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

const addMetadata = (dayObject, publicHolidays, personalData) =>
  dayObject
    ? // TODO: Apply Composition
      checkIfPersonalDay(
        checkIfPublicHoliday(
          checkIfToday(checkIfWeekend(dayObject)),
          publicHolidays
        ),
        personalData
      )
    : null;

const checkIfWeekend = day =>
  day.date.day() === 0 || day.date.day() === 6
    ? { ...day, weekend: true }
    : day;

const checkIfToday = day =>
  moment(moment()).isSame(day.date, 'day') ? { ...day, today: true } : day;

const checkIfPublicHoliday = (day, publicHolidays) => {
  if (!publicHolidays) return day;
  const publicHolidayCheck = publicHolidays.find(holiday =>
    holiday.date.isSame(day.date, 'day')
  );
  return publicHolidayCheck
    ? { ...day, publicHoliday: publicHolidayCheck.name }
    : day;
};

const checkIfPersonalDay = (day, personalData) => {
  if (!personalData) return day;
  const personalDataCheck = personalData.find(data =>
    data.date.isSame(day.date, 'day')
  );
  return personalDataCheck
    ? { ...day, personalDay: { ...personalDataCheck } }
    : day;
};

const mapAndConvertToMoment = data =>
  data.map(item => ({
    ...item,
    date: moment(item.date, MOMENT_DATE_FORMAT)
  }));
