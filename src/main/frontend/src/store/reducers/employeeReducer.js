import { handleActions } from 'redux-actions';
import { receiveEmployee } from '../actions/employeeActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = [];

const actionHandlers = {
  [receiveEmployee]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
