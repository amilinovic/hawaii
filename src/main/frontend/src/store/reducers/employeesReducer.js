import { handleActions } from 'redux-actions';
import { receiveEmployees } from '../actions/employeesActions';

export const initialState = [];

const actionHandlers = {
  [receiveEmployees]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
