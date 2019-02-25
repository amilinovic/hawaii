import { handleActions } from 'redux-actions';
import { clearEmployees, receiveEmployees } from '../actions/employeesActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = [];

const actionHandlers = {
  [receiveEmployees]: (state, action) => action.payload.content,
  [clearEmployees]: () => [],
  [navigateOut]: () => []
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
