import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receivePersonalDays } from '../actions/personalDaysActions';

export const initialState = null;

const actionHandlers = {
  [receivePersonalDays]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
