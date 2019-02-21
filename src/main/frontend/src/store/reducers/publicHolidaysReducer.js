import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receivePublicHolidays } from '../actions/publicHolidayActions';

export const initialState = null;

const actionHandlers = {
  [receivePublicHolidays]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
