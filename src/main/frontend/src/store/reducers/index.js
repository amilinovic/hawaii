import { combineReducers } from 'redux';

import employee from './RandomUserApiReducer';
import authorization from './GetTokenReducer';
import { routerReducer } from 'react-router-redux';

export default combineReducers({
  authorization: authorization,
  employee: employee,
  routerReducer: routerReducer
});
