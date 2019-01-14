import { handleActions } from 'redux-actions';
import { receivePublicHolidays } from '../actions/publicHolidayActions';

export const initialState = [];

const actionHandlers = {
  [receivePublicHolidays]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
