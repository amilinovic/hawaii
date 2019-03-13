import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receivePublicHoliday } from '../actions/publicHolidayActions';

export const initialState = null;

const actionHandlers = {
  [receivePublicHoliday]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
