import { handleActions } from 'redux-actions';
import { receiveLeaveTypes } from '../actions/leaveTypesActions';

export const initialState = [];

const actionHandlers = {
  [receiveLeaveTypes]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
