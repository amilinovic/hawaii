import { handleActions } from 'redux-actions';
import { receiveLeaveProfile } from '../actions/leaveProfileActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = null;

const actionHandlers = {
  [receiveLeaveProfile]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
