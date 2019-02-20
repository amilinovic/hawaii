import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receivePersonalDays } from '../actions/personalDaysActions';

export const initialState = [];

const actionHandlers = {
  [receivePersonalDays]: (state, action) => action.payload,
  [navigateOut]: () => []
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
