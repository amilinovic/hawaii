import { combineReducers } from 'redux';

import employee from './RandomUserApiReducer';
import authorization from './AuthorizationReducer';
import authentication from './AuthenticateReducer';
import { routerReducer } from 'react-router-redux';

export default combineReducers({
  authentication: authentication,
  authorization: authorization,
  employee: employee,
  routerReducer: routerReducer
});
