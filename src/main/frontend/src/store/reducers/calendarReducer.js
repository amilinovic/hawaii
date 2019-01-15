import { handleActions } from 'redux-actions';
import {
  initDate,
  incrementYear,
  decrementYear,
  selectDay
} from '../actions/calendarActions';
import moment from 'moment';
export const initialState = {};

const actionHandlers = {
  [initDate]: (state, action) => ({
    ...action.payload,
    table: initiateTable(action.payload)
  }),
  [incrementYear]: (state, action) => ({
    ...state,
    selectedYear: action.payload.selectedYear + 1,
    table: initiateTable({
      ...state,
      selectedYear: action.payload.selectedYear + 1
    })
  }),
  [decrementYear]: (state, action) => ({
    ...state,
    selectedYear: action.payload.selectedYear - 1,
    table: initiateTable({
      ...state,
      selectedYear: action.payload.selectedYear - 1
    })
  }),
  [selectDay]: (state, action) => ({
    ...state,
    ...action.payload
  })
};

const initiateTable = state => {
  const months = moment.months();
  const monthsWithDays = monthName =>
    fillWithDays(
      monthName,
      months,
      state.selectedYear || state.year,
      state.publicHolidays
    );
  const monthObjects = months.map(monthName => ({
    name: monthName,
    days: [...monthsWithDays(monthName)]
  }));

  const publicHolidays = state.publicHolidays.map(holiday => ({
    date: moment(`${holiday.date}`, 'YYYY-MM-DD').date(),
    month: moment(`${holiday.date}`, 'YYYY-MM-DD').month(),
    year: moment(`${holiday.date}`, 'YYYY-MM-DD').year(),
    name: holiday.name
  }));

  const currentYearHolidays = publicHolidays.filter(
    holiday => holiday.year === (state.selectedYear || state.year)
  );

  const monthsWithMappedPublicHolidays = publicHolidayMetadata(
    monthObjects,
    currentYearHolidays
  );

  return state.selectedYear && state.selectedYear !== state.year
    ? monthsWithMappedPublicHolidays
    : setToday(state, monthsWithMappedPublicHolidays);
  // TODO vrackovic: Create function for addition of metadata when public holidays
};

const publicHolidayMetadata = (calendarObject, currentYearHolidays) => {
  const mappedHolidays = calendarObject.reduce((acc, month) => {
    let mappedMonth = { ...month };
    currentYearHolidays.map(holiday => {
      if (
        moment()
          .month(holiday.month)
          .format('MMMM') === month.name
      ) {
        mappedMonth = {
          ...month,
          days: mappedMonth.days.map(
            dayObj =>
              dayObj && dayObj.date === holiday.date
                ? { ...dayObj, publicHoliday: holiday.name }
                : dayObj
          )
        };
      }
      return holiday;
    });

    return acc.concat(mappedMonth || month);
  }, []);
  return mappedHolidays;
};

const fillWithDays = (monthName, months, year, publicHolidays) => {
  const monthLength = moment(
    `01-${months.indexOf(monthName) + 1}-${parseInt(year, 10)}`,
    `DD-MM-YYYY`
  )
    .endOf('month')
    .date();

  let monthDays = [];

  for (let i = 0; i < 31; i++) {
    i < monthLength
      ? (monthDays[i] = addMetaData(monthName, i, year, publicHolidays))
      : (monthDays[i] = null);
  }

  return monthDays;
};

const addMetaData = (monthName, date, year, publicHolidays) => {
  let dayObject = { date: date + 1 };
  const dayOfWeek = moment(
    `${parseInt(date, 10) + 1}-${moment.months().indexOf(monthName) +
      1}-${parseInt(year, 10)}`,
    `DD-MM-YYYY`
  ).day();

  if (dayOfWeek === 6 || dayOfWeek === 0) {
    dayObject = { ...dayObject, weekend: true };
  }

  return dayObject;
};

const setToday = (payload, calendar) => {
  const currentMonthObject = calendar.find(
    month => month.name === payload.currentMonth
  );

  const daysWithMarkedToday =
    currentMonthObject &&
    currentMonthObject.days.map(
      day => (day.date === payload.currentDay ? { ...day, today: true } : day)
    );

  return calendar.map(
    month =>
      month.name === payload.currentMonth
        ? { name: month.name, days: daysWithMarkedToday }
        : month
  );
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
