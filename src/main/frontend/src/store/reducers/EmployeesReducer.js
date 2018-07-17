import { handleActions } from 'redux-actions';
import { RECEIVE_API_DATA } from '../actions/EmployeesActions';

export const initialState = {
  error: null,
  fetching: ''
};

const actionHandlers = {
  [RECEIVE_API_DATA]: (state, action) => ({ ...action.payload, fetching: null })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
