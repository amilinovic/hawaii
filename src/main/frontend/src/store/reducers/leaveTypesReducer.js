import { handleActions } from 'redux-actions';
import { receiveLeaveTypes } from '../actions/leaveTypesActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = null;

const actionHandlers = {
  [receiveLeaveTypes]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
