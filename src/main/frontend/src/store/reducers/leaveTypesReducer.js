import { handleActions } from 'redux-actions';
import { receiveLeaveTypes } from '../actions/leaveTypesActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = [];

const actionHandlers = {
  [receiveLeaveTypes]: (state, action) => action.payload,
  [navigateOut]: () => []
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
