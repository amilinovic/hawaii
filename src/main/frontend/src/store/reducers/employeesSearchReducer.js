import { handleActions } from 'redux-actions';
import {
  clearEmployees,
  searchEmployeesSuccess
} from '../actions/employeesSearchActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = [];

const actionHandlers = {
  [searchEmployeesSuccess]: (state, action) => action.payload.content,
  [clearEmployees]: () => [],
  [navigateOut]: () => []
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
