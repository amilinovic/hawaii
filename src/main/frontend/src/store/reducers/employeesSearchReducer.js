import { handleActions } from 'redux-actions';
import {
  clearEmployees,
  searchEmployees,
  searchEmployeesSuccess
} from '../actions/employeesSearchActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = { isFetching: false, results: [] };

const actionHandlers = {
  [searchEmployees]: state => {
    return {
      ...state,
      isFetching: true
    };
  },
  [searchEmployeesSuccess]: (state, action) => {
    return {
      ...state,
      isFetching: false,
      results: action.payload.content
    };
  },
  [clearEmployees]: state => {
    return {
      ...state,
      isFetching: false,
      results: []
    };
  },
  [navigateOut]: state => {
    return {
      ...state,
      results: []
    };
  }
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
