import { handleActions } from 'redux-actions';
import { receiveLeaveProfiles } from '../actions/leaveProfilesActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = null;

const actionHandlers = {
  [receiveLeaveProfiles]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
