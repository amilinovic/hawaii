import { handleActions } from 'redux-actions';
import { receiveLeaveProfiles } from '../actions/leaveProfilesActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = [];

const actionHandlers = {
  [receiveLeaveProfiles]: (state, action) => action.payload,
  [navigateOut]: () => []
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
