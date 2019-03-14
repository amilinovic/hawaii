import { handleActions } from 'redux-actions';
import { receiveLeaveType } from '../actions/leaveTypeActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = null;

const actionHandlers = {
  [receiveLeaveType]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
