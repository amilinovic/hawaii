import { handleActions } from 'redux-actions';
import { receiveMyPersonalDays } from '../actions/myPersonalDaysActions';

export const initialState = [];

const actionHandlers = {
  [receiveMyPersonalDays]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
