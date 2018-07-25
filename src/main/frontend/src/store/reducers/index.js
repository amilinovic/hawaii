import { combineReducers } from 'redux';

import employee from './RandomUserApiReducer';
import authorization from './GetTokenReducer';

export default combineReducers({
  authorization,
  employee
});
