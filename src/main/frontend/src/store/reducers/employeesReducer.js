import { handleActions } from 'redux-actions';
import { receiveEmployees } from '../actions/employeesActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = null;

const actionHandlers = {
  [receiveEmployees]: (state, action) => action.payload.content,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
