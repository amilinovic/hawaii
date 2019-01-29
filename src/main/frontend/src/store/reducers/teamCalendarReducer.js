import { handleActions } from 'redux-actions';
import { receiveUsers } from '../actions/teamCalendarActions';

export const initialState = [];

const actionHandlers = {
  [receiveUsers]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
