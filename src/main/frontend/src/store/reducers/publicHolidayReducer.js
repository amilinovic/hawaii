import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receivePublicHolidays } from '../actions/publicHolidayActions';

export const initialState = [];

const actionHandlers = {
  [receivePublicHolidays]: (state, action) => action.payload,
  [navigateOut]: () => []
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
