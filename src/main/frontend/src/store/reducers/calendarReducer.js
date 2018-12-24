import { handleActions } from 'redux-actions';
import {
  initDate,
  incrementYear,
  decrementYear
} from '../actions/calendarActions';

export const initialState = {};

const actionHandlers = {
  [initDate]: (state, action) => action.payload,
  [incrementYear]: (state, action) => ({
    ...state,
    selectedYear: action.payload.selectedYear + 1
  }),
  [decrementYear]: (state, action) => ({
    ...state,
    selectedYear: action.payload.selectedYear - 1
  })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
