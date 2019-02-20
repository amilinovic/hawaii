import { handleActions } from 'redux-actions';
import { receivePersonalDays } from '../actions/personalDaysActions';

export const initialState = [];

const actionHandlers = {
  [receivePersonalDays]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
