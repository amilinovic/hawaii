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
  [initDate]: (state, { payload }) => ({
    ...payload,
    ...state
  }),
  [incrementYear]: (state, { payload }) => ({
    ...state,
    selectedYear: payload.selectedYear + 1
  }),
  [decrementYear]: (state, { payload }) => ({
    ...state,
    selectedYear: payload.selectedYear - 1
  }),
  [selectDay]: (state, { payload }) => ({
    ...state,
    ...payload
  })
};

// const personalDayMetadata = (calendarObject, personalDays) => {
//   const mappedPersonalDays = calendarObject.reduce((acc, month) => {
//     let mappedMonth = { ...month };
//     personalDays.map(personalDay => {
//       mappedMonth = {
//         ...month,
//         days: mappedMonth.days.map(dayObj => {
//           return dayObj && dayObj.localdate === personalDay.date
//             ? { ...dayObj, personalDay: personalDay }
//             : dayObj;
//         })
//       };
//       return personalDay;
//     });
//     return acc.concat(mappedMonth || month);
//   }, []);
//   return mappedPersonalDays;
// };

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
