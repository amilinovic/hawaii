import { handleActions } from 'redux-actions';
import {
  initDate,
  incrementYear,
  decrementYear,
  selectDay
} from '../actions/calendarActions';
export const initialState = {};

const actionHandlers = {
  [initDate]: (state, { payload }) => ({
    ...state,
    ...payload
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

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
