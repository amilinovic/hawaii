import { handleActions } from 'redux-actions';
import {
  initDate,
  incrementYear,
  decrementYear
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
      selectedYear: action.payload.selectedYear + 1
    })
  }),
  [decrementYear]: (state, action) => ({
    ...state,
    selectedYear: action.payload.selectedYear - 1,
    table: initiateTable({
      selectedYear: action.payload.selectedYear - 1
    })
  })
};

const initiateTable = state => {
  const months = moment.months();
  const monthsWithDays = monthName => ({
    ...fillWithDays(monthName, months, state.selectedYear || state.year)
  });
  const monthObjects = months.map(monthName => ({
    name: monthName,
    ...monthsWithDays(monthName)
  }));
  return monthObjects;
};

const fillWithDays = (monthName, months, year) => {
  const monthLength = moment(
    `01-${months.indexOf(monthName) + 1}-${parseInt(year, 10)}`,
    `DD-MM-YYYY`
  )
    .endOf('month')
    .date();

  let monthDays = {};

  for (let i = 0; i < 31; i++) {
    i < monthLength
      ? (monthDays[i + 1] = addMetaData(monthName, i, year))
      : (monthDays[i + 1] = null);
  }

  return monthDays;
};

const addMetaData = (monthName, date, year) => {
  let dayObject = {};
  const dayOfWeek = moment(
    `${parseInt(date, 10) + 1}-${moment.months().indexOf(monthName) +
      1}-${parseInt(year, 10)}`,
    `DD-MM-YYYY`
  ).day();

  if (moment().year() === year) {
    if (
      monthName === moment.months(moment().month()) &&
      date === moment().date()
    ) {
      dayObject = { ...dayObject, today: true };
    }
  }

  if (dayOfWeek === 6 || dayOfWeek === 0) {
    dayObject = { ...dayObject, weekend: true };
  }

  return dayObject;
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
