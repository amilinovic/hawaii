import { combineReducers } from 'redux';

import employee from './randomUserApiReducer';
import authorization from './getTokenReducer';

export default combineReducers({
  authorization,
  employee
});
